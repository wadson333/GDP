import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IHospitalization } from '../hospitalization.model';
import { HospitalizationService } from '../service/hospitalization.service';
import { HospitalizationFormService } from './hospitalization-form.service';

import { HospitalizationUpdateComponent } from './hospitalization-update.component';

describe('Hospitalization Management Update Component', () => {
  let comp: HospitalizationUpdateComponent;
  let fixture: ComponentFixture<HospitalizationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hospitalizationFormService: HospitalizationFormService;
  let hospitalizationService: HospitalizationService;
  let patientService: PatientService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HospitalizationUpdateComponent],
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
      .overrideTemplate(HospitalizationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HospitalizationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hospitalizationFormService = TestBed.inject(HospitalizationFormService);
    hospitalizationService = TestBed.inject(HospitalizationService);
    patientService = TestBed.inject(PatientService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Patient query and add missing value', () => {
      const hospitalization: IHospitalization = { id: 456 };
      const patient: IPatient = { id: 32126 };
      hospitalization.patient = patient;

      const patientCollection: IPatient[] = [{ id: 21838 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hospitalization });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining),
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const hospitalization: IHospitalization = { id: 456 };
      const attendingDoctor: IUser = { id: 13526 };
      hospitalization.attendingDoctor = attendingDoctor;

      const userCollection: IUser[] = [{ id: 8637 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [attendingDoctor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hospitalization });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hospitalization: IHospitalization = { id: 456 };
      const patient: IPatient = { id: 18117 };
      hospitalization.patient = patient;
      const attendingDoctor: IUser = { id: 27407 };
      hospitalization.attendingDoctor = attendingDoctor;

      activatedRoute.data = of({ hospitalization });
      comp.ngOnInit();

      expect(comp.patientsSharedCollection).toContain(patient);
      expect(comp.usersSharedCollection).toContain(attendingDoctor);
      expect(comp.hospitalization).toEqual(hospitalization);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHospitalization>>();
      const hospitalization = { id: 123 };
      jest.spyOn(hospitalizationFormService, 'getHospitalization').mockReturnValue(hospitalization);
      jest.spyOn(hospitalizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hospitalization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hospitalization }));
      saveSubject.complete();

      // THEN
      expect(hospitalizationFormService.getHospitalization).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(hospitalizationService.update).toHaveBeenCalledWith(expect.objectContaining(hospitalization));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHospitalization>>();
      const hospitalization = { id: 123 };
      jest.spyOn(hospitalizationFormService, 'getHospitalization').mockReturnValue({ id: null });
      jest.spyOn(hospitalizationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hospitalization: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hospitalization }));
      saveSubject.complete();

      // THEN
      expect(hospitalizationFormService.getHospitalization).toHaveBeenCalled();
      expect(hospitalizationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHospitalization>>();
      const hospitalization = { id: 123 };
      jest.spyOn(hospitalizationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hospitalization });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hospitalizationService.update).toHaveBeenCalled();
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
