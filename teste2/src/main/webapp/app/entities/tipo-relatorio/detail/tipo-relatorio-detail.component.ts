import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoRelatorio } from '../tipo-relatorio.model';

@Component({
  selector: 'jhi-tipo-relatorio-detail',
  templateUrl: './tipo-relatorio-detail.component.html',
})
export class TipoRelatorioDetailComponent implements OnInit {
  tipoRelatorio: ITipoRelatorio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoRelatorio }) => {
      this.tipoRelatorio = tipoRelatorio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
