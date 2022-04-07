import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OperacaoComponent } from './list/operacao.component';
import { OperacaoDetailComponent } from './detail/operacao-detail.component';
import { OperacaoUpdateComponent } from './update/operacao-update.component';
import { OperacaoDeleteDialogComponent } from './delete/operacao-delete-dialog.component';
import { OperacaoRoutingModule } from './route/operacao-routing.module';

@NgModule({
  imports: [SharedModule, OperacaoRoutingModule],
  declarations: [OperacaoComponent, OperacaoDetailComponent, OperacaoUpdateComponent, OperacaoDeleteDialogComponent],
  entryComponents: [OperacaoDeleteDialogComponent],
})
export class OperacaoModule {}
