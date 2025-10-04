import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMedication } from '../medication.model';

@Component({
  standalone: true,
  selector: 'jhi-medication-detail',
  templateUrl: './medication-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MedicationDetailComponent {
  medication = input<IMedication | null>(null);

  previousState(): void {
    window.history.back();
  }
}
