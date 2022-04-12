import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmostra } from '../amostra.model';

@Component({
  selector: 'jhi-amostra-detail',
  templateUrl: './amostra-detail.component.html',
})
export class AmostraDetailComponent implements OnInit {
  amostra: IAmostra | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amostra }) => {
      this.amostra = amostra;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
