import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [SharedModule, RouterModule, FormsModule, ReactiveFormsModule],
})
export default class DashboardComponent implements OnInit {
  // eslint-disable-next-line @angular-eslint/no-empty-lifecycle-method
  ngOnInit(): void {
    // Initialization logic can go here
  }

  previousState(): void {
    window.history.back();
  }
}
