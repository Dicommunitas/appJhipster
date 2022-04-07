import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoRelatorioComponent } from './list/tipo-relatorio.component';
import { TipoRelatorioDetailComponent } from './detail/tipo-relatorio-detail.component';
import { TipoRelatorioUpdateComponent } from './update/tipo-relatorio-update.component';
import { TipoRelatorioDeleteDialogComponent } from './delete/tipo-relatorio-delete-dialog.component';
import { TipoRelatorioRoutingModule } from './route/tipo-relatorio-routing.module';

@NgModule({
  imports: [SharedModule, TipoRelatorioRoutingModule],
  declarations: [TipoRelatorioComponent, TipoRelatorioDetailComponent, TipoRelatorioUpdateComponent, TipoRelatorioDeleteDialogComponent],
  entryComponents: [TipoRelatorioDeleteDialogComponent],
})
export class TipoRelatorioModule {}
