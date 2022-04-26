import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAlertaProduto } from '../alerta-produto.model';
import { AlertaProdutoService } from '../service/alerta-produto.service';
import { AlertaProdutoDeleteDialogComponent } from '../delete/alerta-produto-delete-dialog.component';

@Component({
  selector: 'jhi-alerta-produto',
  templateUrl: './alerta-produto.component.html',
})
export class AlertaProdutoComponent implements OnInit {
  alertaProdutos?: IAlertaProduto[];
  isLoading = false;

  constructor(protected alertaProdutoService: AlertaProdutoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.alertaProdutoService.query().subscribe({
      next: (res: HttpResponse<IAlertaProduto[]>) => {
        this.isLoading = false;
        this.alertaProdutos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAlertaProduto): number {
    return item.id!;
  }

  delete(alertaProduto: IAlertaProduto): void {
    const modalRef = this.modalService.open(AlertaProdutoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.alertaProduto = alertaProduto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
