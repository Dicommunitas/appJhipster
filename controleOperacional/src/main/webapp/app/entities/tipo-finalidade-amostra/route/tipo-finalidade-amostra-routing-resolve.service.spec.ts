import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoFinalidadeAmostra, TipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';
import { TipoFinalidadeAmostraService } from '../service/tipo-finalidade-amostra.service';

import { TipoFinalidadeAmostraRoutingResolveService } from './tipo-finalidade-amostra-routing-resolve.service';

describe('TipoFinalidadeAmostra routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoFinalidadeAmostraRoutingResolveService;
  let service: TipoFinalidadeAmostraService;
  let resultTipoFinalidadeAmostra: ITipoFinalidadeAmostra | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TipoFinalidadeAmostraRoutingResolveService);
    service = TestBed.inject(TipoFinalidadeAmostraService);
    resultTipoFinalidadeAmostra = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoFinalidadeAmostra returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoFinalidadeAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoFinalidadeAmostra).toEqual({ id: 123 });
    });

    it('should return new ITipoFinalidadeAmostra if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoFinalidadeAmostra = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoFinalidadeAmostra).toEqual(new TipoFinalidadeAmostra());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoFinalidadeAmostra })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoFinalidadeAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoFinalidadeAmostra).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
