import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IPatient } from '../patient.model';
import { CommonModule } from '@angular/common';
import { SkeletonModule } from 'primeng/skeleton';
import { TabViewModule } from 'primeng/tabview';
import { TagModule } from 'primeng/tag';
import { MessageModule } from 'primeng/message';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  selector: 'jhi-patient-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.scss'],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SkeletonModule,
    TabViewModule,
    TagModule,
    MessageModule,
  ],
})
export class PatientDetailComponent {
  patient = input<IPatient | null>(null);

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  getStatusSeverity(status: string | null | undefined): 'success' | 'warning' | 'danger' | 'info' | 'secondary' {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'INACTIVE':
        return 'warning';
      case 'DECEASED':
        return 'danger';
      case 'ARCHIVED':
        return 'secondary';
      default:
        return 'info';
    }
  }

  /**
   * Calcule l'IMC (Indice de Masse Corporelle)
   */
  calculateBMI(heightCm: number | null | undefined, weightKg: number | null | undefined): string {
    if (!heightCm || !weightKg) {
      return '-';
    }

    const heightM = heightCm / 100;
    const bmi = weightKg / (heightM * heightM);
    return bmi.toFixed(1);
  }

  /**
   * Vérifie si l'assurance est expirée
   */
  isInsuranceExpired(validTo: dayjs.Dayjs | null | undefined): boolean {
    if (!validTo) {
      return false;
    }
    return dayjs(validTo).isBefore(dayjs());
  }

  /**
   * Retourne le label du statut d'assurance
   */
  getInsuranceStatusLabel(validTo: dayjs.Dayjs | null | undefined): string {
    if (!validTo) {
      return 'gdpApp.patient.detail.insuranceStatusUnknown';
    }

    const now = dayjs();
    const expiryDate = dayjs(validTo);

    if (expiryDate.isBefore(now)) {
      return 'gdpApp.patient.detail.insuranceExpired';
    }

    const daysUntilExpiry = expiryDate.diff(now, 'days');
    if (daysUntilExpiry <= 30) {
      return 'gdpApp.patient.detail.insuranceExpiringSoon';
    }

    return 'gdpApp.patient.detail.insuranceValid';
  }
}
