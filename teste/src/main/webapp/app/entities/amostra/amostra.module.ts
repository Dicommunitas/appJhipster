import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmostraComponent } from './list/amostra.component';
import { AmostraDetailComponent } from './detail/amostra-detail.component';
import { AmostraUpdateComponent } from './update/amostra-update.component';
import { AmostraDeleteDialogComponent } from './delete/amostra-delete-dialog.component';
import { AmostraRoutingModule } from './route/amostra-routing.module';

@NgModule({
  imports: [SharedModule, AmostraRoutingModule],
  declarations: [AmostraComponent, AmostraDetailComponent, AmostraUpdateComponent, AmostraDeleteDialogComponent],
  entryComponents: [AmostraDeleteDialogComponent],
})
export class AmostraModule {}
