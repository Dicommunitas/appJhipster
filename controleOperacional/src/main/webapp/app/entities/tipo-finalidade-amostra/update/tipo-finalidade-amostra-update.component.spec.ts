jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoFinalidadeAmostraService } from '../service/tipo-finalidade-amostra.service';
import { ITipoFinalidadeAmostra, TipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';

import { TipoFinalidadeAmostraUpdateComponent } from './tipo-finalidade-amostra-update.component';

describe('TipoFinalidadeAmostra Management Update Component', () => {
  let comp: TipoFinalidadeAmostraUpdateComponent;
  let fixture: ComponentFixture<TipoFinalidadeAmostraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoFinalidadeAmostraService: TipoFinalidadeAmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoFinalidadeAmostraUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TipoFinalidadeAmostraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoFinalidadeAmostraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoFinalidadeAmostraService = TestBed.inject(TipoFinalidadeAmostraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 456 };

      activatedRoute.data = of({ tipoFinalidadeAmostra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoFinalidadeAmostra));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoFinalidadeAmostra>>();
      const tipoFinalidadeAmostra = { id: 123 };
      jest.spyOn(tipoFinalidadeAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFinalidadeAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoFinalidadeAmostra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoFinalidadeAmostraService.update).toHaveBeenCalledWith(tipoFinalidadeAmostra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoFinalidadeAmostra>>();
      const tipoFinalidadeAmostra = new TipoFinalidadeAmostra();
      jest.spyOn(tipoFinalidadeAmostraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFinalidadeAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoFinalidadeAmostra }));
      saveSubject.complete();

      // THEN
      expect(tipoFinalidadeAmostraService.create).toHaveBeenCalledWith(tipoFinalidadeAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoFinalidadeAmostra>>();
      const tipoFinalidadeAmostra = { id: 123 };
      jest.spyOn(tipoFinalidadeAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFinalidadeAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoFinalidadeAmostraService.update).toHaveBeenCalledWith(tipoFinalidadeAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
