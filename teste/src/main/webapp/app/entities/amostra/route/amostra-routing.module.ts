import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmostraComponent } from '../list/amostra.component';
import { AmostraDetailComponent } from '../detail/amostra-detail.component';
import { AmostraUpdateComponent } from '../update/amostra-update.component';
import { AmostraRoutingResolveService } from './amostra-routing-resolve.service';

const amostraRoute: Routes = [
  {
    path: '',
    component: AmostraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmostraDetailComponent,
    resolve: {
      amostra: AmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmostraUpdateComponent,
    resolve: {
      amostra: AmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmostraUpdateComponent,
    resolve: {
      amostra: AmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amostraRoute)],
  exports: [RouterModule],
})
export class AmostraRoutingModule {}
