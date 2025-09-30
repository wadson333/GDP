import { Component, inject, input, OnInit, signal } from '@angular/core';
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
  ],
})
export class LabTestCatalogDetailComponent implements OnInit {
  labTestCatalog = input<ILabTestCatalog | null>(null);
  activeTab = signal<number>(0);

  // private confirmationService: ConfirmationService,
  // private messageService: MessageService,
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  ngOnInit(): void {
    // Set active tab from URL
    const tab = this.activatedRoute.snapshot.queryParamMap.get('tab');
    // this.activeTab.set(tab === 'history' ? 1 : 0);
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
    // Implement new version creation logic
  }
}
