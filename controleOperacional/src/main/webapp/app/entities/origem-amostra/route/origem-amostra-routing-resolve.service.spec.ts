jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrigemAmostra, OrigemAmostra } from '../origem-amostra.model';
import { OrigemAmostraService } from '../service/origem-amostra.service';

import { OrigemAmostraRoutingResolveService } from './origem-amostra-routing-resolve.service';

describe('OrigemAmostra routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OrigemAmostraRoutingResolveService;
  let service: OrigemAmostraService;
  let resultOrigemAmostra: IOrigemAmostra | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(OrigemAmostraRoutingResolveService);
    service = TestBed.inject(OrigemAmostraService);
    resultOrigemAmostra = undefined;
  });

  describe('resolve', () => {
    it('should return IOrigemAmostra returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrigemAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrigemAmostra).toEqual({ id: 123 });
    });

    it('should return new IOrigemAmostra if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrigemAmostra = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOrigemAmostra).toEqual(new OrigemAmostra());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrigemAmostra })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrigemAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrigemAmostra).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
