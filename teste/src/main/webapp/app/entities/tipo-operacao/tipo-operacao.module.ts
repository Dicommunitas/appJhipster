import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoOperacaoComponent } from './list/tipo-operacao.component';
import { TipoOperacaoDetailComponent } from './detail/tipo-operacao-detail.component';
import { TipoOperacaoUpdateComponent } from './update/tipo-operacao-update.component';
import { TipoOperacaoDeleteDialogComponent } from './delete/tipo-operacao-delete-dialog.component';
import { TipoOperacaoRoutingModule } from './route/tipo-operacao-routing.module';

@NgModule({
  imports: [SharedModule, TipoOperacaoRoutingModule],
  declarations: [TipoOperacaoComponent, TipoOperacaoDetailComponent, TipoOperacaoUpdateComponent, TipoOperacaoDeleteDialogComponent],
  entryComponents: [TipoOperacaoDeleteDialogComponent],
})
export class TipoOperacaoModule {}
