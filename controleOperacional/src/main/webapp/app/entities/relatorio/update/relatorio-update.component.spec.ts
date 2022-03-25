import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RelatorioService } from '../service/relatorio.service';
import { IRelatorio, Relatorio } from '../relatorio.model';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { TipoRelatorioService } from 'app/entities/tipo-relatorio/service/tipo-relatorio.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { RelatorioUpdateComponent } from './relatorio-update.component';

describe('Relatorio Management Update Component', () => {
  let comp: RelatorioUpdateComponent;
  let fixture: ComponentFixture<RelatorioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let relatorioService: RelatorioService;
  let tipoRelatorioService: TipoRelatorioService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RelatorioUpdateComponent],
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
      .overrideTemplate(RelatorioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RelatorioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    relatorioService = TestBed.inject(RelatorioService);
    tipoRelatorioService = TestBed.inject(TipoRelatorioService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TipoRelatorio query and add missing value', () => {
      const relatorio: IRelatorio = { id: 456 };
      const tipo: ITipoRelatorio = { id: 64082 };
      relatorio.tipo = tipo;

      const tipoRelatorioCollection: ITipoRelatorio[] = [{ id: 25189 }];
      jest.spyOn(tipoRelatorioService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoRelatorioCollection })));
      const additionalTipoRelatorios = [tipo];
      const expectedCollection: ITipoRelatorio[] = [...additionalTipoRelatorios, ...tipoRelatorioCollection];
      jest.spyOn(tipoRelatorioService, 'addTipoRelatorioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      expect(tipoRelatorioService.query).toHaveBeenCalled();
      expect(tipoRelatorioService.addTipoRelatorioToCollectionIfMissing).toHaveBeenCalledWith(
        tipoRelatorioCollection,
        ...additionalTipoRelatorios
      );
      expect(comp.tipoRelatoriosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const relatorio: IRelatorio = { id: 456 };
      const responsavel: IUser = { id: 946 };
      relatorio.responsavel = responsavel;

      const userCollection: IUser[] = [{ id: 9236 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [responsavel];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const relatorio: IRelatorio = { id: 456 };
      const tipo: ITipoRelatorio = { id: 41527 };
      relatorio.tipo = tipo;
      const responsavel: IUser = { id: 42258 };
      relatorio.responsavel = responsavel;

      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(relatorio));
      expect(comp.tipoRelatoriosSharedCollection).toContain(tipo);
      expect(comp.usersSharedCollection).toContain(responsavel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Relatorio>>();
      const relatorio = { id: 123 };
      jest.spyOn(relatorioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: relatorio }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(relatorioService.update).toHaveBeenCalledWith(relatorio);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Relatorio>>();
      const relatorio = new Relatorio();
      jest.spyOn(relatorioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: relatorio }));
      saveSubject.complete();

      // THEN
      expect(relatorioService.create).toHaveBeenCalledWith(relatorio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Relatorio>>();
      const relatorio = { id: 123 };
      jest.spyOn(relatorioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(relatorioService.update).toHaveBeenCalledWith(relatorio);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTipoRelatorioById', () => {
      it('Should return tracked TipoRelatorio primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoRelatorioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
