import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FinalidadeAmostraService } from '../service/finalidade-amostra.service';
import { IFinalidadeAmostra, FinalidadeAmostra } from '../finalidade-amostra.model';
import { ITipoFinalidadeAmostra } from 'app/entities/tipo-finalidade-amostra/tipo-finalidade-amostra.model';
import { TipoFinalidadeAmostraService } from 'app/entities/tipo-finalidade-amostra/service/tipo-finalidade-amostra.service';
import { IAmostra } from 'app/entities/amostra/amostra.model';
import { AmostraService } from 'app/entities/amostra/service/amostra.service';

import { FinalidadeAmostraUpdateComponent } from './finalidade-amostra-update.component';

describe('FinalidadeAmostra Management Update Component', () => {
  let comp: FinalidadeAmostraUpdateComponent;
  let fixture: ComponentFixture<FinalidadeAmostraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let finalidadeAmostraService: FinalidadeAmostraService;
  let tipoFinalidadeAmostraService: TipoFinalidadeAmostraService;
  let amostraService: AmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FinalidadeAmostraUpdateComponent],
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
      .overrideTemplate(FinalidadeAmostraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinalidadeAmostraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    finalidadeAmostraService = TestBed.inject(FinalidadeAmostraService);
    tipoFinalidadeAmostraService = TestBed.inject(TipoFinalidadeAmostraService);
    amostraService = TestBed.inject(AmostraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TipoFinalidadeAmostra query and add missing value', () => {
      const finalidadeAmostra: IFinalidadeAmostra = { id: 456 };
      const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 24427 };
      finalidadeAmostra.tipoFinalidadeAmostra = tipoFinalidadeAmostra;

      const tipoFinalidadeAmostraCollection: ITipoFinalidadeAmostra[] = [{ id: 70796 }];
      jest.spyOn(tipoFinalidadeAmostraService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoFinalidadeAmostraCollection })));
      const additionalTipoFinalidadeAmostras = [tipoFinalidadeAmostra];
      const expectedCollection: ITipoFinalidadeAmostra[] = [...additionalTipoFinalidadeAmostras, ...tipoFinalidadeAmostraCollection];
      jest.spyOn(tipoFinalidadeAmostraService, 'addTipoFinalidadeAmostraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ finalidadeAmostra });
      comp.ngOnInit();

      expect(tipoFinalidadeAmostraService.query).toHaveBeenCalled();
      expect(tipoFinalidadeAmostraService.addTipoFinalidadeAmostraToCollectionIfMissing).toHaveBeenCalledWith(
        tipoFinalidadeAmostraCollection,
        ...additionalTipoFinalidadeAmostras
      );
      expect(comp.tipoFinalidadeAmostrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Amostra query and add missing value', () => {
      const finalidadeAmostra: IFinalidadeAmostra = { id: 456 };
      const amostra: IAmostra = { id: 46615 };
      finalidadeAmostra.amostra = amostra;

      const amostraCollection: IAmostra[] = [{ id: 65218 }];
      jest.spyOn(amostraService, 'query').mockReturnValue(of(new HttpResponse({ body: amostraCollection })));
      const additionalAmostras = [amostra];
      const expectedCollection: IAmostra[] = [...additionalAmostras, ...amostraCollection];
      jest.spyOn(amostraService, 'addAmostraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ finalidadeAmostra });
      comp.ngOnInit();

      expect(amostraService.query).toHaveBeenCalled();
      expect(amostraService.addAmostraToCollectionIfMissing).toHaveBeenCalledWith(amostraCollection, ...additionalAmostras);
      expect(comp.amostrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const finalidadeAmostra: IFinalidadeAmostra = { id: 456 };
      const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 38754 };
      finalidadeAmostra.tipoFinalidadeAmostra = tipoFinalidadeAmostra;
      const amostra: IAmostra = { id: 51153 };
      finalidadeAmostra.amostra = amostra;

      activatedRoute.data = of({ finalidadeAmostra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(finalidadeAmostra));
      expect(comp.tipoFinalidadeAmostrasSharedCollection).toContain(tipoFinalidadeAmostra);
      expect(comp.amostrasSharedCollection).toContain(amostra);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FinalidadeAmostra>>();
      const finalidadeAmostra = { id: 123 };
      jest.spyOn(finalidadeAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finalidadeAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: finalidadeAmostra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(finalidadeAmostraService.update).toHaveBeenCalledWith(finalidadeAmostra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FinalidadeAmostra>>();
      const finalidadeAmostra = new FinalidadeAmostra();
      jest.spyOn(finalidadeAmostraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finalidadeAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: finalidadeAmostra }));
      saveSubject.complete();

      // THEN
      expect(finalidadeAmostraService.create).toHaveBeenCalledWith(finalidadeAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FinalidadeAmostra>>();
      const finalidadeAmostra = { id: 123 };
      jest.spyOn(finalidadeAmostraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ finalidadeAmostra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(finalidadeAmostraService.update).toHaveBeenCalledWith(finalidadeAmostra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTipoFinalidadeAmostraById', () => {
      it('Should return tracked TipoFinalidadeAmostra primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTipoFinalidadeAmostraById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAmostraById', () => {
      it('Should return tracked Amostra primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAmostraById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
