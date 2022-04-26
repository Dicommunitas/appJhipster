import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoOperacao } from '../tipo-operacao.model';
import { TipoOperacaoService } from '../service/tipo-operacao.service';
import { TipoOperacaoDeleteDialogComponent } from '../delete/tipo-operacao-delete-dialog.component';

@Component({
  selector: 'jhi-tipo-operacao',
  templateUrl: './tipo-operacao.component.html',
})
export class TipoOperacaoComponent implements OnInit {
  tipoOperacaos?: ITipoOperacao[];
  isLoading = false;

  constructor(protected tipoOperacaoService: TipoOperacaoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoOperacaoService.query().subscribe({
      next: (res: HttpResponse<ITipoOperacao[]>) => {
        this.isLoading = false;
        this.tipoOperacaos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITipoOperacao): number {
    return item.id!;
  }

  delete(tipoOperacao: ITipoOperacao): void {
    const modalRef = this.modalService.open(TipoOperacaoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoOperacao = tipoOperacao;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
