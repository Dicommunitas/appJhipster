import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrigemAmostra, OrigemAmostra } from '../origem-amostra.model';
import { OrigemAmostraService } from '../service/origem-amostra.service';

@Injectable({ providedIn: 'root' })
export class OrigemAmostraRoutingResolveService implements Resolve<IOrigemAmostra> {
  constructor(protected service: OrigemAmostraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrigemAmostra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((origemAmostra: HttpResponse<OrigemAmostra>) => {
          if (origemAmostra.body) {
            return of(origemAmostra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrigemAmostra());
  }
}
