import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProdutoService } from '../service/produto.service';
import { IProduto, Produto } from '../produto.model';
import { IAlertaProduto } from 'app/entities/alerta-produto/alerta-produto.model';
import { AlertaProdutoService } from 'app/entities/alerta-produto/service/alerta-produto.service';

import { ProdutoUpdateComponent } from './produto-update.component';

describe('Produto Management Update Component', () => {
  let comp: ProdutoUpdateComponent;
  let fixture: ComponentFixture<ProdutoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let produtoService: ProdutoService;
  let alertaProdutoService: AlertaProdutoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProdutoUpdateComponent],
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
      .overrideTemplate(ProdutoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProdutoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    produtoService = TestBed.inject(ProdutoService);
    alertaProdutoService = TestBed.inject(AlertaProdutoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AlertaProduto query and add missing value', () => {
      const produto: IProduto = { id: 456 };
      const alertas: IAlertaProduto[] = [{ id: 78368 }];
      produto.alertas = alertas;

      const alertaProdutoCollection: IAlertaProduto[] = [{ id: 18796 }];
      jest.spyOn(alertaProdutoService, 'query').mockReturnValue(of(new HttpResponse({ body: alertaProdutoCollection })));
      const additionalAlertaProdutos = [...alertas];
      const expectedCollection: IAlertaProduto[] = [...additionalAlertaProdutos, ...alertaProdutoCollection];
      jest.spyOn(alertaProdutoService, 'addAlertaProdutoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      expect(alertaProdutoService.query).toHaveBeenCalled();
      expect(alertaProdutoService.addAlertaProdutoToCollectionIfMissing).toHaveBeenCalledWith(
        alertaProdutoCollection,
        ...additionalAlertaProdutos
      );
      expect(comp.alertaProdutosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const produto: IProduto = { id: 456 };
      const alertas: IAlertaProduto = { id: 45871 };
      produto.alertas = [alertas];

      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(produto));
      expect(comp.alertaProdutosSharedCollection).toContain(alertas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produto>>();
      const produto = { id: 123 };
      jest.spyOn(produtoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(produtoService.update).toHaveBeenCalledWith(produto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produto>>();
      const produto = new Produto();
      jest.spyOn(produtoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produto }));
      saveSubject.complete();

      // THEN
      expect(produtoService.create).toHaveBeenCalledWith(produto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produto>>();
      const produto = { id: 123 };
      jest.spyOn(produtoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(produtoService.update).toHaveBeenCalledWith(produto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAlertaProdutoById', () => {
      it('Should return tracked AlertaProduto primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAlertaProdutoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedAlertaProduto', () => {
      it('Should return option if no AlertaProduto is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedAlertaProduto(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected AlertaProduto for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedAlertaProduto(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this AlertaProduto is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedAlertaProduto(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
