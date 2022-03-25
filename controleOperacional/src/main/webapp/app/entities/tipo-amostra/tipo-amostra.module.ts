import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoAmostraComponent } from './list/tipo-amostra.component';
import { TipoAmostraDetailComponent } from './detail/tipo-amostra-detail.component';
import { TipoAmostraUpdateComponent } from './update/tipo-amostra-update.component';
import { TipoAmostraDeleteDialogComponent } from './delete/tipo-amostra-delete-dialog.component';
import { TipoAmostraRoutingModule } from './route/tipo-amostra-routing.module';

@NgModule({
  imports: [SharedModule, TipoAmostraRoutingModule],
  declarations: [TipoAmostraComponent, TipoAmostraDetailComponent, TipoAmostraUpdateComponent, TipoAmostraDeleteDialogComponent],
  entryComponents: [TipoAmostraDeleteDialogComponent],
})
export class TipoAmostraModule {}
