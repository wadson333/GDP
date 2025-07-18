import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { ILabTestCatalog } from 'app/entities/lab-test-catalog/lab-test-catalog.model';
import { LabTestCatalogService } from 'app/entities/lab-test-catalog/service/lab-test-catalog.service';
import { IConsultation } from 'app/entities/consultation/consultation.model';
import { ConsultationService } from 'app/entities/consultation/service/consultation.service';
import { ILabTestResult } from '../lab-test-result.model';
import { LabTestResultService } from '../service/lab-test-result.service';
import { LabTestResultFormService } from './lab-test-result-form.service';

import { LabTestResultUpdateComponent } from './lab-test-result-update.component';

describe('LabTestResult Management Update Component', () => {
  let comp: LabTestResultUpdateComponent;
  let fixture: ComponentFixture<LabTestResultUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let labTestResultFormService: LabTestResultFormService;
  let labTestResultService: LabTestResultService;
  let patientService: PatientService;
  let labTestCatalogService: LabTestCatalogService;
  let consultationService: ConsultationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LabTestResultUpdateComponent],
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
      .overrideTemplate(LabTestResultUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LabTestResultUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    labTestResultFormService = TestBed.inject(LabTestResultFormService);
    labTestResultService = TestBed.inject(LabTestResultService);
    patientService = TestBed.inject(PatientService);
    labTestCatalogService = TestBed.inject(LabTestCatalogService);
    consultationService = TestBed.inject(ConsultationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Patient query and add missing value', () => {
      const labTestResult: ILabTestResult = { id: 456 };
      const patient: IPatient = { id: 23587 };
      labTestResult.patient = patient;

      const patientCollection: IPatient[] = [{ id: 18356 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ labTestResult });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call LabTestCatalog query and add missing value', () => {
      const labTestResult: ILabTestResult = { id: 456 };
      const labTest: ILabTestCatalog = { id: 970 };
      labTestResult.labTest = labTest;

      const labTestCatalogCollection: ILabTestCatalog[] = [{ id: 1681 }];
      jest.spyOn(labTestCatalogService, 'query').mockReturnValue(of(new HttpResponse({ body: labTestCatalogCollection })));
      const additionalLabTestCatalogs = [labTest];
      const expectedCollection: ILabTestCatalog[] = [...additionalLabTestCatalogs, ...labTestCatalogCollection];
      jest.spyOn(labTestCatalogService, 'addLabTestCatalogToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ labTestResult });
      comp.ngOnInit();

      expect(labTestCatalogService.query).toHaveBeenCalled();
      expect(labTestCatalogService.addLabTestCatalogToCollectionIfMissing).toHaveBeenCalledWith(
        labTestCatalogCollection,
        ...additionalLabTestCatalogs.map(expect.objectContaining),
      );
      expect(comp.labTestCatalogsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Consultation query and add missing value', () => {
      const labTestResult: ILabTestResult = { id: 456 };
      const consultation: IConsultation = { id: 25746 };
      labTestResult.consultation = consultation;

      const consultationCollection: IConsultation[] = [{ id: 739 }];
      jest.spyOn(consultationService, 'query').mockReturnValue(of(new HttpResponse({ body: consultationCollection })));
      const additionalConsultations = [consultation];
      const expectedCollection: IConsultation[] = [...additionalConsultations, ...consultationCollection];
      jest.spyOn(consultationService, 'addConsultationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ labTestResult });
      comp.ngOnInit();

      expect(consultationService.query).toHaveBeenCalled();
      expect(consultationService.addConsultationToCollectionIfMissing).toHaveBeenCalledWith(
        consultationCollection,
        ...additionalConsultations.map(expect.objectContaining),
      );
      expect(comp.consultationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const labTestResult: ILabTestResult = { id: 456 };
      const patient: IPatient = { id: 28861 };
      labTestResult.patient = patient;
      const labTest: ILabTestCatalog = { id: 11957 };
      labTestResult.labTest = labTest;
      const consultation: IConsultation = { id: 14276 };
      labTestResult.consultation = consultation;

      activatedRoute.data = of({ labTestResult });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContain(patient);
      expect(comp.labTestCatalogsSharedCollection).toContain(labTest);
      expect(comp.consultationsSharedCollection).toContain(consultation);
      expect(comp.labTestResult).toEqual(labTestResult);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabTestResult>>();
      const labTestResult = { id: 123 };
      jest.spyOn(labTestResultFormService, 'getLabTestResult').mockReturnValue(labTestResult);
      jest.spyOn(labTestResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labTestResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: labTestResult }));
      saveSubject.complete();

      // THEN
      expect(labTestResultFormService.getLabTestResult).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(labTestResultService.update).toHaveBeenCalledWith(expect.objectContaining(labTestResult));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabTestResult>>();
      const labTestResult = { id: 123 };
      jest.spyOn(labTestResultFormService, 'getLabTestResult').mockReturnValue({ id: null });
      jest.spyOn(labTestResultService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labTestResult: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: labTestResult }));
      saveSubject.complete();

      // THEN
      expect(labTestResultFormService.getLabTestResult).toHaveBeenCalled();
      expect(labTestResultService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILabTestResult>>();
      const labTestResult = { id: 123 };
      jest.spyOn(labTestResultService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ labTestResult });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(labTestResultService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePatient', () => {
      it('Should forward to patientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(patientService, 'comparePatient');
        comp.comparePatient(entity, entity2);
        expect(patientService.comparePatient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLabTestCatalog', () => {
      it('Should forward to labTestCatalogService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(labTestCatalogService, 'compareLabTestCatalog');
        comp.compareLabTestCatalog(entity, entity2);
        expect(labTestCatalogService.compareLabTestCatalog).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareConsultation', () => {
      it('Should forward to consultationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(consultationService, 'compareConsultation');
        comp.compareConsultation(entity, entity2);
        expect(consultationService.compareConsultation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
