import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoOperacao, TipoOperacao } from '../tipo-operacao.model';
import { TipoOperacaoService } from '../service/tipo-operacao.service';

@Component({
  selector: 'jhi-tipo-operacao-update',
  templateUrl: './tipo-operacao-update.component.html',
})
export class TipoOperacaoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
  });

  constructor(protected tipoOperacaoService: TipoOperacaoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoOperacao }) => {
      this.updateForm(tipoOperacao);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoOperacao = this.createFromForm();
    if (tipoOperacao.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoOperacaoService.update(tipoOperacao));
    } else {
      this.subscribeToSaveResponse(this.tipoOperacaoService.create(tipoOperacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoOperacao>>): void {
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

  protected updateForm(tipoOperacao: ITipoOperacao): void {
    this.editForm.patchValue({
      id: tipoOperacao.id,
      descricao: tipoOperacao.descricao,
    });
  }

  protected createFromForm(): ITipoOperacao {
    return {
      ...new TipoOperacao(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
