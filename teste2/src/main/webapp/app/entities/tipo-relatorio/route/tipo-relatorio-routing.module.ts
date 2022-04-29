import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoRelatorioComponent } from '../list/tipo-relatorio.component';
import { TipoRelatorioDetailComponent } from '../detail/tipo-relatorio-detail.component';
import { TipoRelatorioUpdateComponent } from '../update/tipo-relatorio-update.component';
import { TipoRelatorioRoutingResolveService } from './tipo-relatorio-routing-resolve.service';

const tipoRelatorioRoute: Routes = [
  {
    path: '',
    component: TipoRelatorioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoRelatorioDetailComponent,
    resolve: {
      tipoRelatorio: TipoRelatorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoRelatorioUpdateComponent,
    resolve: {
      tipoRelatorio: TipoRelatorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoRelatorioUpdateComponent,
    resolve: {
      tipoRelatorio: TipoRelatorioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoRelatorioRoute)],
  exports: [RouterModule],
})
export class TipoRelatorioRoutingModule {}
