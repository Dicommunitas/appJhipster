import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILembrete } from '../lembrete.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-lembrete-detail',
  templateUrl: './lembrete-detail.component.html',
})
export class LembreteDetailComponent implements OnInit {
  lembrete: ILembrete | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lembrete }) => {
      this.lembrete = lembrete;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
