import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoRelatorio } from '../tipo-relatorio.model';
import { TipoRelatorioService } from '../service/tipo-relatorio.service';

@Component({
  templateUrl: './tipo-relatorio-delete-dialog.component.html',
})
export class TipoRelatorioDeleteDialogComponent {
  tipoRelatorio?: ITipoRelatorio;

  constructor(protected tipoRelatorioService: TipoRelatorioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoRelatorioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
