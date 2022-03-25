import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OperacaoComponent } from '../list/operacao.component';
import { OperacaoDetailComponent } from '../detail/operacao-detail.component';
import { OperacaoUpdateComponent } from '../update/operacao-update.component';
import { OperacaoRoutingResolveService } from './operacao-routing-resolve.service';

const operacaoRoute: Routes = [
  {
    path: '',
    component: OperacaoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperacaoDetailComponent,
    resolve: {
      operacao: OperacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperacaoUpdateComponent,
    resolve: {
      operacao: OperacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperacaoUpdateComponent,
    resolve: {
      operacao: OperacaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(operacaoRoute)],
  exports: [RouterModule],
})
export class OperacaoRoutingModule {}
