import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperacao, Operacao } from '../operacao.model';
import { OperacaoService } from '../service/operacao.service';

@Injectable({ providedIn: 'root' })
export class OperacaoRoutingResolveService implements Resolve<IOperacao> {
  constructor(protected service: OperacaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperacao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operacao: HttpResponse<Operacao>) => {
          if (operacao.body) {
            return of(operacao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Operacao());
  }
}
