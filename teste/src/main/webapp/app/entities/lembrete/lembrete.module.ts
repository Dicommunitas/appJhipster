import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LembreteComponent } from './list/lembrete.component';
import { LembreteDetailComponent } from './detail/lembrete-detail.component';
import { LembreteUpdateComponent } from './update/lembrete-update.component';
import { LembreteDeleteDialogComponent } from './delete/lembrete-delete-dialog.component';
import { LembreteRoutingModule } from './route/lembrete-routing.module';

@NgModule({
  imports: [SharedModule, LembreteRoutingModule],
  declarations: [LembreteComponent, LembreteDetailComponent, LembreteUpdateComponent, LembreteDeleteDialogComponent],
  entryComponents: [LembreteDeleteDialogComponent],
})
export class LembreteModule {}
