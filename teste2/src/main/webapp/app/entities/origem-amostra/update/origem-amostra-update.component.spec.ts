import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrigemAmostraService } from '../service/origem-amostra.service';
import { IOrigemAmostra, OrigemAmostra } from '../origem-amostra.model';

import { OrigemAmostraUpdateComponent } from './origem-amostra-update.component';

describe('OrigemAmostra Management Update Component', () => {
  let comp: OrigemAmostraUpdateComponent;
  let fixture: ComponentFixture<OrigemAmostraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let origemAmostraService: OrigemAmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrigemAmostraUpdateComponent],
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
      .overrideTemplate(OrigemAmostraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrigemAmostraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    origemAmostraService = TestBed.inject(OrigemAmostraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const origemAmostra: IOrigemAmostra = { id: 456 };

      activatedRoute.data = of({ origemAmostra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(origemAmostra));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrigemAmostra>>();
      const origemAmostra = { id: 123 };
      jest.spyOn(origemAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ origemAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: origemAmostra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(origemAmostraService.update).toHaveBeenCalledWith(origemAmostra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrigemAmostra>>();
      const origemAmostra = new OrigemAmostra();
      jest.spyOn(origemAmostraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ origemAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: origemAmostra }));
      saveSubject.complete();

      // THEN
      expect(origemAmostraService.create).toHaveBeenCalledWith(origemAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrigemAmostra>>();
      const origemAmostra = { id: 123 };
      jest.spyOn(origemAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ origemAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(origemAmostraService.update).toHaveBeenCalledWith(origemAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
