import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoAmostra } from '../tipo-amostra.model';
import { TipoAmostraService } from '../service/tipo-amostra.service';
import { TipoAmostraDeleteDialogComponent } from '../delete/tipo-amostra-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-amostra',
  templateUrl: './tipo-amostra.component.html',
})
export class TipoAmostraComponent implements OnInit {
  tipoAmostras?: ITipoAmostra[];
  isLoading = false;

  constructor(protected tipoAmostraService: TipoAmostraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoAmostraService.query().subscribe(
      (res: HttpResponse<ITipoAmostra[]>) => {
        this.isLoading = false;
        this.tipoAmostras = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITipoAmostra): number {
    return item.id!;
  }

  delete(tipoAmostra: ITipoAmostra): void {
    const modalRef = this.modalService.open(TipoAmostraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoAmostra = tipoAmostra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
