import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProblemaComponent } from '../list/problema.component';
import { ProblemaDetailComponent } from '../detail/problema-detail.component';
import { ProblemaUpdateComponent } from '../update/problema-update.component';
import { ProblemaRoutingResolveService } from './problema-routing-resolve.service';

const problemaRoute: Routes = [
  {
    path: '',
    component: ProblemaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProblemaDetailComponent,
    resolve: {
      problema: ProblemaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProblemaUpdateComponent,
    resolve: {
      problema: ProblemaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProblemaUpdateComponent,
    resolve: {
      problema: ProblemaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(problemaRoute)],
  exports: [RouterModule],
})
export class ProblemaRoutingModule {}
