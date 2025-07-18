import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILabTestResult } from '../lab-test-result.model';
import { LabTestResultService } from '../service/lab-test-result.service';

@Component({
  standalone: true,
  templateUrl: './lab-test-result-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LabTestResultDeleteDialogComponent {
  labTestResult?: ILabTestResult;

  protected labTestResultService = inject(LabTestResultService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.labTestResultService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
