import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AlertaProdutoComponent } from './list/alerta-produto.component';
import { AlertaProdutoDetailComponent } from './detail/alerta-produto-detail.component';
import { AlertaProdutoUpdateComponent } from './update/alerta-produto-update.component';
import { AlertaProdutoDeleteDialogComponent } from './delete/alerta-produto-delete-dialog.component';
import { AlertaProdutoRoutingModule } from './route/alerta-produto-routing.module';

@NgModule({
  imports: [SharedModule, AlertaProdutoRoutingModule],
  declarations: [AlertaProdutoComponent, AlertaProdutoDetailComponent, AlertaProdutoUpdateComponent, AlertaProdutoDeleteDialogComponent],
  entryComponents: [AlertaProdutoDeleteDialogComponent],
})
export class AlertaProdutoModule {}
