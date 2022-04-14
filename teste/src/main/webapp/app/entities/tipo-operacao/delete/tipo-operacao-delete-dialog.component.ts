import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoOperacao } from '../tipo-operacao.model';
import { TipoOperacaoService } from '../service/tipo-operacao.service';

@Component({
  templateUrl: './tipo-operacao-delete-dialog.component.html',
})
export class TipoOperacaoDeleteDialogComponent {
  tipoOperacao?: ITipoOperacao;

  constructor(protected tipoOperacaoService: TipoOperacaoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoOperacaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
