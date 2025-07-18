import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LabTestResultDetailComponent } from './lab-test-result-detail.component';

describe('LabTestResult Management Detail Component', () => {
  let comp: LabTestResultDetailComponent;
  let fixture: ComponentFixture<LabTestResultDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LabTestResultDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./lab-test-result-detail.component').then(m => m.LabTestResultDetailComponent),
              resolve: { labTestResult: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LabTestResultDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabTestResultDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load labTestResult on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LabTestResultDetailComponent);

      // THEN
      expect(instance.labTestResult()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
