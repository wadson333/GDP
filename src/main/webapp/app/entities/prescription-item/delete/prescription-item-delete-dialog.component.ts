import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPrescriptionItem } from '../prescription-item.model';
import { PrescriptionItemService } from '../service/prescription-item.service';

@Component({
  standalone: true,
  templateUrl: './prescription-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PrescriptionItemDeleteDialogComponent {
  prescriptionItem?: IPrescriptionItem;

  protected prescriptionItemService = inject(PrescriptionItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prescriptionItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
