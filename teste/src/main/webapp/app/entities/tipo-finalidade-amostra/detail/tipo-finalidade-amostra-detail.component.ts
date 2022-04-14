import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';

@Component({
  selector: 'jhi-tipo-finalidade-amostra-detail',
  templateUrl: './tipo-finalidade-amostra-detail.component.html',
})
export class TipoFinalidadeAmostraDetailComponent implements OnInit {
  tipoFinalidadeAmostra: ITipoFinalidadeAmostra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoFinalidadeAmostra }) => {
      this.tipoFinalidadeAmostra = tipoFinalidadeAmostra;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
