import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAmostra, Amostra } from '../amostra.model';
import { AmostraService } from '../service/amostra.service';

@Injectable({ providedIn: 'root' })
export class AmostraRoutingResolveService implements Resolve<IAmostra> {
  constructor(protected service: AmostraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAmostra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((amostra: HttpResponse<Amostra>) => {
          if (amostra.body) {
            return of(amostra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Amostra());
  }
}
