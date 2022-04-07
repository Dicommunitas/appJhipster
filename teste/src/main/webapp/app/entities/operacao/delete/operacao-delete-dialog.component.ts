import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperacao } from '../operacao.model';
import { OperacaoService } from '../service/operacao.service';

@Component({
  templateUrl: './operacao-delete-dialog.component.html',
})
export class OperacaoDeleteDialogComponent {
  operacao?: IOperacao;

  constructor(protected operacaoService: OperacaoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operacaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
