import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PrescriptionService } from '../service/prescription.service';
import { IPrescription } from '../prescription.model';
import { PrescriptionFormService } from './prescription-form.service';

import { PrescriptionUpdateComponent } from './prescription-update.component';

describe('Prescription Management Update Component', () => {
  let comp: PrescriptionUpdateComponent;
  let fixture: ComponentFixture<PrescriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prescriptionFormService: PrescriptionFormService;
  let prescriptionService: PrescriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PrescriptionUpdateComponent],
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
      .overrideTemplate(PrescriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrescriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prescriptionFormService = TestBed.inject(PrescriptionFormService);
    prescriptionService = TestBed.inject(PrescriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const prescription: IPrescription = { id: 456 };

      activatedRoute.data = of({ prescription });
      comp.ngOnInit();

      expect(comp.prescription).toEqual(prescription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrescription>>();
      const prescription = { id: 123 };
      jest.spyOn(prescriptionFormService, 'getPrescription').mockReturnValue(prescription);
      jest.spyOn(prescriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prescription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prescription }));
      saveSubject.complete();

      // THEN
      expect(prescriptionFormService.getPrescription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prescriptionService.update).toHaveBeenCalledWith(expect.objectContaining(prescription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrescription>>();
      const prescription = { id: 123 };
      jest.spyOn(prescriptionFormService, 'getPrescription').mockReturnValue({ id: null });
      jest.spyOn(prescriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prescription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prescription }));
      saveSubject.complete();

      // THEN
      expect(prescriptionFormService.getPrescription).toHaveBeenCalled();
      expect(prescriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrescription>>();
      const prescription = { id: 123 };
      jest.spyOn(prescriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prescription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prescriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
