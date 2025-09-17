import { Component, EventEmitter, input, Output } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';

import { User } from '../user-management.model';

import { AvatarModule } from 'primeng/avatar';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';

@Component({
  standalone: true,
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html',
  imports: [RouterModule, SharedModule, AvatarModule, TagModule, ButtonModule, CardModule],
})
export default class UserManagementDetailComponent {
  user = input<User | null>(null);
  @Output() closeEvent = new EventEmitter<void>();

  onClose(): void {
    this.closeEvent.emit();
  }
}
