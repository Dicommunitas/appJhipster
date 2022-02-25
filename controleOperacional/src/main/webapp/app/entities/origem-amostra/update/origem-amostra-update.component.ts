import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrigemAmostra, OrigemAmostra } from '../origem-amostra.model';
import { OrigemAmostraService } from '../service/origem-amostra.service';

@Component({
  selector: 'jhi-origem-amostra-update',
  templateUrl: './origem-amostra-update.component.html',
})
export class OrigemAmostraUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
  });

  constructor(protected origemAmostraService: OrigemAmostraService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ origemAmostra }) => {
      this.updateForm(origemAmostra);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const origemAmostra = this.createFromForm();
    if (origemAmostra.id !== undefined) {
      this.subscribeToSaveResponse(this.origemAmostraService.update(origemAmostra));
    } else {
      this.subscribeToSaveResponse(this.origemAmostraService.create(origemAmostra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrigemAmostra>>): void {
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

  protected updateForm(origemAmostra: IOrigemAmostra): void {
    this.editForm.patchValue({
      id: origemAmostra.id,
      descricao: origemAmostra.descricao,
    });
  }

  protected createFromForm(): IOrigemAmostra {
    return {
      ...new OrigemAmostra(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
