import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPrescription } from '../prescription.model';
import { PrescriptionService } from '../service/prescription.service';
import { PrescriptionFormGroup, PrescriptionFormService } from './prescription-form.service';

@Component({
  standalone: true,
  selector: 'jhi-prescription-update',
  templateUrl: './prescription-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PrescriptionUpdateComponent implements OnInit {
  isSaving = false;
  prescription: IPrescription | null = null;

  protected prescriptionService = inject(PrescriptionService);
  protected prescriptionFormService = inject(PrescriptionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PrescriptionFormGroup = this.prescriptionFormService.createPrescriptionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prescription }) => {
      this.prescription = prescription;
      if (prescription) {
        this.updateForm(prescription);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prescription = this.prescriptionFormService.getPrescription(this.editForm);
    if (prescription.id !== null) {
      this.subscribeToSaveResponse(this.prescriptionService.update(prescription));
    } else {
      this.subscribeToSaveResponse(this.prescriptionService.create(prescription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrescription>>): void {
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

  protected updateForm(prescription: IPrescription): void {
    this.prescription = prescription;
    this.prescriptionFormService.resetForm(this.editForm, prescription);
  }
}
