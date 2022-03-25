import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoAmostra } from '../tipo-amostra.model';

@Component({
  selector: 'jhi-tipo-amostra-detail',
  templateUrl: './tipo-amostra-detail.component.html',
})
export class TipoAmostraDetailComponent implements OnInit {
  tipoAmostra: ITipoAmostra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoAmostra }) => {
      this.tipoAmostra = tipoAmostra;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
