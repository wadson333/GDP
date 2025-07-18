import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserConfiguration } from '../user-configuration.model';
import { UserConfigurationService } from '../service/user-configuration.service';

@Component({
  standalone: true,
  templateUrl: './user-configuration-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserConfigurationDeleteDialogComponent {
  userConfiguration?: IUserConfiguration;

  protected userConfigurationService = inject(UserConfigurationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
