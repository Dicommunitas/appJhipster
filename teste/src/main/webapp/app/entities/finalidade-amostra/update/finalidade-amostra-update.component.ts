import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFinalidadeAmostra, FinalidadeAmostra } from '../finalidade-amostra.model';
import { FinalidadeAmostraService } from '../service/finalidade-amostra.service';
import { ITipoFinalidadeAmostra } from 'app/entities/tipo-finalidade-amostra/tipo-finalidade-amostra.model';
import { TipoFinalidadeAmostraService } from 'app/entities/tipo-finalidade-amostra/service/tipo-finalidade-amostra.service';
import { IAmostra } from 'app/entities/amostra/amostra.model';
import { AmostraService } from 'app/entities/amostra/service/amostra.service';

@Component({
  selector: 'jhi-finalidade-amostra-update',
  templateUrl: './finalidade-amostra-update.component.html',
})
export class FinalidadeAmostraUpdateComponent implements OnInit {
  isSaving = false;

  tipoFinalidadeAmostrasSharedCollection: ITipoFinalidadeAmostra[] = [];
  amostrasSharedCollection: IAmostra[] = [];

  editForm = this.fb.group({
    id: [],
    lacre: [null, []],
    tipoFinalidadeAmostra: [null, Validators.required],
    amostra: [null, Validators.required],
  });

  constructor(
    protected finalidadeAmostraService: FinalidadeAmostraService,
    protected tipoFinalidadeAmostraService: TipoFinalidadeAmostraService,
    protected amostraService: AmostraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ finalidadeAmostra }) => {
      this.updateForm(finalidadeAmostra);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const finalidadeAmostra = this.createFromForm();
    if (finalidadeAmostra.id !== undefined) {
      this.subscribeToSaveResponse(this.finalidadeAmostraService.update(finalidadeAmostra));
    } else {
      this.subscribeToSaveResponse(this.finalidadeAmostraService.create(finalidadeAmostra));
    }
  }

  trackTipoFinalidadeAmostraById(_index: number, item: ITipoFinalidadeAmostra): number {
    return item.id!;
  }

  trackAmostraById(_index: number, item: IAmostra): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinalidadeAmostra>>): void {
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

  protected updateForm(finalidadeAmostra: IFinalidadeAmostra): void {
    this.editForm.patchValue({
      id: finalidadeAmostra.id,
      lacre: finalidadeAmostra.lacre,
      tipoFinalidadeAmostra: finalidadeAmostra.tipoFinalidadeAmostra,
      amostra: finalidadeAmostra.amostra,
    });

    this.tipoFinalidadeAmostrasSharedCollection = this.tipoFinalidadeAmostraService.addTipoFinalidadeAmostraToCollectionIfMissing(
      this.tipoFinalidadeAmostrasSharedCollection,
      finalidadeAmostra.tipoFinalidadeAmostra
    );
    this.amostrasSharedCollection = this.amostraService.addAmostraToCollectionIfMissing(
      this.amostrasSharedCollection,
      finalidadeAmostra.amostra
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoFinalidadeAmostraService
      .query()
      .pipe(map((res: HttpResponse<ITipoFinalidadeAmostra[]>) => res.body ?? []))
      .pipe(
        map((tipoFinalidadeAmostras: ITipoFinalidadeAmostra[]) =>
          this.tipoFinalidadeAmostraService.addTipoFinalidadeAmostraToCollectionIfMissing(
            tipoFinalidadeAmostras,
            this.editForm.get('tipoFinalidadeAmostra')!.value
          )
        )
      )
      .subscribe(
        (tipoFinalidadeAmostras: ITipoFinalidadeAmostra[]) => (this.tipoFinalidadeAmostrasSharedCollection = tipoFinalidadeAmostras)
      );

    this.amostraService
      .query()
      .pipe(map((res: HttpResponse<IAmostra[]>) => res.body ?? []))
      .pipe(
        map((amostras: IAmostra[]) => this.amostraService.addAmostraToCollectionIfMissing(amostras, this.editForm.get('amostra')!.value))
      )
      .subscribe((amostras: IAmostra[]) => (this.amostrasSharedCollection = amostras));
  }

  protected createFromForm(): IFinalidadeAmostra {
    return {
      ...new FinalidadeAmostra(),
      id: this.editForm.get(['id'])!.value,
      lacre: this.editForm.get(['lacre'])!.value,
      tipoFinalidadeAmostra: this.editForm.get(['tipoFinalidadeAmostra'])!.value,
      amostra: this.editForm.get(['amostra'])!.value,
    };
  }
}
