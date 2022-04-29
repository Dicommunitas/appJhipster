import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAmostra } from '../amostra.model';
import { AmostraService } from '../service/amostra.service';

@Component({
  templateUrl: './amostra-delete-dialog.component.html',
})
export class AmostraDeleteDialogComponent {
  amostra?: IAmostra;

  constructor(protected amostraService: AmostraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.amostraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
