import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoFinalidadeAmostraComponent } from '../list/tipo-finalidade-amostra.component';
import { TipoFinalidadeAmostraDetailComponent } from '../detail/tipo-finalidade-amostra-detail.component';
import { TipoFinalidadeAmostraUpdateComponent } from '../update/tipo-finalidade-amostra-update.component';
import { TipoFinalidadeAmostraRoutingResolveService } from './tipo-finalidade-amostra-routing-resolve.service';

const tipoFinalidadeAmostraRoute: Routes = [
  {
    path: '',
    component: TipoFinalidadeAmostraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoFinalidadeAmostraDetailComponent,
    resolve: {
      tipoFinalidadeAmostra: TipoFinalidadeAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoFinalidadeAmostraUpdateComponent,
    resolve: {
      tipoFinalidadeAmostra: TipoFinalidadeAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoFinalidadeAmostraUpdateComponent,
    resolve: {
      tipoFinalidadeAmostra: TipoFinalidadeAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoFinalidadeAmostraRoute)],
  exports: [RouterModule],
})
export class TipoFinalidadeAmostraRoutingModule {}
