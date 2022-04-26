import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoRelatorio } from '../tipo-relatorio.model';
import { TipoRelatorioService } from '../service/tipo-relatorio.service';
import { TipoRelatorioDeleteDialogComponent } from '../delete/tipo-relatorio-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-relatorio',
  templateUrl: './tipo-relatorio.component.html',
})
export class TipoRelatorioComponent implements OnInit {
  tipoRelatorios?: ITipoRelatorio[];
  isLoading = false;

  constructor(protected tipoRelatorioService: TipoRelatorioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoRelatorioService.query().subscribe({
      next: (res: HttpResponse<ITipoRelatorio[]>) => {
        this.isLoading = false;
        this.tipoRelatorios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITipoRelatorio): number {
    return item.id!;
  }

  delete(tipoRelatorio: ITipoRelatorio): void {
    const modalRef = this.modalService.open(TipoRelatorioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoRelatorio = tipoRelatorio;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
