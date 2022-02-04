import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrigemAmostra } from '../origem-amostra.model';
import { OrigemAmostraService } from '../service/origem-amostra.service';
import { OrigemAmostraDeleteDialogComponent } from '../delete/origem-amostra-delete-dialog.component';

@Component({
  selector: 'jhi-origem-amostra',
  templateUrl: './origem-amostra.component.html',
})
export class OrigemAmostraComponent implements OnInit {
  origemAmostras?: IOrigemAmostra[];
  isLoading = false;

  constructor(protected origemAmostraService: OrigemAmostraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.origemAmostraService.query().subscribe(
      (res: HttpResponse<IOrigemAmostra[]>) => {
        this.isLoading = false;
        this.origemAmostras = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrigemAmostra): number {
    return item.id!;
  }

  delete(origemAmostra: IOrigemAmostra): void {
    const modalRef = this.modalService.open(OrigemAmostraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.origemAmostra = origemAmostra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
