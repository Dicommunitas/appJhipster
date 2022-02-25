import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAmostra, Amostra } from '../amostra.model';
import { AmostraService } from '../service/amostra.service';
import { IOperacao } from 'app/entities/operacao/operacao.model';
import { OperacaoService } from 'app/entities/operacao/service/operacao.service';
import { IOrigemAmostra } from 'app/entities/origem-amostra/origem-amostra.model';
import { OrigemAmostraService } from 'app/entities/origem-amostra/service/origem-amostra.service';
import { IProduto } from 'app/entities/produto/produto.model';
import { ProdutoService } from 'app/entities/produto/service/produto.service';
import { ITipoAmostra } from 'app/entities/tipo-amostra/tipo-amostra.model';
import { TipoAmostraService } from 'app/entities/tipo-amostra/service/tipo-amostra.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-amostra-update',
  templateUrl: './amostra-update.component.html',
})
export class AmostraUpdateComponent implements OnInit {
  isSaving = false;

  operacaosSharedCollection: IOperacao[] = [];
  origemAmostrasSharedCollection: IOrigemAmostra[] = [];
  produtosSharedCollection: IProduto[] = [];
  tipoAmostrasSharedCollection: ITipoAmostra[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    dataHora: [],
    observacao: [],
    identificadorExterno: [],
    amostraNoLaboratorio: [],
    operacao: [null, Validators.required],
    origemAmostra: [null, Validators.required],
    produto: [null, Validators.required],
    tipoAmostra: [null, Validators.required],
    amostradaPor: [],
    recebidaPor: [],
  });

  constructor(
    protected amostraService: AmostraService,
    protected operacaoService: OperacaoService,
    protected origemAmostraService: OrigemAmostraService,
    protected produtoService: ProdutoService,
    protected tipoAmostraService: TipoAmostraService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amostra }) => {
      if (amostra.id === undefined) {
        const today = dayjs().startOf('day');
        amostra.dataHora = today;
      }

      this.updateForm(amostra);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const amostra = this.createFromForm();
    if (amostra.id !== undefined) {
      this.subscribeToSaveResponse(this.amostraService.update(amostra));
    } else {
      this.subscribeToSaveResponse(this.amostraService.create(amostra));
    }
  }

  trackOperacaoById(index: number, item: IOperacao): number {
    return item.id!;
  }

  trackOrigemAmostraById(index: number, item: IOrigemAmostra): number {
    return item.id!;
  }

  trackProdutoById(index: number, item: IProduto): number {
    return item.id!;
  }

  trackTipoAmostraById(index: number, item: ITipoAmostra): number {
    return item.id!;
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmostra>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(amostra: IAmostra): void {
    this.editForm.patchValue({
      id: amostra.id,
      dataHora: amostra.dataHora ? amostra.dataHora.format(DATE_TIME_FORMAT) : null,
      observacao: amostra.observacao,
      identificadorExterno: amostra.identificadorExterno,
      amostraNoLaboratorio: amostra.amostraNoLaboratorio,
      operacao: amostra.operacao,
      origemAmostra: amostra.origemAmostra,
      produto: amostra.produto,
      tipoAmostra: amostra.tipoAmostra,
      amostradaPor: amostra.amostradaPor,
      recebidaPor: amostra.recebidaPor,
    });

    this.operacaosSharedCollection = this.operacaoService.addOperacaoToCollectionIfMissing(
      this.operacaosSharedCollection,
      amostra.operacao
    );
    this.origemAmostrasSharedCollection = this.origemAmostraService.addOrigemAmostraToCollectionIfMissing(
      this.origemAmostrasSharedCollection,
      amostra.origemAmostra
    );
    this.produtosSharedCollection = this.produtoService.addProdutoToCollectionIfMissing(this.produtosSharedCollection, amostra.produto);
    this.tipoAmostrasSharedCollection = this.tipoAmostraService.addTipoAmostraToCollectionIfMissing(
      this.tipoAmostrasSharedCollection,
      amostra.tipoAmostra
    );
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      amostra.amostradaPor,
      amostra.recebidaPor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.operacaoService
      .query()
      .pipe(map((res: HttpResponse<IOperacao[]>) => res.body ?? []))
      .pipe(
        map((operacaos: IOperacao[]) =>
          this.operacaoService.addOperacaoToCollectionIfMissing(operacaos, this.editForm.get('operacao')!.value)
        )
      )
      .subscribe((operacaos: IOperacao[]) => (this.operacaosSharedCollection = operacaos));

    this.origemAmostraService
      .query()
      .pipe(map((res: HttpResponse<IOrigemAmostra[]>) => res.body ?? []))
      .pipe(
        map((origemAmostras: IOrigemAmostra[]) =>
          this.origemAmostraService.addOrigemAmostraToCollectionIfMissing(origemAmostras, this.editForm.get('origemAmostra')!.value)
        )
      )
      .subscribe((origemAmostras: IOrigemAmostra[]) => (this.origemAmostrasSharedCollection = origemAmostras));

    this.produtoService
      .query()
      .pipe(map((res: HttpResponse<IProduto[]>) => res.body ?? []))
      .pipe(
        map((produtos: IProduto[]) => this.produtoService.addProdutoToCollectionIfMissing(produtos, this.editForm.get('produto')!.value))
      )
      .subscribe((produtos: IProduto[]) => (this.produtosSharedCollection = produtos));

    this.tipoAmostraService
      .query()
      .pipe(map((res: HttpResponse<ITipoAmostra[]>) => res.body ?? []))
      .pipe(
        map((tipoAmostras: ITipoAmostra[]) =>
          this.tipoAmostraService.addTipoAmostraToCollectionIfMissing(tipoAmostras, this.editForm.get('tipoAmostra')!.value)
        )
      )
      .subscribe((tipoAmostras: ITipoAmostra[]) => (this.tipoAmostrasSharedCollection = tipoAmostras));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(
            usuarios,
            this.editForm.get('amostradaPor')!.value,
            this.editForm.get('recebidaPor')!.value
          )
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IAmostra {
    return {
      ...new Amostra(),
      id: this.editForm.get(['id'])!.value,
      dataHora: this.editForm.get(['dataHora'])!.value ? dayjs(this.editForm.get(['dataHora'])!.value, DATE_TIME_FORMAT) : undefined,
      observacao: this.editForm.get(['observacao'])!.value,
      identificadorExterno: this.editForm.get(['identificadorExterno'])!.value,
      amostraNoLaboratorio: this.editForm.get(['amostraNoLaboratorio'])!.value,
      operacao: this.editForm.get(['operacao'])!.value,
      origemAmostra: this.editForm.get(['origemAmostra'])!.value,
      produto: this.editForm.get(['produto'])!.value,
      tipoAmostra: this.editForm.get(['tipoAmostra'])!.value,
      amostradaPor: this.editForm.get(['amostradaPor'])!.value,
      recebidaPor: this.editForm.get(['recebidaPor'])!.value,
    };
  }
}