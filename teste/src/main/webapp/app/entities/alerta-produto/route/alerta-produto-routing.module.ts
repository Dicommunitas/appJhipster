import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AlertaProdutoComponent } from '../list/alerta-produto.component';
import { AlertaProdutoDetailComponent } from '../detail/alerta-produto-detail.component';
import { AlertaProdutoUpdateComponent } from '../update/alerta-produto-update.component';
import { AlertaProdutoRoutingResolveService } from './alerta-produto-routing-resolve.service';

const alertaProdutoRoute: Routes = [
  {
    path: '',
    component: AlertaProdutoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AlertaProdutoDetailComponent,
    resolve: {
      alertaProduto: AlertaProdutoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AlertaProdutoUpdateComponent,
    resolve: {
      alertaProduto: AlertaProdutoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AlertaProdutoUpdateComponent,
    resolve: {
      alertaProduto: AlertaProdutoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(alertaProdutoRoute)],
  exports: [RouterModule],
})
export class AlertaProdutoRoutingModule {}
