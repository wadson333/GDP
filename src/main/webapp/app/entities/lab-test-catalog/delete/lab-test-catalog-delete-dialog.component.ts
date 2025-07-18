import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILabTestCatalog } from '../lab-test-catalog.model';
import { LabTestCatalogService } from '../service/lab-test-catalog.service';

@Component({
  standalone: true,
  templateUrl: './lab-test-catalog-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LabTestCatalogDeleteDialogComponent {
  labTestCatalog?: ILabTestCatalog;

  protected labTestCatalogService = inject(LabTestCatalogService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.labTestCatalogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
