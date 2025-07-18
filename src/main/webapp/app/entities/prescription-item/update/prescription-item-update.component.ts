import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedication } from 'app/entities/medication/medication.model';
import { MedicationService } from 'app/entities/medication/service/medication.service';
import { IPrescription } from 'app/entities/prescription/prescription.model';
import { PrescriptionService } from 'app/entities/prescription/service/prescription.service';
import { PrescriptionItemService } from '../service/prescription-item.service';
import { IPrescriptionItem } from '../prescription-item.model';
import { PrescriptionItemFormGroup, PrescriptionItemFormService } from './prescription-item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-prescription-item-update',
  templateUrl: './prescription-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PrescriptionItemUpdateComponent implements OnInit {
  isSaving = false;
  prescriptionItem: IPrescriptionItem | null = null;

  medicationsSharedCollection: IMedication[] = [];
  prescriptionsSharedCollection: IPrescription[] = [];

  protected prescriptionItemService = inject(PrescriptionItemService);
  protected prescriptionItemFormService = inject(PrescriptionItemFormService);
  protected medicationService = inject(MedicationService);
  protected prescriptionService = inject(PrescriptionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PrescriptionItemFormGroup = this.prescriptionItemFormService.createPrescriptionItemFormGroup();

  compareMedication = (o1: IMedication | null, o2: IMedication | null): boolean => this.medicationService.compareMedication(o1, o2);

  comparePrescription = (o1: IPrescription | null, o2: IPrescription | null): boolean =>
    this.prescriptionService.comparePrescription(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prescriptionItem }) => {
      this.prescriptionItem = prescriptionItem;
      if (prescriptionItem) {
        this.updateForm(prescriptionItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prescriptionItem = this.prescriptionItemFormService.getPrescriptionItem(this.editForm);
    if (prescriptionItem.id !== null) {
      this.subscribeToSaveResponse(this.prescriptionItemService.update(prescriptionItem));
    } else {
      this.subscribeToSaveResponse(this.prescriptionItemService.create(prescriptionItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrescriptionItem>>): void {
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

  protected updateForm(prescriptionItem: IPrescriptionItem): void {
    this.prescriptionItem = prescriptionItem;
    this.prescriptionItemFormService.resetForm(this.editForm, prescriptionItem);

    this.medicationsSharedCollection = this.medicationService.addMedicationToCollectionIfMissing<IMedication>(
      this.medicationsSharedCollection,
      prescriptionItem.medication,
    );
    this.prescriptionsSharedCollection = this.prescriptionService.addPrescriptionToCollectionIfMissing<IPrescription>(
      this.prescriptionsSharedCollection,
      prescriptionItem.prescription,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicationService
      .query()
      .pipe(map((res: HttpResponse<IMedication[]>) => res.body ?? []))
      .pipe(
        map((medications: IMedication[]) =>
          this.medicationService.addMedicationToCollectionIfMissing<IMedication>(medications, this.prescriptionItem?.medication),
        ),
      )
      .subscribe((medications: IMedication[]) => (this.medicationsSharedCollection = medications));

    this.prescriptionService
      .query()
      .pipe(map((res: HttpResponse<IPrescription[]>) => res.body ?? []))
      .pipe(
        map((prescriptions: IPrescription[]) =>
          this.prescriptionService.addPrescriptionToCollectionIfMissing<IPrescription>(prescriptions, this.prescriptionItem?.prescription),
        ),
      )
      .subscribe((prescriptions: IPrescription[]) => (this.prescriptionsSharedCollection = prescriptions));
  }
}
