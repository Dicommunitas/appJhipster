import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFinalidadeAmostra } from '../finalidade-amostra.model';
import { FinalidadeAmostraService } from '../service/finalidade-amostra.service';
import { FinalidadeAmostraDeleteDialogComponent } from '../delete/finalidade-amostra-delete-dialog.component';

@Component({
  selector: 'jhi-finalidade-amostra',
  templateUrl: './finalidade-amostra.component.html',
})
export class FinalidadeAmostraComponent implements OnInit {
  finalidadeAmostras?: IFinalidadeAmostra[];
  isLoading = false;

  constructor(protected finalidadeAmostraService: FinalidadeAmostraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.finalidadeAmostraService.query().subscribe({
      next: (res: HttpResponse<IFinalidadeAmostra[]>) => {
        this.isLoading = false;
        this.finalidadeAmostras = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFinalidadeAmostra): number {
    return item.id!;
  }

  delete(finalidadeAmostra: IFinalidadeAmostra): void {
    const modalRef = this.modalService.open(FinalidadeAmostraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.finalidadeAmostra = finalidadeAmostra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
