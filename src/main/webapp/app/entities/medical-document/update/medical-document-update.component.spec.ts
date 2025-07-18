import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { MedicalDocumentService } from '../service/medical-document.service';
import { IMedicalDocument } from '../medical-document.model';
import { MedicalDocumentFormService } from './medical-document-form.service';

import { MedicalDocumentUpdateComponent } from './medical-document-update.component';

describe('MedicalDocument Management Update Component', () => {
  let comp: MedicalDocumentUpdateComponent;
  let fixture: ComponentFixture<MedicalDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicalDocumentFormService: MedicalDocumentFormService;
  let medicalDocumentService: MedicalDocumentService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MedicalDocumentUpdateComponent],
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
      .overrideTemplate(MedicalDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicalDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicalDocumentFormService = TestBed.inject(MedicalDocumentFormService);
    medicalDocumentService = TestBed.inject(MedicalDocumentService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Patient query and add missing value', () => {
      const medicalDocument: IMedicalDocument = { id: 456 };
      const patient: IPatient = { id: 30386 };
      medicalDocument.patient = patient;

      const patientCollection: IPatient[] = [{ id: 10874 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicalDocument: IMedicalDocument = { id: 456 };
      const patient: IPatient = { id: 6289 };
      medicalDocument.patient = patient;

      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContain(patient);
      expect(comp.medicalDocument).toEqual(medicalDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalDocument>>();
      const medicalDocument = { id: 123 };
      jest.spyOn(medicalDocumentFormService, 'getMedicalDocument').mockReturnValue(medicalDocument);
      jest.spyOn(medicalDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalDocument }));
      saveSubject.complete();

      // THEN
      expect(medicalDocumentFormService.getMedicalDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicalDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(medicalDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalDocument>>();
      const medicalDocument = { id: 123 };
      jest.spyOn(medicalDocumentFormService, 'getMedicalDocument').mockReturnValue({ id: null });
      jest.spyOn(medicalDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicalDocument }));
      saveSubject.complete();

      // THEN
      expect(medicalDocumentFormService.getMedicalDocument).toHaveBeenCalled();
      expect(medicalDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicalDocument>>();
      const medicalDocument = { id: 123 };
      jest.spyOn(medicalDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicalDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicalDocumentService.update).toHaveBeenCalled();
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
  });
});
