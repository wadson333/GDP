import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPrescriptionItem } from '../prescription-item.model';

@Component({
  standalone: true,
  selector: 'jhi-prescription-item-detail',
  templateUrl: './prescription-item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PrescriptionItemDetailComponent {
  prescriptionItem = input<IPrescriptionItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
