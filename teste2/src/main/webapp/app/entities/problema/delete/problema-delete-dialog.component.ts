import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProblema } from '../problema.model';
import { ProblemaService } from '../service/problema.service';

@Component({
  templateUrl: './problema-delete-dialog.component.html',
})
export class ProblemaDeleteDialogComponent {
  problema?: IProblema;

  constructor(protected problemaService: ProblemaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.problemaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
