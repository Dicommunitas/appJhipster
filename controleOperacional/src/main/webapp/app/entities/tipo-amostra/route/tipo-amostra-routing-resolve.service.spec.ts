import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoAmostra, TipoAmostra } from '../tipo-amostra.model';
import { TipoAmostraService } from '../service/tipo-amostra.service';

import { TipoAmostraRoutingResolveService } from './tipo-amostra-routing-resolve.service';

describe('TipoAmostra routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoAmostraRoutingResolveService;
  let service: TipoAmostraService;
  let resultTipoAmostra: ITipoAmostra | undefined;

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
    routingResolveService = TestBed.inject(TipoAmostraRoutingResolveService);
    service = TestBed.inject(TipoAmostraService);
    resultTipoAmostra = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoAmostra returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoAmostra).toEqual({ id: 123 });
    });

    it('should return new ITipoAmostra if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoAmostra = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoAmostra).toEqual(new TipoAmostra());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoAmostra })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoAmostra).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
