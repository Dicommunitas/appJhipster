import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'c',
        data: { pageTitle: 'controleOperacionalApp.c.home.title' },
        loadChildren: () => import('./c/c.module').then(m => m.CModule),
      },
      {
        path: 'b',
        data: { pageTitle: 'controleOperacionalApp.b.home.title' },
        loadChildren: () => import('./b/b.module').then(m => m.BModule),
      },
      {
        path: 'a',
        data: { pageTitle: 'controleOperacionalApp.a.home.title' },
        loadChildren: () => import('./a/a.module').then(m => m.AModule),
      },
      {
        path: 'd',
        data: { pageTitle: 'controleOperacionalApp.d.home.title' },
        loadChildren: () => import('./d/d.module').then(m => m.DModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
