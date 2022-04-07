import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AmostraService } from '../service/amostra.service';
import { IAmostra, Amostra } from '../amostra.model';
import { IOperacao } from 'app/entities/operacao/operacao.model';
import { OperacaoService } from 'app/entities/operacao/service/operacao.service';
import { IOrigemAmostra } from 'app/entities/origem-amostra/origem-amostra.model';
import { OrigemAmostraService } from 'app/entities/origem-amostra/service/origem-amostra.service';
import { ITipoAmostra } from 'app/entities/tipo-amostra/tipo-amostra.model';
import { TipoAmostraService } from 'app/entities/tipo-amostra/service/tipo-amostra.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { AmostraUpdateComponent } from './amostra-update.component';

describe('Amostra Management Update Component', () => {
  let comp: AmostraUpdateComponent;
  let fixture: ComponentFixture<AmostraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amostraService: AmostraService;
  let operacaoService: OperacaoService;
  let origemAmostraService: OrigemAmostraService;
  let tipoAmostraService: TipoAmostraService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AmostraUpdateComponent],
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
      .overrideTemplate(AmostraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmostraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amostraService = TestBed.inject(AmostraService);
    operacaoService = TestBed.inject(OperacaoService);
    origemAmostraService = TestBed.inject(OrigemAmostraService);
    tipoAmostraService = TestBed.inject(TipoAmostraService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Operacao query and add missing value', () => {
      const amostra: IAmostra = { id: 456 };
      const operacao: IOperacao = { id: 45185 };
      amostra.operacao = operacao;

      const operacaoCollection: IOperacao[] = [{ id: 77772 }];
      jest.spyOn(operacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: operacaoCollection })));
      const additionalOperacaos = [operacao];
      const expectedCollection: IOperacao[] = [...additionalOperacaos, ...operacaoCollection];
      jest.spyOn(operacaoService, 'addOperacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      expect(operacaoService.query).toHaveBeenCalled();
      expect(operacaoService.addOperacaoToCollectionIfMissing).toHaveBeenCalledWith(operacaoCollection, ...additionalOperacaos);
      expect(comp.operacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrigemAmostra query and add missing value', () => {
      const amostra: IAmostra = { id: 456 };
      const origemAmostra: IOrigemAmostra = { id: 78160 };
      amostra.origemAmostra = origemAmostra;

      const origemAmostraCollection: IOrigemAmostra[] = [{ id: 48497 }];
      jest.spyOn(origemAmostraService, 'query').mockReturnValue(of(new HttpResponse({ body: origemAmostraCollection })));
      const additionalOrigemAmostras = [origemAmostra];
      const expectedCollection: IOrigemAmostra[] = [...additionalOrigemAmostras, ...origemAmostraCollection];
      jest.spyOn(origemAmostraService, 'addOrigemAmostraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      expect(origemAmostraService.query).toHaveBeenCalled();
      expect(origemAmostraService.addOrigemAmostraToCollectionIfMissing).toHaveBeenCalledWith(
        origemAmostraCollection,
        ...additionalOrigemAmostras
      );
      expect(comp.origemAmostrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoAmostra query and add missing value', () => {
      const amostra: IAmostra = { id: 456 };
      const tipoAmostra: ITipoAmostra = { id: 91619 };
      amostra.tipoAmostra = tipoAmostra;

      const tipoAmostraCollection: ITipoAmostra[] = [{ id: 40957 }];
      jest.spyOn(tipoAmostraService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoAmostraCollection })));
      const additionalTipoAmostras = [tipoAmostra];
      const expectedCollection: ITipoAmostra[] = [...additionalTipoAmostras, ...tipoAmostraCollection];
      jest.spyOn(tipoAmostraService, 'addTipoAmostraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      expect(tipoAmostraService.query).toHaveBeenCalled();
      expect(tipoAmostraService.addTipoAmostraToCollectionIfMissing).toHaveBeenCalledWith(tipoAmostraCollection, ...additionalTipoAmostras);
      expect(comp.tipoAmostrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const amostra: IAmostra = { id: 456 };
      const amostradaPor: IUser = { id: 31101 };
      amostra.amostradaPor = amostradaPor;
      const recebidaPor: IUser = { id: 1393 };
      amostra.recebidaPor = recebidaPor;

      const userCollection: IUser[] = [{ id: 60877 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [amostradaPor, recebidaPor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const amostra: IAmostra = { id: 456 };
      const operacao: IOperacao = { id: 73150 };
      amostra.operacao = operacao;
      const origemAmostra: IOrigemAmostra = { id: 25637 };
      amostra.origemAmostra = origemAmostra;
      const tipoAmostra: ITipoAmostra = { id: 30560 };
      amostra.tipoAmostra = tipoAmostra;
      const amostradaPor: IUser = { id: 53678 };
      amostra.amostradaPor = amostradaPor;
      const recebidaPor: IUser = { id: 57651 };
      amostra.recebidaPor = recebidaPor;

      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(amostra));
      expect(comp.operacaosSharedCollection).toContain(operacao);
      expect(comp.origemAmostrasSharedCollection).toContain(origemAmostra);
      expect(comp.tipoAmostrasSharedCollection).toContain(tipoAmostra);
      expect(comp.usersSharedCollection).toContain(amostradaPor);
      expect(comp.usersSharedCollection).toContain(recebidaPor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Amostra>>();
      const amostra = { id: 123 };
      jest.spyOn(amostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amostra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amostraService.update).toHaveBeenCalledWith(amostra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Amostra>>();
      const amostra = new Amostra();
      jest.spyOn(amostraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amostra }));
      saveSubject.complete();

      // THEN
      expect(amostraService.create).toHaveBeenCalledWith(amostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Amostra>>();
      const amostra = { id: 123 };
      jest.spyOn(amostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amostraService.update).toHaveBeenCalledWith(amostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOperacaoById', () => {
      it('Should return tracked Operacao primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOperacaoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOrigemAmostraById', () => {
      it('Should return tracked OrigemAmostra primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrigemAmostraById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTipoAmostraById', () => {
      it('Should return tracked TipoAmostra primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoAmostraById(0, entity);
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
