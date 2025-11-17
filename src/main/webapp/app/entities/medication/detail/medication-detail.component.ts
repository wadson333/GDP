import { Component, inject, Input, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMedication } from '../medication.model';

import { TabViewModule } from 'primeng/tabview';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { BadgeModule } from 'primeng/badge';
import { TagModule } from 'primeng/tag';
import { AccordionModule } from 'primeng/accordion';
import { TranslateModule } from '@ngx-translate/core';
import dayjs from 'dayjs';
import { NgxBarcode6Module } from 'ngx-barcode6';
import { ExportService } from 'app/shared/services/export-file.service';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  standalone: true,
  selector: 'jhi-medication-detail',
  templateUrl: './medication-detail.component.html',
  styleUrls: ['./medication-detail.component.scss'],
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    TranslateModule,
    TabViewModule,
    CardModule,
    ButtonModule,
    BadgeModule,
    TagModule,
    AccordionModule,
    FormatMediumDatetimePipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    NgxBarcode6Module,
    TooltipModule,
  ],
})
export class MedicationDetailComponent {
  @Input() medication: IMedication | null = null;
  activeTab = 0;

  protected exportService = inject(ExportService);

  isExpired(): boolean {
    if (!this.medication?.expiryDate) return false;
    return dayjs(this.medication.expiryDate).isBefore(dayjs());
  }

  isNearExpiry(): boolean {
    if (!this.medication?.expiryDate) return false;
    const expiryDate = dayjs(this.medication.expiryDate);
    const threeMonthsFromNow = dayjs().add(3, 'month');
    return expiryDate.isAfter(dayjs()) && expiryDate.isBefore(threeMonthsFromNow);
  }

  previousState(): void {
    window.history.back();
  }

  exportDetailsToPdf(): void {
    if (!this.medication) return;

    const title = `Medication Details - ${this.medication.name}`;
    this.exportService.exportMedicationDetailsToPdf(this.medication, title);
  }
}
