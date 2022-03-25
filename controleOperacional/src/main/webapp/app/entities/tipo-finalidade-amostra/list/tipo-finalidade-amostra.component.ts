import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';
import { TipoFinalidadeAmostraService } from '../service/tipo-finalidade-amostra.service';
import { TipoFinalidadeAmostraDeleteDialogComponent } from '../delete/tipo-finalidade-amostra-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-finalidade-amostra',
  templateUrl: './tipo-finalidade-amostra.component.html',
})
export class TipoFinalidadeAmostraComponent implements OnInit {
  tipoFinalidadeAmostras?: ITipoFinalidadeAmostra[];
  isLoading = false;

  constructor(protected tipoFinalidadeAmostraService: TipoFinalidadeAmostraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoFinalidadeAmostraService.query().subscribe({
      next: (res: HttpResponse<ITipoFinalidadeAmostra[]>) => {
        this.isLoading = false;
        this.tipoFinalidadeAmostras = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITipoFinalidadeAmostra): number {
    return item.id!;
  }

  delete(tipoFinalidadeAmostra: ITipoFinalidadeAmostra): void {
    const modalRef = this.modalService.open(TipoFinalidadeAmostraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoFinalidadeAmostra = tipoFinalidadeAmostra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
