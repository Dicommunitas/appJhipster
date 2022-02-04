import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'problema',
        data: { pageTitle: 'controleOperacionalApp.problema.home.title' },
        loadChildren: () => import('./problema/problema.module').then(m => m.ProblemaModule),
      },
      {
        path: 'status',
        data: { pageTitle: 'controleOperacionalApp.status.home.title' },
        loadChildren: () => import('./status/status.module').then(m => m.StatusModule),
      },
      {
        path: 'alerta-produto',
        data: { pageTitle: 'controleOperacionalApp.alertaProduto.home.title' },
        loadChildren: () => import('./alerta-produto/alerta-produto.module').then(m => m.AlertaProdutoModule),
      },
      {
        path: 'relatorio',
        data: { pageTitle: 'controleOperacionalApp.relatorio.home.title' },
        loadChildren: () => import('./relatorio/relatorio.module').then(m => m.RelatorioModule),
      },
      {
        path: 'tipo-relatorio',
        data: { pageTitle: 'controleOperacionalApp.tipoRelatorio.home.title' },
        loadChildren: () => import('./tipo-relatorio/tipo-relatorio.module').then(m => m.TipoRelatorioModule),
      },
      {
        path: 'lembrete',
        data: { pageTitle: 'controleOperacionalApp.lembrete.home.title' },
        loadChildren: () => import('./lembrete/lembrete.module').then(m => m.LembreteModule),
      },
      {
        path: 'usuario',
        data: { pageTitle: 'controleOperacionalApp.usuario.home.title' },
        loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'amostra',
        data: { pageTitle: 'controleOperacionalApp.amostra.home.title' },
        loadChildren: () => import('./amostra/amostra.module').then(m => m.AmostraModule),
      },
      {
        path: 'operacao',
        data: { pageTitle: 'controleOperacionalApp.operacao.home.title' },
        loadChildren: () => import('./operacao/operacao.module').then(m => m.OperacaoModule),
      },
      {
        path: 'tipo-operacao',
        data: { pageTitle: 'controleOperacionalApp.tipoOperacao.home.title' },
        loadChildren: () => import('./tipo-operacao/tipo-operacao.module').then(m => m.TipoOperacaoModule),
      },
      {
        path: 'origem-amostra',
        data: { pageTitle: 'controleOperacionalApp.origemAmostra.home.title' },
        loadChildren: () => import('./origem-amostra/origem-amostra.module').then(m => m.OrigemAmostraModule),
      },
      {
        path: 'tipo-amostra',
        data: { pageTitle: 'controleOperacionalApp.tipoAmostra.home.title' },
        loadChildren: () => import('./tipo-amostra/tipo-amostra.module').then(m => m.TipoAmostraModule),
      },
      {
        path: 'finalidade-amostra',
        data: { pageTitle: 'controleOperacionalApp.finalidadeAmostra.home.title' },
        loadChildren: () => import('./finalidade-amostra/finalidade-amostra.module').then(m => m.FinalidadeAmostraModule),
      },
      {
        path: 'tipo-finalidade-amostra',
        data: { pageTitle: 'controleOperacionalApp.tipoFinalidadeAmostra.home.title' },
        loadChildren: () => import('./tipo-finalidade-amostra/tipo-finalidade-amostra.module').then(m => m.TipoFinalidadeAmostraModule),
      },
      {
        path: 'produto',
        data: { pageTitle: 'controleOperacionalApp.produto.home.title' },
        loadChildren: () => import('./produto/produto.module').then(m => m.ProdutoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
