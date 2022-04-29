import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoFinalidadeAmostraComponent } from './list/tipo-finalidade-amostra.component';
import { TipoFinalidadeAmostraDetailComponent } from './detail/tipo-finalidade-amostra-detail.component';
import { TipoFinalidadeAmostraUpdateComponent } from './update/tipo-finalidade-amostra-update.component';
import { TipoFinalidadeAmostraDeleteDialogComponent } from './delete/tipo-finalidade-amostra-delete-dialog.component';
import { TipoFinalidadeAmostraRoutingModule } from './route/tipo-finalidade-amostra-routing.module';

@NgModule({
  imports: [SharedModule, TipoFinalidadeAmostraRoutingModule],
  declarations: [
    TipoFinalidadeAmostraComponent,
    TipoFinalidadeAmostraDetailComponent,
    TipoFinalidadeAmostraUpdateComponent,
    TipoFinalidadeAmostraDeleteDialogComponent,
  ],
  entryComponents: [TipoFinalidadeAmostraDeleteDialogComponent],
})
export class TipoFinalidadeAmostraModule {}
