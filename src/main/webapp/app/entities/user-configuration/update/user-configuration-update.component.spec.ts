import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { UserConfigurationService } from '../service/user-configuration.service';
import { IUserConfiguration } from '../user-configuration.model';
import { UserConfigurationFormService } from './user-configuration-form.service';

import { UserConfigurationUpdateComponent } from './user-configuration-update.component';

describe('UserConfiguration Management Update Component', () => {
  let comp: UserConfigurationUpdateComponent;
  let fixture: ComponentFixture<UserConfigurationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userConfigurationFormService: UserConfigurationFormService;
  let userConfigurationService: UserConfigurationService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserConfigurationUpdateComponent],
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
      .overrideTemplate(UserConfigurationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserConfigurationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userConfigurationFormService = TestBed.inject(UserConfigurationFormService);
    userConfigurationService = TestBed.inject(UserConfigurationService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userConfiguration: IUserConfiguration = { id: 456 };
      const user: IUser = { id: 19706 };
      userConfiguration.user = user;

      const userCollection: IUser[] = [{ id: 8879 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userConfiguration });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userConfiguration: IUserConfiguration = { id: 456 };
      const user: IUser = { id: 19845 };
      userConfiguration.user = user;

      activatedRoute.data = of({ userConfiguration });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.userConfiguration).toEqual(userConfiguration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConfiguration>>();
      const userConfiguration = { id: 123 };
      jest.spyOn(userConfigurationFormService, 'getUserConfiguration').mockReturnValue(userConfiguration);
      jest.spyOn(userConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userConfiguration }));
      saveSubject.complete();

      // THEN
      expect(userConfigurationFormService.getUserConfiguration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userConfigurationService.update).toHaveBeenCalledWith(expect.objectContaining(userConfiguration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConfiguration>>();
      const userConfiguration = { id: 123 };
      jest.spyOn(userConfigurationFormService, 'getUserConfiguration').mockReturnValue({ id: null });
      jest.spyOn(userConfigurationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConfiguration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userConfiguration }));
      saveSubject.complete();

      // THEN
      expect(userConfigurationFormService.getUserConfiguration).toHaveBeenCalled();
      expect(userConfigurationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserConfiguration>>();
      const userConfiguration = { id: 123 };
      jest.spyOn(userConfigurationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userConfiguration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userConfigurationService.update).toHaveBeenCalled();
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
