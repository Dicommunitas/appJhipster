import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFinalidadeAmostra } from '../finalidade-amostra.model';
import { FinalidadeAmostraService } from '../service/finalidade-amostra.service';

@Component({
  templateUrl: './finalidade-amostra-delete-dialog.component.html',
})
export class FinalidadeAmostraDeleteDialogComponent {
  finalidadeAmostra?: IFinalidadeAmostra;

  constructor(protected finalidadeAmostraService: FinalidadeAmostraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.finalidadeAmostraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
