import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoAmostra, TipoAmostra } from '../tipo-amostra.model';
import { TipoAmostraService } from '../service/tipo-amostra.service';

@Injectable({ providedIn: 'root' })
export class TipoAmostraRoutingResolveService implements Resolve<ITipoAmostra> {
  constructor(protected service: TipoAmostraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoAmostra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoAmostra: HttpResponse<TipoAmostra>) => {
          if (tipoAmostra.body) {
            return of(tipoAmostra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoAmostra());
  }
}
