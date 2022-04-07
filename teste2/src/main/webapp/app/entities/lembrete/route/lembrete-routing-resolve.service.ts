import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILembrete, Lembrete } from '../lembrete.model';
import { LembreteService } from '../service/lembrete.service';

@Injectable({ providedIn: 'root' })
export class LembreteRoutingResolveService implements Resolve<ILembrete> {
  constructor(protected service: LembreteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILembrete> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lembrete: HttpResponse<Lembrete>) => {
          if (lembrete.body) {
            return of(lembrete.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Lembrete());
  }
}
