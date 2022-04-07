import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoOperacao, TipoOperacao } from '../tipo-operacao.model';
import { TipoOperacaoService } from '../service/tipo-operacao.service';

@Injectable({ providedIn: 'root' })
export class TipoOperacaoRoutingResolveService implements Resolve<ITipoOperacao> {
  constructor(protected service: TipoOperacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoOperacao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoOperacao: HttpResponse<TipoOperacao>) => {
          if (tipoOperacao.body) {
            return of(tipoOperacao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoOperacao());
  }
}
