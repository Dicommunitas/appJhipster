import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoOperacao } from '../tipo-operacao.model';

@Component({
  selector: 'jhi-tipo-operacao-detail',
  templateUrl: './tipo-operacao-detail.component.html',
})
export class TipoOperacaoDetailComponent implements OnInit {
  tipoOperacao: ITipoOperacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoOperacao }) => {
      this.tipoOperacao = tipoOperacao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
