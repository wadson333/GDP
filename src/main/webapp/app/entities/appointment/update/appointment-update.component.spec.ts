import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IAppointment } from '../appointment.model';
import { AppointmentService } from '../service/appointment.service';
import { AppointmentFormService } from './appointment-form.service';

import { AppointmentUpdateComponent } from './appointment-update.component';

describe('Appointment Management Update Component', () => {
  let comp: AppointmentUpdateComponent;
  let fixture: ComponentFixture<AppointmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appointmentFormService: AppointmentFormService;
  let appointmentService: AppointmentService;
  let patientService: PatientService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AppointmentUpdateComponent],
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
      .overrideTemplate(AppointmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppointmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appointmentFormService = TestBed.inject(AppointmentFormService);
    appointmentService = TestBed.inject(AppointmentService);
    patientService = TestBed.inject(PatientService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Patient query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const patient: IPatient = { id: 11627 };
      appointment.patient = patient;

      const patientCollection: IPatient[] = [{ id: 31152 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const doctor: IUser = { id: 11109 };
      appointment.doctor = doctor;

      const userCollection: IUser[] = [{ id: 28022 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [doctor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appointment: IAppointment = { id: 456 };
      const patient: IPatient = { id: 6871 };
      appointment.patient = patient;
      const doctor: IUser = { id: 12565 };
      appointment.doctor = doctor;

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContain(patient);
      expect(comp.usersSharedCollection).toContain(doctor);
      expect(comp.appointment).toEqual(appointment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentFormService, 'getAppointment').mockReturnValue(appointment);
      jest.spyOn(appointmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointment }));
      saveSubject.complete();

      // THEN
      expect(appointmentFormService.getAppointment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appointmentService.update).toHaveBeenCalledWith(expect.objectContaining(appointment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentFormService, 'getAppointment').mockReturnValue({ id: null });
      jest.spyOn(appointmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointment }));
      saveSubject.complete();

      // THEN
      expect(appointmentFormService.getAppointment).toHaveBeenCalled();
      expect(appointmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appointmentService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
