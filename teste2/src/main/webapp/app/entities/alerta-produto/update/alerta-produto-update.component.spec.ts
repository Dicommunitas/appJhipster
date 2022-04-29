import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AlertaProdutoService } from '../service/alerta-produto.service';
import { IAlertaProduto, AlertaProduto } from '../alerta-produto.model';

import { AlertaProdutoUpdateComponent } from './alerta-produto-update.component';

describe('AlertaProduto Management Update Component', () => {
  let comp: AlertaProdutoUpdateComponent;
  let fixture: ComponentFixture<AlertaProdutoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alertaProdutoService: AlertaProdutoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AlertaProdutoUpdateComponent],
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
      .overrideTemplate(AlertaProdutoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlertaProdutoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alertaProdutoService = TestBed.inject(AlertaProdutoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const alertaProduto: IAlertaProduto = { id: 456 };

      activatedRoute.data = of({ alertaProduto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(alertaProduto));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AlertaProduto>>();
      const alertaProduto = { id: 123 };
      jest.spyOn(alertaProdutoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alertaProduto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alertaProduto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(alertaProdutoService.update).toHaveBeenCalledWith(alertaProduto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AlertaProduto>>();
      const alertaProduto = new AlertaProduto();
      jest.spyOn(alertaProdutoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alertaProduto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alertaProduto }));
      saveSubject.complete();

      // THEN
      expect(alertaProdutoService.create).toHaveBeenCalledWith(alertaProduto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AlertaProduto>>();
      const alertaProduto = { id: 123 };
      jest.spyOn(alertaProdutoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alertaProduto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alertaProdutoService.update).toHaveBeenCalledWith(alertaProduto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
