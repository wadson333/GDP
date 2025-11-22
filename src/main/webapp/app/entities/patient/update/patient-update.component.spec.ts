import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { PatientService } from '../service/patient.service';
import { IPatient } from '../patient.model';
import { PatientFormService } from './patient-form.service';

import { PatientUpdateComponent } from './patient-update.component';

describe('Patient Management Update Component', () => {
  let comp: PatientUpdateComponent;
  let fixture: ComponentFixture<PatientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patientFormService: PatientFormService;
  let patientService: PatientService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PatientUpdateComponent],
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
      .overrideTemplate(PatientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patientFormService = TestBed.inject(PatientFormService);
    patientService = TestBed.inject(PatientService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const patient: IPatient = { id: 456 };
      const user: IUser = { id: 6365 };
      patient.user = user;

      const userCollection: IUser[] = [{ id: 25932 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const patient: IPatient = { id: 456 };
      const user: IUser = { id: 12435 };
      patient.user = user;

      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.patient).toEqual(patient);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPatient>>();
      const patient = { id: 123 };
      jest.spyOn(patientFormService, 'getPatient').mockReturnValue(patient);
      jest.spyOn(patientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patient }));
      saveSubject.complete();

      // THEN
      expect(patientFormService.getPatient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(patientService.update).toHaveBeenCalledWith(expect.objectContaining(patient));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPatient>>();
      const patient = { id: 123 };
      jest.spyOn(patientFormService, 'getPatient').mockReturnValue({ id: null });
      jest.spyOn(patientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patient }));
      saveSubject.complete();

      // THEN
      expect(patientFormService.getPatient).toHaveBeenCalled();
      expect(patientService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPatient>>();
      const patient = { id: 123 };
      jest.spyOn(patientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patientService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
