import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrigemAmostraComponent } from '../list/origem-amostra.component';
import { OrigemAmostraDetailComponent } from '../detail/origem-amostra-detail.component';
import { OrigemAmostraUpdateComponent } from '../update/origem-amostra-update.component';
import { OrigemAmostraRoutingResolveService } from './origem-amostra-routing-resolve.service';

const origemAmostraRoute: Routes = [
  {
    path: '',
    component: OrigemAmostraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrigemAmostraDetailComponent,
    resolve: {
      origemAmostra: OrigemAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrigemAmostraUpdateComponent,
    resolve: {
      origemAmostra: OrigemAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrigemAmostraUpdateComponent,
    resolve: {
      origemAmostra: OrigemAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(origemAmostraRoute)],
  exports: [RouterModule],
})
export class OrigemAmostraRoutingModule {}
