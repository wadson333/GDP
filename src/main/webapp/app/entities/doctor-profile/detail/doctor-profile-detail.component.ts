import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDoctorProfile } from '../doctor-profile.model';

@Component({
  standalone: true,
  selector: 'jhi-doctor-profile-detail',
  templateUrl: './doctor-profile-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DoctorProfileDetailComponent {
  doctorProfile = input<IDoctorProfile | null>(null);

  previousState(): void {
    window.history.back();
  }
}
