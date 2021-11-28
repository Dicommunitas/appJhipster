import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'c',
        data: { pageTitle: 'controleAmostraGatewayApp.c.home.title' },
        loadChildren: () => import('./c/c.module').then(m => m.CModule),
      },
      {
        path: 'b',
        data: { pageTitle: 'controleAmostraGatewayApp.b.home.title' },
        loadChildren: () => import('./b/b.module').then(m => m.BModule),
      },
      {
        path: 'a',
        data: { pageTitle: 'controleAmostraGatewayApp.a.home.title' },
        loadChildren: () => import('./a/a.module').then(m => m.AModule),
      },
      {
        path: 'd',
        data: { pageTitle: 'controleAmostraGatewayApp.d.home.title' },
        loadChildren: () => import('./d/d.module').then(m => m.DModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
