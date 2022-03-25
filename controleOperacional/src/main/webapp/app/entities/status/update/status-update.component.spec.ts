import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StatusService } from '../service/status.service';
import { IStatus, Status } from '../status.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProblema } from 'app/entities/problema/problema.model';
import { ProblemaService } from 'app/entities/problema/service/problema.service';

import { StatusUpdateComponent } from './status-update.component';

describe('Status Management Update Component', () => {
  let comp: StatusUpdateComponent;
  let fixture: ComponentFixture<StatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let statusService: StatusService;
  let userService: UserService;
  let problemaService: ProblemaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StatusUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(StatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusService = TestBed.inject(StatusService);
    userService = TestBed.inject(UserService);
    problemaService = TestBed.inject(ProblemaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const status: IStatus = { id: 456 };
      const relator: IUser = { id: 70313 };
      status.relator = relator;
      const responsavel: IUser = { id: 72760 };
      status.responsavel = responsavel;

      const userCollection: IUser[] = [{ id: 47916 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [relator, responsavel];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Problema query and add missing value', () => {
      const status: IStatus = { id: 456 };
      const problema: IProblema = { id: 34537 };
      status.problema = problema;

      const problemaCollection: IProblema[] = [{ id: 6610 }];
      jest.spyOn(problemaService, 'query').mockReturnValue(of(new HttpResponse({ body: problemaCollection })));
      const additionalProblemas = [problema];
      const expectedCollection: IProblema[] = [...additionalProblemas, ...problemaCollection];
      jest.spyOn(problemaService, 'addProblemaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(problemaService.query).toHaveBeenCalled();
      expect(problemaService.addProblemaToCollectionIfMissing).toHaveBeenCalledWith(problemaCollection, ...additionalProblemas);
      expect(comp.problemasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const status: IStatus = { id: 456 };
      const relator: IUser = { id: 99168 };
      status.relator = relator;
      const responsavel: IUser = { id: 98478 };
      status.responsavel = responsavel;
      const problema: IProblema = { id: 69516 };
      status.problema = problema;

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(status));
      expect(comp.usersSharedCollection).toContain(relator);
      expect(comp.usersSharedCollection).toContain(responsavel);
      expect(comp.problemasSharedCollection).toContain(problema);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Status>>();
      const status = { id: 123 };
      jest.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: status }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusService.update).toHaveBeenCalledWith(status);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Status>>();
      const status = new Status();
      jest.spyOn(statusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: status }));
      saveSubject.complete();

      // THEN
      expect(statusService.create).toHaveBeenCalledWith(status);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Status>>();
      const status = { id: 123 };
      jest.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusService.update).toHaveBeenCalledWith(status);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProblemaById', () => {
      it('Should return tracked Problema primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProblemaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
