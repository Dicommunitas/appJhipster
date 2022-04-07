import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';
import { TipoFinalidadeAmostraService } from '../service/tipo-finalidade-amostra.service';

@Component({
  templateUrl: './tipo-finalidade-amostra-delete-dialog.component.html',
})
export class TipoFinalidadeAmostraDeleteDialogComponent {
  tipoFinalidadeAmostra?: ITipoFinalidadeAmostra;

  constructor(protected tipoFinalidadeAmostraService: TipoFinalidadeAmostraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoFinalidadeAmostraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
