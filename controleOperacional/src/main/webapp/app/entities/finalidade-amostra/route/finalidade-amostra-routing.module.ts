import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FinalidadeAmostraComponent } from '../list/finalidade-amostra.component';
import { FinalidadeAmostraDetailComponent } from '../detail/finalidade-amostra-detail.component';
import { FinalidadeAmostraUpdateComponent } from '../update/finalidade-amostra-update.component';
import { FinalidadeAmostraRoutingResolveService } from './finalidade-amostra-routing-resolve.service';

const finalidadeAmostraRoute: Routes = [
  {
    path: '',
    component: FinalidadeAmostraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FinalidadeAmostraDetailComponent,
    resolve: {
      finalidadeAmostra: FinalidadeAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FinalidadeAmostraUpdateComponent,
    resolve: {
      finalidadeAmostra: FinalidadeAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FinalidadeAmostraUpdateComponent,
    resolve: {
      finalidadeAmostra: FinalidadeAmostraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(finalidadeAmostraRoute)],
  exports: [RouterModule],
})
export class FinalidadeAmostraRoutingModule {}
