jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LembreteService } from '../service/lembrete.service';
import { ILembrete, Lembrete } from '../lembrete.model';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { TipoRelatorioService } from 'app/entities/tipo-relatorio/service/tipo-relatorio.service';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';
import { TipoOperacaoService } from 'app/entities/tipo-operacao/service/tipo-operacao.service';

import { LembreteUpdateComponent } from './lembrete-update.component';

describe('Lembrete Management Update Component', () => {
  let comp: LembreteUpdateComponent;
  let fixture: ComponentFixture<LembreteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let lembreteService: LembreteService;
  let tipoRelatorioService: TipoRelatorioService;
  let tipoOperacaoService: TipoOperacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LembreteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LembreteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LembreteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    lembreteService = TestBed.inject(LembreteService);
    tipoRelatorioService = TestBed.inject(TipoRelatorioService);
    tipoOperacaoService = TestBed.inject(TipoOperacaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TipoRelatorio query and add missing value', () => {
      const lembrete: ILembrete = { id: 456 };
      const tipoRelatorio: ITipoRelatorio = { id: 9741 };
      lembrete.tipoRelatorio = tipoRelatorio;

      const tipoRelatorioCollection: ITipoRelatorio[] = [{ id: 46623 }];
      jest.spyOn(tipoRelatorioService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoRelatorioCollection })));
      const additionalTipoRelatorios = [tipoRelatorio];
      const expectedCollection: ITipoRelatorio[] = [...additionalTipoRelatorios, ...tipoRelatorioCollection];
      jest.spyOn(tipoRelatorioService, 'addTipoRelatorioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lembrete });
      comp.ngOnInit();

      expect(tipoRelatorioService.query).toHaveBeenCalled();
      expect(tipoRelatorioService.addTipoRelatorioToCollectionIfMissing).toHaveBeenCalledWith(
        tipoRelatorioCollection,
        ...additionalTipoRelatorios
      );
      expect(comp.tipoRelatoriosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoOperacao query and add missing value', () => {
      const lembrete: ILembrete = { id: 456 };
      const tipoOperacao: ITipoOperacao = { id: 96957 };
      lembrete.tipoOperacao = tipoOperacao;

      const tipoOperacaoCollection: ITipoOperacao[] = [{ id: 78119 }];
      jest.spyOn(tipoOperacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoOperacaoCollection })));
      const additionalTipoOperacaos = [tipoOperacao];
      const expectedCollection: ITipoOperacao[] = [...additionalTipoOperacaos, ...tipoOperacaoCollection];
      jest.spyOn(tipoOperacaoService, 'addTipoOperacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lembrete });
      comp.ngOnInit();

      expect(tipoOperacaoService.query).toHaveBeenCalled();
      expect(tipoOperacaoService.addTipoOperacaoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoOperacaoCollection,
        ...additionalTipoOperacaos
      );
      expect(comp.tipoOperacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lembrete: ILembrete = { id: 456 };
      const tipoRelatorio: ITipoRelatorio = { id: 25620 };
      lembrete.tipoRelatorio = tipoRelatorio;
      const tipoOperacao: ITipoOperacao = { id: 28307 };
      lembrete.tipoOperacao = tipoOperacao;

      activatedRoute.data = of({ lembrete });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(lembrete));
      expect(comp.tipoRelatoriosSharedCollection).toContain(tipoRelatorio);
      expect(comp.tipoOperacaosSharedCollection).toContain(tipoOperacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lembrete>>();
      const lembrete = { id: 123 };
      jest.spyOn(lembreteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lembrete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lembrete }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(lembreteService.update).toHaveBeenCalledWith(lembrete);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lembrete>>();
      const lembrete = new Lembrete();
      jest.spyOn(lembreteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lembrete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lembrete }));
      saveSubject.complete();

      // THEN
      expect(lembreteService.create).toHaveBeenCalledWith(lembrete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Lembrete>>();
      const lembrete = { id: 123 };
      jest.spyOn(lembreteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lembrete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(lembreteService.update).toHaveBeenCalledWith(lembrete);
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

    describe('trackTipoOperacaoById', () => {
      it('Should return tracked TipoOperacao primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoOperacaoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
