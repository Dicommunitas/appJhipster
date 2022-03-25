import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoAmostraComponent } from '../list/tipo-amostra.component';
import { TipoAmostraDetailComponent } from '../detail/tipo-amostra-detail.component';
import { TipoAmostraUpdateComponent } from '../update/tipo-amostra-update.component';
import { TipoAmostraRoutingResolveService } from './tipo-amostra-routing-resolve.service';

const tipoAmostraRoute: Routes = [
  {
    path: '',
    component: TipoAmostraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoAmostraDetailComponent,
    resolve: {
      tipoAmostra: TipoAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoAmostraUpdateComponent,
    resolve: {
      tipoAmostra: TipoAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoAmostraUpdateComponent,
    resolve: {
      tipoAmostra: TipoAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoAmostraRoute)],
  exports: [RouterModule],
})
export class TipoAmostraRoutingModule {}
