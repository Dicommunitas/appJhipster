import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoAmostra, TipoAmostra } from '../tipo-amostra.model';
import { TipoAmostraService } from '../service/tipo-amostra.service';

@Component({
  selector: 'jhi-tipo-amostra-update',
  templateUrl: './tipo-amostra-update.component.html',
})
export class TipoAmostraUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
  });

  constructor(protected tipoAmostraService: TipoAmostraService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoAmostra }) => {
      this.updateForm(tipoAmostra);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoAmostra = this.createFromForm();
    if (tipoAmostra.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoAmostraService.update(tipoAmostra));
    } else {
      this.subscribeToSaveResponse(this.tipoAmostraService.create(tipoAmostra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoAmostra>>): void {
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

  protected updateForm(tipoAmostra: ITipoAmostra): void {
    this.editForm.patchValue({
      id: tipoAmostra.id,
      descricao: tipoAmostra.descricao,
    });
  }

  protected createFromForm(): ITipoAmostra {
    return {
      ...new TipoAmostra(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
