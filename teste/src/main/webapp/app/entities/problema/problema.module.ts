import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProblemaComponent } from './list/problema.component';
import { ProblemaDetailComponent } from './detail/problema-detail.component';
import { ProblemaUpdateComponent } from './update/problema-update.component';
import { ProblemaDeleteDialogComponent } from './delete/problema-delete-dialog.component';
import { ProblemaRoutingModule } from './route/problema-routing.module';

@NgModule({
  imports: [SharedModule, ProblemaRoutingModule],
  declarations: [ProblemaComponent, ProblemaDetailComponent, ProblemaUpdateComponent, ProblemaDeleteDialogComponent],
  entryComponents: [ProblemaDeleteDialogComponent],
})
export class ProblemaModule {}
