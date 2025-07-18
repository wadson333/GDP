import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILabTestCatalog } from '../lab-test-catalog.model';
import { LabTestCatalogService } from '../service/lab-test-catalog.service';
import { LabTestCatalogFormGroup, LabTestCatalogFormService } from './lab-test-catalog-form.service';

@Component({
  standalone: true,
  selector: 'jhi-lab-test-catalog-update',
  templateUrl: './lab-test-catalog-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LabTestCatalogUpdateComponent implements OnInit {
  isSaving = false;
  labTestCatalog: ILabTestCatalog | null = null;

  protected labTestCatalogService = inject(LabTestCatalogService);
  protected labTestCatalogFormService = inject(LabTestCatalogFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LabTestCatalogFormGroup = this.labTestCatalogFormService.createLabTestCatalogFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ labTestCatalog }) => {
      this.labTestCatalog = labTestCatalog;
      if (labTestCatalog) {
        this.updateForm(labTestCatalog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const labTestCatalog = this.labTestCatalogFormService.getLabTestCatalog(this.editForm);
    if (labTestCatalog.id !== null) {
      this.subscribeToSaveResponse(this.labTestCatalogService.update(labTestCatalog));
    } else {
      this.subscribeToSaveResponse(this.labTestCatalogService.create(labTestCatalog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILabTestCatalog>>): void {
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

  protected updateForm(labTestCatalog: ILabTestCatalog): void {
    this.labTestCatalog = labTestCatalog;
    this.labTestCatalogFormService.resetForm(this.editForm, labTestCatalog);
  }
}
