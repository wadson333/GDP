import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LabTestCatalogDetailComponent } from './lab-test-catalog-detail.component';

describe('LabTestCatalog Management Detail Component', () => {
  let comp: LabTestCatalogDetailComponent;
  let fixture: ComponentFixture<LabTestCatalogDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LabTestCatalogDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./lab-test-catalog-detail.component').then(m => m.LabTestCatalogDetailComponent),
              resolve: { labTestCatalog: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LabTestCatalogDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LabTestCatalogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load labTestCatalog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LabTestCatalogDetailComponent);

      // THEN
      expect(instance.labTestCatalog()).toEqual(expect.objectContaining({ id: 123 }));
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
