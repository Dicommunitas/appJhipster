import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoAmostra } from '../tipo-amostra.model';
import { TipoAmostraService } from '../service/tipo-amostra.service';

@Component({
  templateUrl: './tipo-amostra-delete-dialog.component.html',
})
export class TipoAmostraDeleteDialogComponent {
  tipoAmostra?: ITipoAmostra;

  constructor(protected tipoAmostraService: TipoAmostraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoAmostraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
