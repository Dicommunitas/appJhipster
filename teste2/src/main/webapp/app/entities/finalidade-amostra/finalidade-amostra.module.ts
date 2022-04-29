import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FinalidadeAmostraComponent } from './list/finalidade-amostra.component';
import { FinalidadeAmostraDetailComponent } from './detail/finalidade-amostra-detail.component';
import { FinalidadeAmostraUpdateComponent } from './update/finalidade-amostra-update.component';
import { FinalidadeAmostraDeleteDialogComponent } from './delete/finalidade-amostra-delete-dialog.component';
import { FinalidadeAmostraRoutingModule } from './route/finalidade-amostra-routing.module';

@NgModule({
  imports: [SharedModule, FinalidadeAmostraRoutingModule],
  declarations: [
    FinalidadeAmostraComponent,
    FinalidadeAmostraDetailComponent,
    FinalidadeAmostraUpdateComponent,
    FinalidadeAmostraDeleteDialogComponent,
  ],
  entryComponents: [FinalidadeAmostraDeleteDialogComponent],
})
export class FinalidadeAmostraModule {}
