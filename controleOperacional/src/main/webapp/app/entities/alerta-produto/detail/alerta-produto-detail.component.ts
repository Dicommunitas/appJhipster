import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlertaProduto } from '../alerta-produto.model';

@Component({
  selector: 'jhi-alerta-produto-detail',
  templateUrl: './alerta-produto-detail.component.html',
})
export class AlertaProdutoDetailComponent implements OnInit {
  alertaProduto: IAlertaProduto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alertaProduto }) => {
      this.alertaProduto = alertaProduto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
