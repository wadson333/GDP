import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMedication } from 'app/entities/medication/medication.model';
import { MedicationService } from 'app/entities/medication/service/medication.service';
import { IPrescription } from 'app/entities/prescription/prescription.model';
import { PrescriptionService } from 'app/entities/prescription/service/prescription.service';
import { IPrescriptionItem } from '../prescription-item.model';
import { PrescriptionItemService } from '../service/prescription-item.service';
import { PrescriptionItemFormService } from './prescription-item-form.service';

import { PrescriptionItemUpdateComponent } from './prescription-item-update.component';

describe('PrescriptionItem Management Update Component', () => {
  let comp: PrescriptionItemUpdateComponent;
  let fixture: ComponentFixture<PrescriptionItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prescriptionItemFormService: PrescriptionItemFormService;
  let prescriptionItemService: PrescriptionItemService;
  let medicationService: MedicationService;
  let prescriptionService: PrescriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PrescriptionItemUpdateComponent],
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
      .overrideTemplate(PrescriptionItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrescriptionItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prescriptionItemFormService = TestBed.inject(PrescriptionItemFormService);
    prescriptionItemService = TestBed.inject(PrescriptionItemService);
    medicationService = TestBed.inject(MedicationService);
    prescriptionService = TestBed.inject(PrescriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Medication query and add missing value', () => {
      const prescriptionItem: IPrescriptionItem = { id: 456 };
      const medication: IMedication = { id: 5321 };
      prescriptionItem.medication = medication;

      const medicationCollection: IMedication[] = [{ id: 17190 }];
      jest.spyOn(medicationService, 'query').mockReturnValue(of(new HttpResponse({ body: medicationCollection })));
      const additionalMedications = [medication];
      const expectedCollection: IMedication[] = [...additionalMedications, ...medicationCollection];
      jest.spyOn(medicationService, 'addMedicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prescriptionItem });
      comp.ngOnInit();

      expect(medicationService.query).toHaveBeenCalled();
      expect(medicationService.addMedicationToCollectionIfMissing).toHaveBeenCalledWith(
        medicationCollection,
        ...additionalMedications.map(expect.objectContaining),
      );
      expect(comp.medicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Prescription query and add missing value', () => {
      const prescriptionItem: IPrescriptionItem = { id: 456 };
      const prescription: IPrescription = { id: 2619 };
      prescriptionItem.prescription = prescription;

      const prescriptionCollection: IPrescription[] = [{ id: 30235 }];
      jest.spyOn(prescriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: prescriptionCollection })));
      const additionalPrescriptions = [prescription];
      const expectedCollection: IPrescription[] = [...additionalPrescriptions, ...prescriptionCollection];
      jest.spyOn(prescriptionService, 'addPrescriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prescriptionItem });
      comp.ngOnInit();

      expect(prescriptionService.query).toHaveBeenCalled();
      expect(prescriptionService.addPrescriptionToCollectionIfMissing).toHaveBeenCalledWith(
        prescriptionCollection,
        ...additionalPrescriptions.map(expect.objectContaining),
      );
      expect(comp.prescriptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prescriptionItem: IPrescriptionItem = { id: 456 };
      const medication: IMedication = { id: 15373 };
      prescriptionItem.medication = medication;
      const prescription: IPrescription = { id: 422 };
      prescriptionItem.prescription = prescription;

      activatedRoute.data = of({ prescriptionItem });
      comp.ngOnInit();

      expect(comp.medicationsSharedCollection).toContain(medication);
      expect(comp.prescriptionsSharedCollection).toContain(prescription);
      expect(comp.prescriptionItem).toEqual(prescriptionItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrescriptionItem>>();
      const prescriptionItem = { id: 123 };
      jest.spyOn(prescriptionItemFormService, 'getPrescriptionItem').mockReturnValue(prescriptionItem);
      jest.spyOn(prescriptionItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prescriptionItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prescriptionItem }));
      saveSubject.complete();

      // THEN
      expect(prescriptionItemFormService.getPrescriptionItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prescriptionItemService.update).toHaveBeenCalledWith(expect.objectContaining(prescriptionItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrescriptionItem>>();
      const prescriptionItem = { id: 123 };
      jest.spyOn(prescriptionItemFormService, 'getPrescriptionItem').mockReturnValue({ id: null });
      jest.spyOn(prescriptionItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prescriptionItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prescriptionItem }));
      saveSubject.complete();

      // THEN
      expect(prescriptionItemFormService.getPrescriptionItem).toHaveBeenCalled();
      expect(prescriptionItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrescriptionItem>>();
      const prescriptionItem = { id: 123 };
      jest.spyOn(prescriptionItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prescriptionItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prescriptionItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedication', () => {
      it('Should forward to medicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(medicationService, 'compareMedication');
        comp.compareMedication(entity, entity2);
        expect(medicationService.compareMedication).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePrescription', () => {
      it('Should forward to prescriptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(prescriptionService, 'comparePrescription');
        comp.comparePrescription(entity, entity2);
        expect(prescriptionService.comparePrescription).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
