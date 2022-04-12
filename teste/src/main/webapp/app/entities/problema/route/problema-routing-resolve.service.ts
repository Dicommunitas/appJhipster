import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProblema, Problema } from '../problema.model';
import { ProblemaService } from '../service/problema.service';

@Injectable({ providedIn: 'root' })
export class ProblemaRoutingResolveService implements Resolve<IProblema> {
  constructor(protected service: ProblemaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProblema> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((problema: HttpResponse<Problema>) => {
          if (problema.body) {
            return of(problema.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Problema());
  }
}
