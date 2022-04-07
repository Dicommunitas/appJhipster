import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrigemAmostraComponent } from './list/origem-amostra.component';
import { OrigemAmostraDetailComponent } from './detail/origem-amostra-detail.component';
import { OrigemAmostraUpdateComponent } from './update/origem-amostra-update.component';
import { OrigemAmostraDeleteDialogComponent } from './delete/origem-amostra-delete-dialog.component';
import { OrigemAmostraRoutingModule } from './route/origem-amostra-routing.module';

@NgModule({
  imports: [SharedModule, OrigemAmostraRoutingModule],
  declarations: [OrigemAmostraComponent, OrigemAmostraDetailComponent, OrigemAmostraUpdateComponent, OrigemAmostraDeleteDialogComponent],
  entryComponents: [OrigemAmostraDeleteDialogComponent],
})
export class OrigemAmostraModule {}
