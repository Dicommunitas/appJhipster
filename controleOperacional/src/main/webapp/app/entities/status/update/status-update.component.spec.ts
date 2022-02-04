jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StatusService } from '../service/status.service';
import { IStatus, Status } from '../status.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IProblema } from 'app/entities/problema/problema.model';
import { ProblemaService } from 'app/entities/problema/service/problema.service';

import { StatusUpdateComponent } from './status-update.component';

describe('Status Management Update Component', () => {
  let comp: StatusUpdateComponent;
  let fixture: ComponentFixture<StatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let statusService: StatusService;
  let usuarioService: UsuarioService;
  let problemaService: ProblemaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(StatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusService = TestBed.inject(StatusService);
    usuarioService = TestBed.inject(UsuarioService);
    problemaService = TestBed.inject(ProblemaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Usuario query and add missing value', () => {
      const status: IStatus = { id: 456 };
      const relator: IUsuario = { id: 32958 };
      status.relator = relator;
      const responsavel: IUsuario = { id: 38137 };
      status.responsavel = responsavel;

      const usuarioCollection: IUsuario[] = [{ id: 81756 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [relator, responsavel];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(usuarioCollection, ...additionalUsuarios);
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Problema query and add missing value', () => {
      const status: IStatus = { id: 456 };
      const problema: IProblema = { id: 34537 };
      status.problema = problema;

      const problemaCollection: IProblema[] = [{ id: 6610 }];
      jest.spyOn(problemaService, 'query').mockReturnValue(of(new HttpResponse({ body: problemaCollection })));
      const additionalProblemas = [problema];
      const expectedCollection: IProblema[] = [...additionalProblemas, ...problemaCollection];
      jest.spyOn(problemaService, 'addProblemaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(problemaService.query).toHaveBeenCalled();
      expect(problemaService.addProblemaToCollectionIfMissing).toHaveBeenCalledWith(problemaCollection, ...additionalProblemas);
      expect(comp.problemasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const status: IStatus = { id: 456 };
      const relator: IUsuario = { id: 65800 };
      status.relator = relator;
      const responsavel: IUsuario = { id: 56940 };
      status.responsavel = responsavel;
      const problema: IProblema = { id: 69516 };
      status.problema = problema;

      activatedRoute.data = of({ status });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(status));
      expect(comp.usuariosSharedCollection).toContain(relator);
      expect(comp.usuariosSharedCollection).toContain(responsavel);
      expect(comp.problemasSharedCollection).toContain(problema);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Status>>();
      const status = { id: 123 };
      jest.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: status }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusService.update).toHaveBeenCalledWith(status);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Status>>();
      const status = new Status();
      jest.spyOn(statusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: status }));
      saveSubject.complete();

      // THEN
      expect(statusService.create).toHaveBeenCalledWith(status);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Status>>();
      const status = { id: 123 };
      jest.spyOn(statusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ status });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusService.update).toHaveBeenCalledWith(status);
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

    describe('trackProblemaById', () => {
      it('Should return tracked Problema primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProblemaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
