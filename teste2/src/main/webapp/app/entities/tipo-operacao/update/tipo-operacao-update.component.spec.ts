import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoOperacaoService } from '../service/tipo-operacao.service';
import { ITipoOperacao, TipoOperacao } from '../tipo-operacao.model';

import { TipoOperacaoUpdateComponent } from './tipo-operacao-update.component';

describe('TipoOperacao Management Update Component', () => {
  let comp: TipoOperacaoUpdateComponent;
  let fixture: ComponentFixture<TipoOperacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoOperacaoService: TipoOperacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoOperacaoUpdateComponent],
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
      .overrideTemplate(TipoOperacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoOperacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoOperacaoService = TestBed.inject(TipoOperacaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoOperacao: ITipoOperacao = { id: 456 };

      activatedRoute.data = of({ tipoOperacao });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoOperacao));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoOperacao>>();
      const tipoOperacao = { id: 123 };
      jest.spyOn(tipoOperacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoOperacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoOperacao }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoOperacaoService.update).toHaveBeenCalledWith(tipoOperacao);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoOperacao>>();
      const tipoOperacao = new TipoOperacao();
      jest.spyOn(tipoOperacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoOperacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoOperacao }));
      saveSubject.complete();

      // THEN
      expect(tipoOperacaoService.create).toHaveBeenCalledWith(tipoOperacao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoOperacao>>();
      const tipoOperacao = { id: 123 };
      jest.spyOn(tipoOperacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoOperacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoOperacaoService.update).toHaveBeenCalledWith(tipoOperacao);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
