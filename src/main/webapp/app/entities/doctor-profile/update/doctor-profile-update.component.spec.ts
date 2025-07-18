import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { DoctorProfileService } from '../service/doctor-profile.service';
import { IDoctorProfile } from '../doctor-profile.model';
import { DoctorProfileFormService } from './doctor-profile-form.service';

import { DoctorProfileUpdateComponent } from './doctor-profile-update.component';

describe('DoctorProfile Management Update Component', () => {
  let comp: DoctorProfileUpdateComponent;
  let fixture: ComponentFixture<DoctorProfileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let doctorProfileFormService: DoctorProfileFormService;
  let doctorProfileService: DoctorProfileService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DoctorProfileUpdateComponent],
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
      .overrideTemplate(DoctorProfileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DoctorProfileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    doctorProfileFormService = TestBed.inject(DoctorProfileFormService);
    doctorProfileService = TestBed.inject(DoctorProfileService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const doctorProfile: IDoctorProfile = { id: 456 };
      const user: IUser = { id: 31584 };
      doctorProfile.user = user;

      const userCollection: IUser[] = [{ id: 16284 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ doctorProfile });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const doctorProfile: IDoctorProfile = { id: 456 };
      const user: IUser = { id: 4769 };
      doctorProfile.user = user;

      activatedRoute.data = of({ doctorProfile });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.doctorProfile).toEqual(doctorProfile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDoctorProfile>>();
      const doctorProfile = { id: 123 };
      jest.spyOn(doctorProfileFormService, 'getDoctorProfile').mockReturnValue(doctorProfile);
      jest.spyOn(doctorProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doctorProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: doctorProfile }));
      saveSubject.complete();

      // THEN
      expect(doctorProfileFormService.getDoctorProfile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(doctorProfileService.update).toHaveBeenCalledWith(expect.objectContaining(doctorProfile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDoctorProfile>>();
      const doctorProfile = { id: 123 };
      jest.spyOn(doctorProfileFormService, 'getDoctorProfile').mockReturnValue({ id: null });
      jest.spyOn(doctorProfileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doctorProfile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: doctorProfile }));
      saveSubject.complete();

      // THEN
      expect(doctorProfileFormService.getDoctorProfile).toHaveBeenCalled();
      expect(doctorProfileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDoctorProfile>>();
      const doctorProfile = { id: 123 };
      jest.spyOn(doctorProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doctorProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(doctorProfileService.update).toHaveBeenCalled();
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
