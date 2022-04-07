import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrigemAmostra } from '../origem-amostra.model';
import { OrigemAmostraService } from '../service/origem-amostra.service';

@Component({
  templateUrl: './origem-amostra-delete-dialog.component.html',
})
export class OrigemAmostraDeleteDialogComponent {
  origemAmostra?: IOrigemAmostra;

  constructor(protected origemAmostraService: OrigemAmostraService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.origemAmostraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
