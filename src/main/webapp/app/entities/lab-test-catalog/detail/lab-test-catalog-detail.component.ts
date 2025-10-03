import { Component, inject, Input, input, OnInit, Output, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ILabTestCatalog } from '../lab-test-catalog.model';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ChipModule } from 'primeng/chip';
import { TagModule } from 'primeng/tag';
import { TimelineModule } from 'primeng/timeline';
import { TabViewModule } from 'primeng/tabview';
import { LabTestCatalogService } from '../service/lab-test-catalog.service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import { LabTestCatalogUpdateComponent } from '../update/lab-test-catalog-update.component';
import { DialogModule } from 'primeng/dialog';

@Component({
  standalone: true,
  selector: 'jhi-lab-test-catalog-detail',
  templateUrl: './lab-test-catalog-detail.component.html',
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ButtonModule,
    DividerModule,
    ChipModule,
    TagModule,
    TabViewModule,
    TimelineModule,
    ProgressSpinnerModule,
    TooltipModule,
    DialogModule,
    LabTestCatalogUpdateComponent,
  ],
})
export class LabTestCatalogDetailComponent implements OnInit {
  @Input() labTestCatalog: ILabTestCatalog | null = null;
  @Output() labTestNewVersion: ILabTestCatalog | null = null;

  labtestToUpdate: ILabTestCatalog | null = null;

  displayFormDialog = false;
  activeTab = signal<number>(0);
  loading = signal<boolean>(false);
  versions: ILabTestCatalog[] = [];

  protected messageService = inject(MessageService);
  protected confirmationService = inject(ConfirmationService);
  protected translateService = inject(TranslateService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private labTestCatalogService = inject(LabTestCatalogService);

  ngOnInit(): void {
    // Set active tab from URL
    const tab = this.activatedRoute.snapshot.queryParamMap.get('tab');
    if (this.labTestCatalog) {
      this.loadHistory(this.labTestCatalog);
    }
  }

  onTabChange(event: any): void {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { tab: event.index === 0 ? 'details' : 'history' },
      queryParamsHandling: 'merge',
    });
  }

  previousState(): void {
    window.history.back();
  }

  loadHistory(ltc: ILabTestCatalog): void {
    this.loading.set(true);
    this.labTestCatalogService.getHistory(ltc.name!).subscribe({
      next: res => {
        if (res.body) {
          this.versions = res.body;
        }
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  isLastversion(version: ILabTestCatalog): boolean {
    return this.versions[0].id === version.id;
  }

  deactivate(ltc: ILabTestCatalog): void {
    this.confirmationService.confirm({
      message: this.translateService.instant('gdpApp.labTestCatalog.deactivate.confirmation'),
      header: this.translateService.instant('gdpApp.labTestCatalog.deactivate.title'),
      icon: 'pi pi-exclamation-triangle',
      rejectButtonStyleClass: 'p-button-sm p-button-text',
      acceptButtonStyleClass: 'p-button-sm',
      accept: () => {
        this.loading.set(true);
        this.labTestCatalogService.deactivate(ltc.id).subscribe({
          next: res => {
            if (res.body) {
              this.labTestCatalog = res.body;
              this.messageService.add({
                severity: 'success',
                summary: this.translateService.instant('gdpApp.labTestCatalog.deactivate.success.title'),
                detail: this.translateService.instant('gdpApp.labTestCatalog.deactivate.success.detail'),
              });
              // Reload history if needed
              this.loadHistory(res.body);
            }
            this.loading.set(false);
          },

          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: this.translateService.instant('gdpApp.labTestCatalog.deactivate.error.title'),
              detail: this.translateService.instant('gdpApp.labTestCatalog.deactivate.error.detail'),
            });
            this.loading.set(false);
          },
        });
      },
    });
  }

  // createNewVersion(ltc: ILabTestCatalog): void {
  //   this.labTestCatalogService.prepareNewVersion(ltc.id).subscribe({
  //     next: res => {
  //       if (res.body) {
  //         this.showNewVersionDialog(res.body);
  //       }
  //     },
  //     error: () => {
  //       this.messageService.add({
  //         severity: 'error',
  //         summary: this.translateService.instant('gdpApp.labTestCatalog.newVersion.error.title'),
  //         detail: this.translateService.instant('gdpApp.labTestCatalog.newVersion.error.detail'),
  //       });
  //     },
  //   });
  // }
  onDialogHide(result?: ILabTestCatalog): void {
    this.displayFormDialog = false;

    if (result) {
      // Update the current view with new version
      this.labTestCatalog = result;
      // Reload history
      this.loadHistory(result);
      // Show success message
      // this.messageService.add({
      //   severity: 'success',
      //   summary: this.translateService.instant('gdpApp.labTestCatalog.newVersion.success.title'),
      //   detail: this.translateService.instant('gdpApp.labTestCatalog.newVersion.success.detail'),
      // });
    }
  }
  showNewVersionDialog(preFilled: ILabTestCatalog): void {
    this.displayFormDialog = true;
  }
}
