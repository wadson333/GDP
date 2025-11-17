import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, finalize, switchMap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { RouteAdmin } from 'app/entities/enumerations/route-admin.model';
import { PrescriptionStatus } from 'app/entities/enumerations/prescription-status.model';
import { RiskLevel } from 'app/entities/enumerations/risk-level.model';
import { MedicationService } from '../service/medication.service';
import { IMedication } from '../medication.model';
import { MedicationFormGroup, MedicationFormService } from './medication-form.service';

import { StepsModule } from 'primeng/steps';
import { DropdownModule } from 'primeng/dropdown';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { CheckboxModule } from 'primeng/checkbox';
import { MessageService } from 'primeng/api';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  standalone: true,
  selector: 'jhi-medication-update',
  templateUrl: './medication-update.component.html',
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    StepsModule,
    DropdownModule,
    InputTextareaModule,
    FileUploadModule,
    InputTextModule,
    InputNumberModule,
    CheckboxModule,
    CalendarModule,
    ButtonModule,
    TooltipModule,
    TranslateModule,
  ],
  styleUrls: ['./medication-update.component.scss'],
  providers: [MessageService],
})
export class MedicationUpdateComponent implements OnInit {
  // isSaving = false;
  // medication: IMedication | null = null;
  // routeAdminValues = Object.keys(RouteAdmin);
  // prescriptionStatusValues = Object.keys(PrescriptionStatus);
  // riskLevelValues = Object.keys(RiskLevel);

  // protected medicationService = inject(MedicationService);
  // protected medicationFormService = inject(MedicationFormService);
  // protected activatedRoute = inject(ActivatedRoute);

  // // eslint-disable-next-line @typescript-eslint/member-ordering
  // editForm: MedicationFormGroup = this.medicationFormService.createMedicationFormGroup();

  // ngOnInit(): void {
  //   this.activatedRoute.data.subscribe(({ medication }) => {
  //     this.medication = medication;
  //     if (medication) {
  //       this.updateForm(medication);
  //     }
  //   });
  // }

  // previousState(): void {
  //   window.history.back();
  // }

  // save(): void {
  //   this.isSaving = true;
  //   const medication = this.medicationFormService.getMedication(this.editForm);
  //   if (medication.id !== null) {
  //     this.subscribeToSaveResponse(this.medicationService.update(medication));
  //   } else {
  //     this.subscribeToSaveResponse(this.medicationService.create(medication));
  //   }
  // }

  // protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedication>>): void {
  //   result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
  //     next: () => this.onSaveSuccess(),
  //     error: () => this.onSaveError(),
  //   });
  // }

  // protected onSaveSuccess(): void {
  //   this.previousState();
  // }

  // protected onSaveError(): void {
  //   // Api for inheritance.
  // }

  // protected onSaveFinalize(): void {
  //   this.isSaving = false;
  // }
  isSaving = false;
  medication: IMedication | null = null;
  activeStep = 0;

  routeAdminValues = Object.entries(RouteAdmin).map(([key, value]) => ({
    label: key,
    value: key,
  }));

  prescriptionStatusValues = Object.entries(PrescriptionStatus).map(([key, value]) => ({
    label: key,
    value: key,
  }));

  riskLevelValues = Object.entries(RiskLevel).map(([key, value]) => ({
    label: key,
    value: key,
  }));

  steps = [
    { label: 'gdpApp.medication.form.steps.basic', complete: false },
    { label: 'gdpApp.medication.form.steps.details', complete: false },
    { label: 'gdpApp.medication.form.steps.medical', complete: false },
  ];

  uploadedImage: File | null = null;
  imagePreview: string | null = null;

  protected messageService = inject(MessageService);
  protected medicationService = inject(MedicationService);
  protected medicationFormService = inject(MedicationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FormGroup = this.medicationFormService.createMedicationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medication }) => {
      this.medication = medication;
      if (medication) {
        this.updateForm(medication);
      }
    });

    // Setup name uniqueness validator
    this.editForm
      .get('name')
      ?.valueChanges.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(name => this.checkNameUniqueness(name)),
      )
      .subscribe(isUnique => {
        const nameControl = this.editForm.get('name');
        if (!isUnique && nameControl) {
          nameControl.setErrors({ notUnique: true });
        }
      });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    if (this.editForm.invalid) {
      this.markAllFieldsAsTouched();
      return;
    }

    this.isSaving = true;
    const medication = this.medicationFormService.getMedication(this.editForm);

    if (medication.id !== null) {
      if (this.uploadedImage) {
        // If we have a new image, upload it first
        this.uploadImageAndSave(medication);
      } else {
        this.subscribeToSaveResponse(this.medicationService.update(medication));
      }
    } else {
      // For new medications, create first then handle image
      this.subscribeToSaveResponse(this.medicationService.create(medication));
    }
  }

  onImageSelected(event: any): void {
    const file = event.files[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'medication.form.image.error.size',
        });
        return;
      }

      this.uploadedImage = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  canProceedToNextStep(): boolean {
    if (this.activeStep === 0) {
      const requiredFields = ['name', 'routeOfAdministration', 'prescriptionStatus'];
      return requiredFields.every(field => this.editForm.get(field)?.valid);
    }
    return true;
  }

  nextStep(): void {
    if (this.canProceedToNextStep()) {
      this.steps[this.activeStep].complete = true;
      this.activeStep++;
    }
  }

  prevStep(): void {
    this.activeStep--;
  }

  protected updateForm(medication: IMedication): void {
    this.medication = medication;
    this.medicationFormService.resetForm(this.editForm, medication);
  }

  private markAllFieldsAsTouched(): void {
    Object.keys(this.editForm.controls).forEach(key => {
      const control = this.editForm.get(key);
      control?.markAsTouched();
    });
  }

  private checkNameUniqueness(name: string): Observable<boolean> {
    // Don't check if name is empty or it's the same name in edit mode
    if (!name || (this.medication && this.medication.name === name)) {
      return of(true);
    }
    return this.medicationService.checkNameUniqueness(name);
  }

  private uploadImageAndSave(medication: IMedication): void {
    if (!this.uploadedImage || !medication.id) return;

    const formData = new FormData();
    formData.append('file', this.uploadedImage);

    this.medicationService.uploadImage(medication.id, formData).subscribe({
      next: response => {
        if (response.body?.image) {
          // Update the medication with the new image URL
          medication.image = response.body.image;
          this.subscribeToSaveResponse(this.medicationService.update(medication));
        } else {
          this.onSaveError();
        }
      },
      error: () => this.onSaveError(),
    });
  }

  // Update the subscribeToSaveResponse to handle image upload for new medications
  private subscribeToSaveResponse(result: Observable<HttpResponse<IMedication>>): void {
    result.pipe(finalize(() => (this.isSaving = false))).subscribe({
      next: response => {
        if (!this.medication?.id && this.uploadedImage && response.body) {
          // For new medications, upload image after creation
          this.uploadImageAndSave(response.body);
        } else {
          this.onSaveSuccess();
        }
      },
      error: () => this.onSaveError(),
    });
  }

  private onSaveSuccess(): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: 'gdpApp.medication.form.saveSuccess',
    });
    this.previousState();
  }

  private onSaveError(): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: 'gdpApp.medication.form.saveError',
    });
  }
}
