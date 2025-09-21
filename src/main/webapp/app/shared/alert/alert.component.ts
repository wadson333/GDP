import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessagesModule } from 'primeng/messages';
import { Message } from 'primeng/api';

import { Alert, AlertService } from 'app/core/util/alert.service';

@Component({
  standalone: true,
  selector: 'jhi-alert',
  templateUrl: './alert.component.html',
  imports: [CommonModule, MessagesModule],
})
export class AlertComponent implements OnInit, OnDestroy {
  messages = signal<Message[]>([]);
  private alertService = inject(AlertService);

  ngOnInit(): void {
    // Convert Alert[] to Message[]
    this.alertService.get().forEach(alert => {
      this.messages.update(msgs => [...msgs, this.convertAlertToMessage(alert)]);
    });
  }

  ngOnDestroy(): void {
    this.alertService.clear();
    this.messages.set([]);
  }

  private convertAlertToMessage(alert: Alert): Message {
    return {
      severity: this.getSeverity(alert.type),
      summary: alert.type.charAt(0).toUpperCase() + alert.type.slice(1),
      detail: alert.message,
      closable: false,
    };
  }

  private getSeverity(type: string | undefined): string {
    switch (type) {
      case 'success':
        return 'success';
      case 'danger':
        return 'error';
      case 'warning':
        return 'warn';
      case 'info':
        return 'info';
      default:
        return 'info';
    }
  }
}
