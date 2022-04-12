import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoRelatorio, TipoRelatorio } from '../tipo-relatorio.model';
import { TipoRelatorioService } from '../service/tipo-relatorio.service';

import { TipoRelatorioRoutingResolveService } from './tipo-relatorio-routing-resolve.service';

describe('TipoRelatorio routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoRelatorioRoutingResolveService;
  let service: TipoRelatorioService;
  let resultTipoRelatorio: ITipoRelatorio | undefined;

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
    routingResolveService = TestBed.inject(TipoRelatorioRoutingResolveService);
    service = TestBed.inject(TipoRelatorioService);
    resultTipoRelatorio = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoRelatorio returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoRelatorio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoRelatorio).toEqual({ id: 123 });
    });

    it('should return new ITipoRelatorio if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoRelatorio = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoRelatorio).toEqual(new TipoRelatorio());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoRelatorio })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoRelatorio = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoRelatorio).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
