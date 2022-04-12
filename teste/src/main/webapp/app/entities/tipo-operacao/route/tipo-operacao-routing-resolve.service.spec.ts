import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoOperacao, TipoOperacao } from '../tipo-operacao.model';
import { TipoOperacaoService } from '../service/tipo-operacao.service';

import { TipoOperacaoRoutingResolveService } from './tipo-operacao-routing-resolve.service';

describe('TipoOperacao routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoOperacaoRoutingResolveService;
  let service: TipoOperacaoService;
  let resultTipoOperacao: ITipoOperacao | undefined;

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
    routingResolveService = TestBed.inject(TipoOperacaoRoutingResolveService);
    service = TestBed.inject(TipoOperacaoService);
    resultTipoOperacao = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoOperacao returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoOperacao = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoOperacao).toEqual({ id: 123 });
    });

    it('should return new ITipoOperacao if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoOperacao = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoOperacao).toEqual(new TipoOperacao());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoOperacao })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoOperacao = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoOperacao).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
