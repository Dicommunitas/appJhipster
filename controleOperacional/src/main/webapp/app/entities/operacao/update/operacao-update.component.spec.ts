import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperacaoService } from '../service/operacao.service';
import { IOperacao, Operacao } from '../operacao.model';
import { IProduto } from 'app/entities/produto/produto.model';
import { ProdutoService } from 'app/entities/produto/service/produto.service';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';
import { TipoOperacaoService } from 'app/entities/tipo-operacao/service/tipo-operacao.service';

import { OperacaoUpdateComponent } from './operacao-update.component';

describe('Operacao Management Update Component', () => {
  let comp: OperacaoUpdateComponent;
  let fixture: ComponentFixture<OperacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operacaoService: OperacaoService;
  let produtoService: ProdutoService;
  let tipoOperacaoService: TipoOperacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperacaoUpdateComponent],
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
      .overrideTemplate(OperacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operacaoService = TestBed.inject(OperacaoService);
    produtoService = TestBed.inject(ProdutoService);
    tipoOperacaoService = TestBed.inject(TipoOperacaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Produto query and add missing value', () => {
      const operacao: IOperacao = { id: 456 };
      const produto: IProduto = { id: 65765 };
      operacao.produto = produto;

      const produtoCollection: IProduto[] = [{ id: 75519 }];
      jest.spyOn(produtoService, 'query').mockReturnValue(of(new HttpResponse({ body: produtoCollection })));
      const additionalProdutos = [produto];
      const expectedCollection: IProduto[] = [...additionalProdutos, ...produtoCollection];
      jest.spyOn(produtoService, 'addProdutoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operacao });
      comp.ngOnInit();

      expect(produtoService.query).toHaveBeenCalled();
      expect(produtoService.addProdutoToCollectionIfMissing).toHaveBeenCalledWith(produtoCollection, ...additionalProdutos);
      expect(comp.produtosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoOperacao query and add missing value', () => {
      const operacao: IOperacao = { id: 456 };
      const tipoOperacao: ITipoOperacao = { id: 65365 };
      operacao.tipoOperacao = tipoOperacao;

      const tipoOperacaoCollection: ITipoOperacao[] = [{ id: 83809 }];
      jest.spyOn(tipoOperacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoOperacaoCollection })));
      const additionalTipoOperacaos = [tipoOperacao];
      const expectedCollection: ITipoOperacao[] = [...additionalTipoOperacaos, ...tipoOperacaoCollection];
      jest.spyOn(tipoOperacaoService, 'addTipoOperacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operacao });
      comp.ngOnInit();

      expect(tipoOperacaoService.query).toHaveBeenCalled();
      expect(tipoOperacaoService.addTipoOperacaoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoOperacaoCollection,
        ...additionalTipoOperacaos
      );
      expect(comp.tipoOperacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const operacao: IOperacao = { id: 456 };
      const produto: IProduto = { id: 28462 };
      operacao.produto = produto;
      const tipoOperacao: ITipoOperacao = { id: 3154 };
      operacao.tipoOperacao = tipoOperacao;

      activatedRoute.data = of({ operacao });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(operacao));
      expect(comp.produtosSharedCollection).toContain(produto);
      expect(comp.tipoOperacaosSharedCollection).toContain(tipoOperacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operacao>>();
      const operacao = { id: 123 };
      jest.spyOn(operacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operacao }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(operacaoService.update).toHaveBeenCalledWith(operacao);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operacao>>();
      const operacao = new Operacao();
      jest.spyOn(operacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operacao }));
      saveSubject.complete();

      // THEN
      expect(operacaoService.create).toHaveBeenCalledWith(operacao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operacao>>();
      const operacao = { id: 123 };
      jest.spyOn(operacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operacaoService.update).toHaveBeenCalledWith(operacao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProdutoById', () => {
      it('Should return tracked Produto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProdutoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTipoOperacaoById', () => {
      it('Should return tracked TipoOperacao primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoOperacaoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
