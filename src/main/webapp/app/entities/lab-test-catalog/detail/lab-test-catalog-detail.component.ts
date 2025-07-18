import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ILabTestCatalog } from '../lab-test-catalog.model';

@Component({
  standalone: true,
  selector: 'jhi-lab-test-catalog-detail',
  templateUrl: './lab-test-catalog-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LabTestCatalogDetailComponent {
  labTestCatalog = input<ILabTestCatalog | null>(null);

  previousState(): void {
    window.history.back();
  }
}
