import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAlertaProduto, AlertaProduto } from '../alerta-produto.model';
import { AlertaProdutoService } from '../service/alerta-produto.service';

@Component({
  selector: 'jhi-alerta-produto-update',
  templateUrl: './alerta-produto-update.component.html',
})
export class AlertaProdutoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
  });

  constructor(protected alertaProdutoService: AlertaProdutoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alertaProduto }) => {
      this.updateForm(alertaProduto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alertaProduto = this.createFromForm();
    if (alertaProduto.id !== undefined) {
      this.subscribeToSaveResponse(this.alertaProdutoService.update(alertaProduto));
    } else {
      this.subscribeToSaveResponse(this.alertaProdutoService.create(alertaProduto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlertaProduto>>): void {
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

  protected updateForm(alertaProduto: IAlertaProduto): void {
    this.editForm.patchValue({
      id: alertaProduto.id,
      descricao: alertaProduto.descricao,
    });
  }

  protected createFromForm(): IAlertaProduto {
    return {
      ...new AlertaProduto(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
