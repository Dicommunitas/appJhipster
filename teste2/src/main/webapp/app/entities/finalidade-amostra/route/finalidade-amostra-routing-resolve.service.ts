import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinalidadeAmostra, FinalidadeAmostra } from '../finalidade-amostra.model';
import { FinalidadeAmostraService } from '../service/finalidade-amostra.service';

@Injectable({ providedIn: 'root' })
export class FinalidadeAmostraRoutingResolveService implements Resolve<IFinalidadeAmostra> {
  constructor(protected service: FinalidadeAmostraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFinalidadeAmostra> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((finalidadeAmostra: HttpResponse<FinalidadeAmostra>) => {
          if (finalidadeAmostra.body) {
            return of(finalidadeAmostra.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FinalidadeAmostra());
  }
}
