import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ILabTestResult } from '../lab-test-result.model';

@Component({
  standalone: true,
  selector: 'jhi-lab-test-result-detail',
  templateUrl: './lab-test-result-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LabTestResultDetailComponent {
  labTestResult = input<ILabTestResult | null>(null);

  previousState(): void {
    window.history.back();
  }
}
