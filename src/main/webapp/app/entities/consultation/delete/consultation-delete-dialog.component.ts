import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConsultation } from '../consultation.model';
import { ConsultationService } from '../service/consultation.service';

@Component({
  standalone: true,
  templateUrl: './consultation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConsultationDeleteDialogComponent {
  consultation?: IConsultation;

  protected consultationService = inject(ConsultationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consultationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
