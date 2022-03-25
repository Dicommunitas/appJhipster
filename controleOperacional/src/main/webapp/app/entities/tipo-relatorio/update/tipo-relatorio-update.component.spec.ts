import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoRelatorioService } from '../service/tipo-relatorio.service';
import { ITipoRelatorio, TipoRelatorio } from '../tipo-relatorio.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TipoRelatorioUpdateComponent } from './tipo-relatorio-update.component';

describe('TipoRelatorio Management Update Component', () => {
  let comp: TipoRelatorioUpdateComponent;
  let fixture: ComponentFixture<TipoRelatorioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoRelatorioService: TipoRelatorioService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoRelatorioUpdateComponent],
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
      .overrideTemplate(TipoRelatorioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoRelatorioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoRelatorioService = TestBed.inject(TipoRelatorioService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const tipoRelatorio: ITipoRelatorio = { id: 456 };
      const usuariosAuts: IUser[] = [{ id: 66685 }];
      tipoRelatorio.usuariosAuts = usuariosAuts;

      const userCollection: IUser[] = [{ id: 20681 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [...usuariosAuts];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tipoRelatorio });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tipoRelatorio: ITipoRelatorio = { id: 456 };
      const usuariosAuts: IUser = { id: 15054 };
      tipoRelatorio.usuariosAuts = [usuariosAuts];

      activatedRoute.data = of({ tipoRelatorio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoRelatorio));
      expect(comp.usersSharedCollection).toContain(usuariosAuts);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoRelatorio>>();
      const tipoRelatorio = { id: 123 };
      jest.spyOn(tipoRelatorioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoRelatorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoRelatorio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoRelatorioService.update).toHaveBeenCalledWith(tipoRelatorio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoRelatorio>>();
      const tipoRelatorio = new TipoRelatorio();
      jest.spyOn(tipoRelatorioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoRelatorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoRelatorio }));
      saveSubject.complete();

      // THEN
      expect(tipoRelatorioService.create).toHaveBeenCalledWith(tipoRelatorio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoRelatorio>>();
      const tipoRelatorio = { id: 123 };
      jest.spyOn(tipoRelatorioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoRelatorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoRelatorioService.update).toHaveBeenCalledWith(tipoRelatorio);
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
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedUser', () => {
      it('Should return option if no User is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUser(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected User for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUser(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this User is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUser(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
