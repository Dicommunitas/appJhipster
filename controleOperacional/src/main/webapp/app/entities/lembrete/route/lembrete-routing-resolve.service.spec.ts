jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILembrete, Lembrete } from '../lembrete.model';
import { LembreteService } from '../service/lembrete.service';

import { LembreteRoutingResolveService } from './lembrete-routing-resolve.service';

describe('Lembrete routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LembreteRoutingResolveService;
  let service: LembreteService;
  let resultLembrete: ILembrete | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LembreteRoutingResolveService);
    service = TestBed.inject(LembreteService);
    resultLembrete = undefined;
  });

  describe('resolve', () => {
    it('should return ILembrete returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLembrete = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLembrete).toEqual({ id: 123 });
    });

    it('should return new ILembrete if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLembrete = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLembrete).toEqual(new Lembrete());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Lembrete })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLembrete = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLembrete).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
