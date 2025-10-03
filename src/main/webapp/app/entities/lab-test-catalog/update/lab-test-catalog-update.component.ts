import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LabTestMethod } from 'app/entities/enumerations/lab-test-method.model';
import { SampleType } from 'app/entities/enumerations/sample-type.model';
import { LabTestType } from 'app/entities/enumerations/lab-test-type.model';
import { LabTestCatalogService } from '../service/lab-test-catalog.service';
import { ILabTestCatalog } from '../lab-test-catalog.model';
import { LabTestCatalogFormGroup, LabTestCatalogFormService } from './lab-test-catalog-form.service';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TranslateService } from '@ngx-translate/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { TooltipModule } from 'primeng/tooltip';
import { DividerModule } from 'primeng/divider';
import { ChipModule } from 'primeng/chip';
import { TagModule } from 'primeng/tag';

@Component({
  standalone: true,
  selector: 'jhi-lab-test-catalog-update',
  templateUrl: './lab-test-catalog-update.component.html',
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
    InputNumberModule,
    DropdownModule,
    CalendarModule,
    CheckboxModule,
    ButtonModule,
    InputTextModule,
    InputTextareaModule,
    InputNumberModule,
    DropdownModule,
    CalendarModule,
    CheckboxModule,
    ButtonModule,
    ProgressSpinnerModule,
    TooltipModule,
    DividerModule,
    ChipModule,
    TagModule,
  ],
})
export class LabTestCatalogUpdateComponent implements OnInit {
  @Input() labTestCatalog: ILabTestCatalog | null = null;
  @Input() isRenew = false;

  @Output() closeEvent = new EventEmitter<boolean>();
  @Output() saveNewLabVersionEvent = new EventEmitter<ILabTestCatalog>();
  isSaving = false;
  // loading = false;
  showValidationErrors = false;
  typesOptions = Object.values(LabTestType).map(type => ({ label: type, value: type }));
  methodsOptions = Object.values(LabTestMethod).map(method => ({ label: method, value: method }));
  sampleTypeOptions = Object.values(SampleType).map(sampleType => ({ label: sampleType, value: sampleType }));
  // labTestMethodValues = Object.keys(LabTestMethod);
  // sampleTypeValues = Object.keys(SampleType);
  // labTestTypeValues = Object.keys(LabTestType);
  saveAtLeastOneLabTest = false;

  protected labTestCatalogService = inject(LabTestCatalogService);
  protected labTestCatalogFormService = inject(LabTestCatalogFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected messageService = inject(MessageService);
  protected confirmationService = inject(ConfirmationService);
  protected translateService = inject(TranslateService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LabTestCatalogFormGroup = this.labTestCatalogFormService.createLabTestCatalogFormGroup();

  ngOnInit(): void {
    if (this.labTestCatalog) {
      this.updateForm(this.labTestCatalog);
      if (this.isRenew) {
        this.editForm.get('name')?.disable();
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  cancel(): void {
    this.closeEvent.emit(this.saveAtLeastOneLabTest);
  }

  save(): void {
    if (!this.editForm.valid) {
      this.showValidationErrors = true;
      return;
    }

    this.confirmationService.confirm({
      message: this.translateService.instant('gdpApp.labTestCatalog.confirmation.save'),
      header: this.translateService.instant('gdpApp.labTestCatalog.confirmation.header'),
      rejectButtonStyleClass: 'p-button-sm p-button-text',
      acceptButtonStyleClass: 'p-button-sm',
      accept: () => {
        this.isSaving = true;
        const labTestCatalog = this.labTestCatalogFormService.getLabTestCatalog(this.editForm);
        if (labTestCatalog.id === null) {
          labTestCatalog.active = true; // New or updated tests are active by default
        }
        if (labTestCatalog.referenceRangeLow && labTestCatalog.referenceRangeHigh) {
          if (labTestCatalog.referenceRangeLow >= labTestCatalog.referenceRangeHigh) {
            this.isSaving = false;
            this.messageService.add({
              severity: 'error',
              summary: this.translateService.instant('gdpApp.labTestCatalog.toast.saveError.title'),
              detail: this.translateService.instant('gdpApp.labTestCatalog.toast.saveError.referenceRangeError'),
            });
            return;
          }
        }
        labTestCatalog.version = labTestCatalog.version ?? 1; // Handle null version for new entities

        if (this.isRenew && this.labTestCatalog !== null) {
          const newLabTestCatalog = { ...labTestCatalog, id: null };
          this.subscribeToSaveResponse(this.labTestCatalogService.createNewVersion(this.labTestCatalog.id, newLabTestCatalog));
        } else if (labTestCatalog.id !== null) {
          this.subscribeToSaveResponse(this.labTestCatalogService.update(labTestCatalog), true);
        } else {
          this.subscribeToSaveResponse(this.labTestCatalogService.create(labTestCatalog));
        }
      },
    });
  }

  // createNewVersion(ltc: ILabTestCatalog): void {
  //   this.labTestCatalogService.prepareNewVersion(ltc.id).subscribe({
  //     next: res => {
  //       if (res.body) {
  //         this.saveNewLabVersionEvent.emit(res.body);
  //       }
  //     },
  //     error: () => {
  //       this.messageService.add({
  //         severity: 'error',
  //         summary: this.translateService.instant('gdpApp.labTestCatalog.newVersion.error.title'),
  //         detail: this.translateService.instant('gdpApp.labTestCatalog.newVersion.error.detail'),
  //       });
  //     },
  //   });
  // }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILabTestCatalog>>, isNew = false): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: response => this.onSaveSuccess(isNew, response.body ?? undefined),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(isNew: boolean, labTestCatalog?: ILabTestCatalog): void {
    this.messageService.add({
      severity: 'success',
      summary: this.translateService.instant('gdpApp.labTestCatalog.toast.saveSuccess.title'),
      detail: this.isRenew
        ? this.translateService.instant('gdpApp.labTestCatalog.toast.saveSuccess.detail')
        : this.translateService.instant('gdpApp.labTestCatalog.newVersion.success.detail'),
    });

    // Reset form but keep dialog open
    this.editForm = this.labTestCatalogFormService.createLabTestCatalogFormGroup();
    this.saveAtLeastOneLabTest = true;
    if (isNew) this.closeEvent.emit(this.saveAtLeastOneLabTest);
    if (this.isRenew) this.saveNewLabVersionEvent.emit(labTestCatalog);
  }

  protected onSaveError(): void {
    // Api for inheritance.
    this.messageService.add({
      severity: 'error',
      summary: this.translateService.instant('gdpApp.labTestCatalog.toast.saveError.title'),
      detail: this.isRenew
        ? this.translateService.instant('gdpApp.labTestCatalog.toast.saveError.detail')
        : this.translateService.instant('gdpApp.labTestCatalog.newVersion.error.detail'),
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(labTestCatalog: ILabTestCatalog): void {
    this.labTestCatalog = labTestCatalog;
    this.labTestCatalogFormService.resetForm(this.editForm, labTestCatalog);
  }
}
