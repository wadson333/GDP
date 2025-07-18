import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDoctorProfile } from '../doctor-profile.model';
import { DoctorProfileService } from '../service/doctor-profile.service';

@Component({
  standalone: true,
  templateUrl: './doctor-profile-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DoctorProfileDeleteDialogComponent {
  doctorProfile?: IDoctorProfile;

  protected doctorProfileService = inject(DoctorProfileService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.doctorProfileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
