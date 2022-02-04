jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFinalidadeAmostra, FinalidadeAmostra } from '../finalidade-amostra.model';
import { FinalidadeAmostraService } from '../service/finalidade-amostra.service';

import { FinalidadeAmostraRoutingResolveService } from './finalidade-amostra-routing-resolve.service';

describe('FinalidadeAmostra routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FinalidadeAmostraRoutingResolveService;
  let service: FinalidadeAmostraService;
  let resultFinalidadeAmostra: IFinalidadeAmostra | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FinalidadeAmostraRoutingResolveService);
    service = TestBed.inject(FinalidadeAmostraService);
    resultFinalidadeAmostra = undefined;
  });

  describe('resolve', () => {
    it('should return IFinalidadeAmostra returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFinalidadeAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFinalidadeAmostra).toEqual({ id: 123 });
    });

    it('should return new IFinalidadeAmostra if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFinalidadeAmostra = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFinalidadeAmostra).toEqual(new FinalidadeAmostra());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FinalidadeAmostra })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFinalidadeAmostra = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFinalidadeAmostra).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
