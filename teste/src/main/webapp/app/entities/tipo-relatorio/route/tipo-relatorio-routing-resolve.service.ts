import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoRelatorio, TipoRelatorio } from '../tipo-relatorio.model';
import { TipoRelatorioService } from '../service/tipo-relatorio.service';

@Injectable({ providedIn: 'root' })
export class TipoRelatorioRoutingResolveService implements Resolve<ITipoRelatorio> {
  constructor(protected service: TipoRelatorioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoRelatorio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoRelatorio: HttpResponse<TipoRelatorio>) => {
          if (tipoRelatorio.body) {
            return of(tipoRelatorio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoRelatorio());
  }
}
