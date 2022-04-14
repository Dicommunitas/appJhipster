import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProblema } from '../problema.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-problema-detail',
  templateUrl: './problema-detail.component.html',
})
export class ProblemaDetailComponent implements OnInit {
  problema: IProblema | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ problema }) => {
      this.problema = problema;
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
