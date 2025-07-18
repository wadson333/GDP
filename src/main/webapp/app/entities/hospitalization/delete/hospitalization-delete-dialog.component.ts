import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHospitalization } from '../hospitalization.model';
import { HospitalizationService } from '../service/hospitalization.service';

@Component({
  standalone: true,
  templateUrl: './hospitalization-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HospitalizationDeleteDialogComponent {
  hospitalization?: IHospitalization;

  protected hospitalizationService = inject(HospitalizationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hospitalizationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
