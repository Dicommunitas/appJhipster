jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAlertaProduto, AlertaProduto } from '../alerta-produto.model';
import { AlertaProdutoService } from '../service/alerta-produto.service';

import { AlertaProdutoRoutingResolveService } from './alerta-produto-routing-resolve.service';

describe('AlertaProduto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AlertaProdutoRoutingResolveService;
  let service: AlertaProdutoService;
  let resultAlertaProduto: IAlertaProduto | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AlertaProdutoRoutingResolveService);
    service = TestBed.inject(AlertaProdutoService);
    resultAlertaProduto = undefined;
  });

  describe('resolve', () => {
    it('should return IAlertaProduto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAlertaProduto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAlertaProduto).toEqual({ id: 123 });
    });

    it('should return new IAlertaProduto if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAlertaProduto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAlertaProduto).toEqual(new AlertaProduto());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AlertaProduto })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAlertaProduto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAlertaProduto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
