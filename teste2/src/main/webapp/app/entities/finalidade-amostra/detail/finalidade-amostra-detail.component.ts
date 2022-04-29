import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinalidadeAmostra } from '../finalidade-amostra.model';

@Component({
  selector: 'jhi-finalidade-amostra-detail',
  templateUrl: './finalidade-amostra-detail.component.html',
})
export class FinalidadeAmostraDetailComponent implements OnInit {
  finalidadeAmostra: IFinalidadeAmostra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ finalidadeAmostra }) => {
      this.finalidadeAmostra = finalidadeAmostra;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
