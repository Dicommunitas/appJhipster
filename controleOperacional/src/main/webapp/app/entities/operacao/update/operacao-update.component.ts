import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOperacao, Operacao } from '../operacao.model';
import { OperacaoService } from '../service/operacao.service';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';
import { TipoOperacaoService } from 'app/entities/tipo-operacao/service/tipo-operacao.service';

@Component({
  selector: 'jhi-operacao-update',
  templateUrl: './operacao-update.component.html',
})
export class OperacaoUpdateComponent implements OnInit {
  isSaving = false;

  tipoOperacaosSharedCollection: ITipoOperacao[] = [];

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
    volumePeso: [null, [Validators.required]],
    inicio: [],
    fim: [],
    quantidadeAmostras: [null, [Validators.required]],
    observacao: [],
    tipoOperacao: [null, Validators.required],
  });

  constructor(
    protected operacaoService: OperacaoService,
    protected tipoOperacaoService: TipoOperacaoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operacao }) => {
      if (operacao.id === undefined) {
        const today = dayjs().startOf('day');
        operacao.inicio = today;
        operacao.fim = today;
      }

      this.updateForm(operacao);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operacao = this.createFromForm();
    if (operacao.id !== undefined) {
      this.subscribeToSaveResponse(this.operacaoService.update(operacao));
    } else {
      this.subscribeToSaveResponse(this.operacaoService.create(operacao));
    }
  }

  trackTipoOperacaoById(index: number, item: ITipoOperacao): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperacao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(operacao: IOperacao): void {
    this.editForm.patchValue({
      id: operacao.id,
      descricao: operacao.descricao,
      volumePeso: operacao.volumePeso,
      inicio: operacao.inicio ? operacao.inicio.format(DATE_TIME_FORMAT) : null,
      fim: operacao.fim ? operacao.fim.format(DATE_TIME_FORMAT) : null,
      quantidadeAmostras: operacao.quantidadeAmostras,
      observacao: operacao.observacao,
      tipoOperacao: operacao.tipoOperacao,
    });

    this.tipoOperacaosSharedCollection = this.tipoOperacaoService.addTipoOperacaoToCollectionIfMissing(
      this.tipoOperacaosSharedCollection,
      operacao.tipoOperacao
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoOperacaoService
      .query()
      .pipe(map((res: HttpResponse<ITipoOperacao[]>) => res.body ?? []))
      .pipe(
        map((tipoOperacaos: ITipoOperacao[]) =>
          this.tipoOperacaoService.addTipoOperacaoToCollectionIfMissing(tipoOperacaos, this.editForm.get('tipoOperacao')!.value)
        )
      )
      .subscribe((tipoOperacaos: ITipoOperacao[]) => (this.tipoOperacaosSharedCollection = tipoOperacaos));
  }

  protected createFromForm(): IOperacao {
    return {
      ...new Operacao(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      volumePeso: this.editForm.get(['volumePeso'])!.value,
      inicio: this.editForm.get(['inicio'])!.value ? dayjs(this.editForm.get(['inicio'])!.value, DATE_TIME_FORMAT) : undefined,
      fim: this.editForm.get(['fim'])!.value ? dayjs(this.editForm.get(['fim'])!.value, DATE_TIME_FORMAT) : undefined,
      quantidadeAmostras: this.editForm.get(['quantidadeAmostras'])!.value,
      observacao: this.editForm.get(['observacao'])!.value,
      tipoOperacao: this.editForm.get(['tipoOperacao'])!.value,
    };
  }
}
