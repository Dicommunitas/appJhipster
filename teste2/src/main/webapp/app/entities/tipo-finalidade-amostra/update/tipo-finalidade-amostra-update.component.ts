import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITipoFinalidadeAmostra, TipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';
import { TipoFinalidadeAmostraService } from '../service/tipo-finalidade-amostra.service';

@Component({
  selector: 'jhi-tipo-finalidade-amostra-update',
  templateUrl: './tipo-finalidade-amostra-update.component.html',
})
export class TipoFinalidadeAmostraUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
    obrigatorioLacre: [null, [Validators.required]],
  });

  constructor(
    protected tipoFinalidadeAmostraService: TipoFinalidadeAmostraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoFinalidadeAmostra }) => {
      this.updateForm(tipoFinalidadeAmostra);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoFinalidadeAmostra = this.createFromForm();
    if (tipoFinalidadeAmostra.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoFinalidadeAmostraService.update(tipoFinalidadeAmostra));
    } else {
      this.subscribeToSaveResponse(this.tipoFinalidadeAmostraService.create(tipoFinalidadeAmostra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoFinalidadeAmostra>>): void {
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

  protected updateForm(tipoFinalidadeAmostra: ITipoFinalidadeAmostra): void {
    this.editForm.patchValue({
      id: tipoFinalidadeAmostra.id,
      descricao: tipoFinalidadeAmostra.descricao,
      obrigatorioLacre: tipoFinalidadeAmostra.obrigatorioLacre,
    });
  }

  protected createFromForm(): ITipoFinalidadeAmostra {
    return {
      ...new TipoFinalidadeAmostra(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      obrigatorioLacre: this.editForm.get(['obrigatorioLacre'])!.value,
    };
  }
}
