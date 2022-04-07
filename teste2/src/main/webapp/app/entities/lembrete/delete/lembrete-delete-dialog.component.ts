import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILembrete } from '../lembrete.model';
import { LembreteService } from '../service/lembrete.service';

@Component({
  templateUrl: './lembrete-delete-dialog.component.html',
})
export class LembreteDeleteDialogComponent {
  lembrete?: ILembrete;

  constructor(protected lembreteService: LembreteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lembreteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
