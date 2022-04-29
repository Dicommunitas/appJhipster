import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAlertaProduto } from '../alerta-produto.model';
import { AlertaProdutoService } from '../service/alerta-produto.service';

@Component({
  templateUrl: './alerta-produto-delete-dialog.component.html',
})
export class AlertaProdutoDeleteDialogComponent {
  alertaProduto?: IAlertaProduto;

  constructor(protected alertaProdutoService: AlertaProdutoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alertaProdutoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
