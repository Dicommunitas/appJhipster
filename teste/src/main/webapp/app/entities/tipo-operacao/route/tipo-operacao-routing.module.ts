import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoOperacaoComponent } from '../list/tipo-operacao.component';
import { TipoOperacaoDetailComponent } from '../detail/tipo-operacao-detail.component';
import { TipoOperacaoUpdateComponent } from '../update/tipo-operacao-update.component';
import { TipoOperacaoRoutingResolveService } from './tipo-operacao-routing-resolve.service';

const tipoOperacaoRoute: Routes = [
  {
    path: '',
    component: TipoOperacaoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoOperacaoDetailComponent,
    resolve: {
      tipoOperacao: TipoOperacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoOperacaoUpdateComponent,
    resolve: {
      tipoOperacao: TipoOperacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoOperacaoUpdateComponent,
    resolve: {
      tipoOperacao: TipoOperacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoOperacaoRoute)],
  exports: [RouterModule],
})
export class TipoOperacaoRoutingModule {}
