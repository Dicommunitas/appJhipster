jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoAmostraService } from '../service/tipo-amostra.service';
import { ITipoAmostra, TipoAmostra } from '../tipo-amostra.model';

import { TipoAmostraUpdateComponent } from './tipo-amostra-update.component';

describe('TipoAmostra Management Update Component', () => {
  let comp: TipoAmostraUpdateComponent;
  let fixture: ComponentFixture<TipoAmostraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoAmostraService: TipoAmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoAmostraUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TipoAmostraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoAmostraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoAmostraService = TestBed.inject(TipoAmostraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoAmostra: ITipoAmostra = { id: 456 };

      activatedRoute.data = of({ tipoAmostra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoAmostra));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoAmostra>>();
      const tipoAmostra = { id: 123 };
      jest.spyOn(tipoAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoAmostra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoAmostraService.update).toHaveBeenCalledWith(tipoAmostra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoAmostra>>();
      const tipoAmostra = new TipoAmostra();
      jest.spyOn(tipoAmostraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoAmostra }));
      saveSubject.complete();

      // THEN
      expect(tipoAmostraService.create).toHaveBeenCalledWith(tipoAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoAmostra>>();
      const tipoAmostra = { id: 123 };
      jest.spyOn(tipoAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoAmostraService.update).toHaveBeenCalledWith(tipoAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
