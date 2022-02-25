import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduto, Produto } from '../produto.model';
import { ProdutoService } from '../service/produto.service';
import { IAlertaProduto } from 'app/entities/alerta-produto/alerta-produto.model';
import { AlertaProdutoService } from 'app/entities/alerta-produto/service/alerta-produto.service';

@Component({
  selector: 'jhi-produto-update',
  templateUrl: './produto-update.component.html',
})
export class ProdutoUpdateComponent implements OnInit {
  isSaving = false;

  alertaProdutosSharedCollection: IAlertaProduto[] = [];

  editForm = this.fb.group({
    id: [],
    codigoBDEMQ: [null, [Validators.required, Validators.maxLength(3)]],
    nomeCurto: [null, [Validators.required]],
    nomeCompleto: [null, [Validators.required]],
    alertas: [],
  });

  constructor(
    protected produtoService: ProdutoService,
    protected alertaProdutoService: AlertaProdutoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produto }) => {
      this.updateForm(produto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produto = this.createFromForm();
    if (produto.id !== undefined) {
      this.subscribeToSaveResponse(this.produtoService.update(produto));
    } else {
      this.subscribeToSaveResponse(this.produtoService.create(produto));
    }
  }

  trackAlertaProdutoById(index: number, item: IAlertaProduto): number {
    return item.id!;
  }

  getSelectedAlertaProduto(option: IAlertaProduto, selectedVals?: IAlertaProduto[]): IAlertaProduto {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduto>>): void {
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

  protected updateForm(produto: IProduto): void {
    this.editForm.patchValue({
      id: produto.id,
      codigoBDEMQ: produto.codigoBDEMQ,
      nomeCurto: produto.nomeCurto,
      nomeCompleto: produto.nomeCompleto,
      alertas: produto.alertas,
    });

    this.alertaProdutosSharedCollection = this.alertaProdutoService.addAlertaProdutoToCollectionIfMissing(
      this.alertaProdutosSharedCollection,
      ...(produto.alertas ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alertaProdutoService
      .query()
      .pipe(map((res: HttpResponse<IAlertaProduto[]>) => res.body ?? []))
      .pipe(
        map((alertaProdutos: IAlertaProduto[]) =>
          this.alertaProdutoService.addAlertaProdutoToCollectionIfMissing(alertaProdutos, ...(this.editForm.get('alertas')!.value ?? []))
        )
      )
      .subscribe((alertaProdutos: IAlertaProduto[]) => (this.alertaProdutosSharedCollection = alertaProdutos));
  }

  protected createFromForm(): IProduto {
    return {
      ...new Produto(),
      id: this.editForm.get(['id'])!.value,
      codigoBDEMQ: this.editForm.get(['codigoBDEMQ'])!.value,
      nomeCurto: this.editForm.get(['nomeCurto'])!.value,
      nomeCompleto: this.editForm.get(['nomeCompleto'])!.value,
      alertas: this.editForm.get(['alertas'])!.value,
    };
  }
}
