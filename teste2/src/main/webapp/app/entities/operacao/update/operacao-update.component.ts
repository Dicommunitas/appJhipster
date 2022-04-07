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
import { IProduto } from 'app/entities/produto/produto.model';
import { ProdutoService } from 'app/entities/produto/service/produto.service';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';
import { TipoOperacaoService } from 'app/entities/tipo-operacao/service/tipo-operacao.service';

@Component({
  selector: 'jhi-operacao-update',
  templateUrl: './operacao-update.component.html',
})
export class OperacaoUpdateComponent implements OnInit {
  isSaving = false;

  produtosSharedCollection: IProduto[] = [];
  tipoOperacaosSharedCollection: ITipoOperacao[] = [];

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
    volumePeso: [null, [Validators.required]],
    inicio: [],
    fim: [],
    quantidadeAmostras: [],
    observacao: [],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    produto: [null, Validators.required],
    tipoOperacao: [null, Validators.required],
  });

  constructor(
    protected operacaoService: OperacaoService,
    protected produtoService: ProdutoService,
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
        operacao.createdDate = today;
        operacao.lastModifiedDate = today;
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

  trackProdutoById(_index: number, item: IProduto): number {
    return item.id!;
  }

  trackTipoOperacaoById(_index: number, item: ITipoOperacao): number {
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
      createdBy: operacao.createdBy,
      createdDate: operacao.createdDate ? operacao.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: operacao.lastModifiedBy,
      lastModifiedDate: operacao.lastModifiedDate ? operacao.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      produto: operacao.produto,
      tipoOperacao: operacao.tipoOperacao,
    });

    this.produtosSharedCollection = this.produtoService.addProdutoToCollectionIfMissing(this.produtosSharedCollection, operacao.produto);
    this.tipoOperacaosSharedCollection = this.tipoOperacaoService.addTipoOperacaoToCollectionIfMissing(
      this.tipoOperacaosSharedCollection,
      operacao.tipoOperacao
    );
  }

  protected loadRelationshipsOptions(): void {
    this.produtoService
      .query()
      .pipe(map((res: HttpResponse<IProduto[]>) => res.body ?? []))
      .pipe(
        map((produtos: IProduto[]) => this.produtoService.addProdutoToCollectionIfMissing(produtos, this.editForm.get('produto')!.value))
      )
      .subscribe((produtos: IProduto[]) => (this.produtosSharedCollection = produtos));

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
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      produto: this.editForm.get(['produto'])!.value,
      tipoOperacao: this.editForm.get(['tipoOperacao'])!.value,
    };
  }
}
