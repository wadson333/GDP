import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPrescription } from 'app/entities/prescription/prescription.model';
import { PrescriptionService } from 'app/entities/prescription/service/prescription.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { IConsultation } from '../consultation.model';
import { ConsultationService } from '../service/consultation.service';
import { ConsultationFormService } from './consultation-form.service';

import { ConsultationUpdateComponent } from './consultation-update.component';

describe('Consultation Management Update Component', () => {
  let comp: ConsultationUpdateComponent;
  let fixture: ComponentFixture<ConsultationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consultationFormService: ConsultationFormService;
  let consultationService: ConsultationService;
  let prescriptionService: PrescriptionService;
  let userService: UserService;
  let patientService: PatientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ConsultationUpdateComponent],
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
      .overrideTemplate(ConsultationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsultationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consultationFormService = TestBed.inject(ConsultationFormService);
    consultationService = TestBed.inject(ConsultationService);
    prescriptionService = TestBed.inject(PrescriptionService);
    userService = TestBed.inject(UserService);
    patientService = TestBed.inject(PatientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call prescription query and add missing value', () => {
      const consultation: IConsultation = { id: 456 };
      const prescription: IPrescription = { id: 15703 };
      consultation.prescription = prescription;

      const prescriptionCollection: IPrescription[] = [{ id: 31647 }];
      jest.spyOn(prescriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: prescriptionCollection })));
      const expectedCollection: IPrescription[] = [prescription, ...prescriptionCollection];
      jest.spyOn(prescriptionService, 'addPrescriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consultation });
      comp.ngOnInit();

      expect(prescriptionService.query).toHaveBeenCalled();
      expect(prescriptionService.addPrescriptionToCollectionIfMissing).toHaveBeenCalledWith(prescriptionCollection, prescription);
      expect(comp.prescriptionsCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const consultation: IConsultation = { id: 456 };
      const doctor: IUser = { id: 20416 };
      consultation.doctor = doctor;

      const userCollection: IUser[] = [{ id: 23675 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [doctor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consultation });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Patient query and add missing value', () => {
      const consultation: IConsultation = { id: 456 };
      const patient: IPatient = { id: 18207 };
      consultation.patient = patient;

      const patientCollection: IPatient[] = [{ id: 13015 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consultation });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const consultation: IConsultation = { id: 456 };
      const prescription: IPrescription = { id: 11151 };
      consultation.prescription = prescription;
      const doctor: IUser = { id: 29990 };
      consultation.doctor = doctor;
      const patient: IPatient = { id: 26957 };
      consultation.patient = patient;

      activatedRoute.data = of({ consultation });
      comp.ngOnInit();

      expect(comp.prescriptionsCollection).toContain(prescription);
      expect(comp.usersSharedCollection).toContain(doctor);
      expect(comp.patientsSharedCollection).toContain(patient);
      expect(comp.consultation).toEqual(consultation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultation>>();
      const consultation = { id: 123 };
      jest.spyOn(consultationFormService, 'getConsultation').mockReturnValue(consultation);
      jest.spyOn(consultationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consultation }));
      saveSubject.complete();

      // THEN
      expect(consultationFormService.getConsultation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consultationService.update).toHaveBeenCalledWith(expect.objectContaining(consultation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultation>>();
      const consultation = { id: 123 };
      jest.spyOn(consultationFormService, 'getConsultation').mockReturnValue({ id: null });
      jest.spyOn(consultationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consultation }));
      saveSubject.complete();

      // THEN
      expect(consultationFormService.getConsultation).toHaveBeenCalled();
      expect(consultationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsultation>>();
      const consultation = { id: 123 };
      jest.spyOn(consultationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consultation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consultationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePrescription', () => {
      it('Should forward to prescriptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(prescriptionService, 'comparePrescription');
        comp.comparePrescription(entity, entity2);
        expect(prescriptionService.comparePrescription).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
