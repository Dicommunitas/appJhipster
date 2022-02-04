jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoRelatorioService } from '../service/tipo-relatorio.service';
import { ITipoRelatorio, TipoRelatorio } from '../tipo-relatorio.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

import { TipoRelatorioUpdateComponent } from './tipo-relatorio-update.component';

describe('TipoRelatorio Management Update Component', () => {
  let comp: TipoRelatorioUpdateComponent;
  let fixture: ComponentFixture<TipoRelatorioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoRelatorioService: TipoRelatorioService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoRelatorioUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TipoRelatorioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoRelatorioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoRelatorioService = TestBed.inject(TipoRelatorioService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const tipoRelatorio: ITipoRelatorio = { id: 456 };
      const usuariosAuts: IUsuario[] = [{ id: 6801 }];
      tipoRelatorio.usuariosAuts = usuariosAuts;

      const usuarioCollection: IUsuario[] = [{ id: 2769 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [...usuariosAuts];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tipoRelatorio });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tipoRelatorio: ITipoRelatorio = { id: 456 };
      const usuariosAuts: IUsuario = { id: 62053 };
      tipoRelatorio.usuariosAuts = [usuariosAuts];

      activatedRoute.data = of({ tipoRelatorio });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoRelatorio));
      expect(comp.usuariosSharedCollection).toContain(usuariosAuts);
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
    describe('trackUsuarioById', () => {
      it('Should return tracked Usuario primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUsuarioById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedUsuario', () => {
      it('Should return option if no Usuario is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUsuario(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Usuario for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUsuario(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Usuario is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUsuario(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
