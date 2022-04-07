import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LembreteComponent } from '../list/lembrete.component';
import { LembreteDetailComponent } from '../detail/lembrete-detail.component';
import { LembreteUpdateComponent } from '../update/lembrete-update.component';
import { LembreteRoutingResolveService } from './lembrete-routing-resolve.service';

const lembreteRoute: Routes = [
  {
    path: '',
    component: LembreteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LembreteDetailComponent,
    resolve: {
      lembrete: LembreteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LembreteUpdateComponent,
    resolve: {
      lembrete: LembreteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LembreteUpdateComponent,
    resolve: {
      lembrete: LembreteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lembreteRoute)],
  exports: [RouterModule],
})
export class LembreteRoutingModule {}
