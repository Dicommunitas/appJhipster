import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoFinalidadeAmostra, TipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';
import { TipoFinalidadeAmostraService } from '../service/tipo-finalidade-amostra.service';

@Injectable({ providedIn: 'root' })
export class TipoFinalidadeAmostraRoutingResolveService implements Resolve<ITipoFinalidadeAmostra> {
  constructor(protected service: TipoFinalidadeAmostraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoFinalidadeAmostra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoFinalidadeAmostra: HttpResponse<TipoFinalidadeAmostra>) => {
          if (tipoFinalidadeAmostra.body) {
            return of(tipoFinalidadeAmostra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoFinalidadeAmostra());
  }
}
