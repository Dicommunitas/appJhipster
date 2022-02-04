jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RelatorioService } from '../service/relatorio.service';
import { IRelatorio, Relatorio } from '../relatorio.model';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { TipoRelatorioService } from 'app/entities/tipo-relatorio/service/tipo-relatorio.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { RelatorioUpdateComponent } from './relatorio-update.component';

describe('Relatorio Management Update Component', () => {
  let comp: RelatorioUpdateComponent;
  let fixture: ComponentFixture<RelatorioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let relatorioService: RelatorioService;
  let tipoRelatorioService: TipoRelatorioService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RelatorioUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RelatorioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RelatorioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    relatorioService = TestBed.inject(RelatorioService);
    tipoRelatorioService = TestBed.inject(TipoRelatorioService);
    usuarioService = TestBed.inject(UsuarioService);

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

    it('Should call Usuario query and add missing value', () => {
      const relatorio: IRelatorio = { id: 456 };
      const responsavel: IUsuario = { id: 96149 };
      relatorio.responsavel = responsavel;

      const usuarioCollection: IUsuario[] = [{ id: 81099 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [responsavel];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const relatorio: IRelatorio = { id: 456 };
      const tipo: ITipoRelatorio = { id: 41527 };
      relatorio.tipo = tipo;
      const responsavel: IUsuario = { id: 85140 };
      relatorio.responsavel = responsavel;

      activatedRoute.data = of({ relatorio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(relatorio));
      expect(comp.tipoRelatoriosSharedCollection).toContain(tipo);
      expect(comp.usuariosSharedCollection).toContain(responsavel);
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

    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
