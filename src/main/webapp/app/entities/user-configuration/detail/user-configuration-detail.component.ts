import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserConfiguration } from '../user-configuration.model';

@Component({
  standalone: true,
  selector: 'jhi-user-configuration-detail',
  templateUrl: './user-configuration-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserConfigurationDetailComponent {
  userConfiguration = input<IUserConfiguration | null>(null);

  previousState(): void {
    window.history.back();
  }
}
