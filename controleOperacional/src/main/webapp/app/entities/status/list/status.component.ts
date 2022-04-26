import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStatus } from '../status.model';
import { StatusService } from '../service/status.service';
import { StatusDeleteDialogComponent } from '../delete/status-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-status',
  templateUrl: './status.component.html',
})
export class StatusComponent implements OnInit {
  statuses?: IStatus[];
  isLoading = false;

  constructor(protected statusService: StatusService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.statusService.query().subscribe({
      next: (res: HttpResponse<IStatus[]>) => {
        this.isLoading = false;
        this.statuses = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IStatus): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(status: IStatus): void {
    const modalRef = this.modalService.open(StatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.status = status;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
