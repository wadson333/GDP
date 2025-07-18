import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { MedicationService } from '../service/medication.service';
import { IMedication } from '../medication.model';
import { MedicationFormGroup, MedicationFormService } from './medication-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medication-update',
  templateUrl: './medication-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicationUpdateComponent implements OnInit {
  isSaving = false;
  medication: IMedication | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected medicationService = inject(MedicationService);
  protected medicationFormService = inject(MedicationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicationFormGroup = this.medicationFormService.createMedicationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medication }) => {
      this.medication = medication;
      if (medication) {
        this.updateForm(medication);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gdpApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medication = this.medicationFormService.getMedication(this.editForm);
    if (medication.id !== null) {
      this.subscribeToSaveResponse(this.medicationService.update(medication));
    } else {
      this.subscribeToSaveResponse(this.medicationService.create(medication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedication>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medication: IMedication): void {
    this.medication = medication;
    this.medicationFormService.resetForm(this.editForm, medication);
  }
}
