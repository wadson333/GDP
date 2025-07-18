import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { LabTestCatalogService } from '../service/lab-test-catalog.service';
import { ILabTestCatalog } from '../lab-test-catalog.model';
import { LabTestCatalogFormService } from './lab-test-catalog-form.service';

import { LabTestCatalogUpdateComponent } from './lab-test-catalog-update.component';

describe('LabTestCatalog Management Update Component', () => {
  let comp: LabTestCatalogUpdateComponent;
  let fixture: ComponentFixture<LabTestCatalogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let labTestCatalogFormService: LabTestCatalogFormService;
  let labTestCatalogService: LabTestCatalogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LabTestCatalogUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LabTestCatalogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LabTestCatalogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    labTestCatalogFormService = TestBed.inject(LabTestCatalogFormService);
    labTestCatalogService = TestBed.inject(LabTestCatalogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const labTestCatalog: ILabTestCatalog = { id: 456 };

      activatedRoute.data = of({ labTestCatalog });
      comp.ngOnInit();

      expect(comp.labTestCatalog).toEqual(labTestCatalog);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabTestCatalog>>();
      const labTestCatalog = { id: 123 };
      jest.spyOn(labTestCatalogFormService, 'getLabTestCatalog').mockReturnValue(labTestCatalog);
      jest.spyOn(labTestCatalogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labTestCatalog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: labTestCatalog }));
      saveSubject.complete();

      // THEN
      expect(labTestCatalogFormService.getLabTestCatalog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(labTestCatalogService.update).toHaveBeenCalledWith(expect.objectContaining(labTestCatalog));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabTestCatalog>>();
      const labTestCatalog = { id: 123 };
      jest.spyOn(labTestCatalogFormService, 'getLabTestCatalog').mockReturnValue({ id: null });
      jest.spyOn(labTestCatalogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labTestCatalog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: labTestCatalog }));
      saveSubject.complete();

      // THEN
      expect(labTestCatalogFormService.getLabTestCatalog).toHaveBeenCalled();
      expect(labTestCatalogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabTestCatalog>>();
      const labTestCatalog = { id: 123 };
      jest.spyOn(labTestCatalogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labTestCatalog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(labTestCatalogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
