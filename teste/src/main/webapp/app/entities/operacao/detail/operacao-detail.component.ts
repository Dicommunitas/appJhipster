import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperacao } from '../operacao.model';

@Component({
  selector: 'jhi-operacao-detail',
  templateUrl: './operacao-detail.component.html',
})
export class OperacaoDetailComponent implements OnInit {
  operacao: IOperacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operacao }) => {
      this.operacao = operacao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
