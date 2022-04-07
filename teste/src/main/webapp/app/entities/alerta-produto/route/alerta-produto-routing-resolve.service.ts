import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlertaProduto, AlertaProduto } from '../alerta-produto.model';
import { AlertaProdutoService } from '../service/alerta-produto.service';

@Injectable({ providedIn: 'root' })
export class AlertaProdutoRoutingResolveService implements Resolve<IAlertaProduto> {
  constructor(protected service: AlertaProdutoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlertaProduto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((alertaProduto: HttpResponse<AlertaProduto>) => {
          if (alertaProduto.body) {
            return of(alertaProduto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AlertaProduto());
  }
}
