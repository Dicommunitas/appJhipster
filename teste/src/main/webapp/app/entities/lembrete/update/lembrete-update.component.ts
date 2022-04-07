import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILembrete, Lembrete } from '../lembrete.model';
import { LembreteService } from '../service/lembrete.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { TipoRelatorioService } from 'app/entities/tipo-relatorio/service/tipo-relatorio.service';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';
import { TipoOperacaoService } from 'app/entities/tipo-operacao/service/tipo-operacao.service';

@Component({
  selector: 'jhi-lembrete-update',
  templateUrl: './lembrete-update.component.html',
})
export class LembreteUpdateComponent implements OnInit {
  isSaving = false;

  tipoRelatoriosSharedCollection: ITipoRelatorio[] = [];
  tipoOperacaosSharedCollection: ITipoOperacao[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    descricao: [null, [Validators.required]],
    createdBy: [null, [Validators.maxLength(50)]],
    createdDate: [],
    lastModifiedBy: [null, [Validators.maxLength(50)]],
    lastModifiedDate: [],
    tipoRelatorio: [],
    tipoOperacao: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected lembreteService: LembreteService,
    protected tipoRelatorioService: TipoRelatorioService,
    protected tipoOperacaoService: TipoOperacaoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lembrete }) => {
      if (lembrete.id === undefined) {
        const today = dayjs().startOf('day');
        lembrete.createdDate = today;
        lembrete.lastModifiedDate = today;
      }

      this.updateForm(lembrete);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('controleOperacionalApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lembrete = this.createFromForm();
    if (lembrete.id !== undefined) {
      this.subscribeToSaveResponse(this.lembreteService.update(lembrete));
    } else {
      this.subscribeToSaveResponse(this.lembreteService.create(lembrete));
    }
  }

  trackTipoRelatorioById(_index: number, item: ITipoRelatorio): number {
    return item.id!;
  }

  trackTipoOperacaoById(_index: number, item: ITipoOperacao): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILembrete>>): void {
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

  protected updateForm(lembrete: ILembrete): void {
    this.editForm.patchValue({
      id: lembrete.id,
      nome: lembrete.nome,
      descricao: lembrete.descricao,
      createdBy: lembrete.createdBy,
      createdDate: lembrete.createdDate ? lembrete.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: lembrete.lastModifiedBy,
      lastModifiedDate: lembrete.lastModifiedDate ? lembrete.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      tipoRelatorio: lembrete.tipoRelatorio,
      tipoOperacao: lembrete.tipoOperacao,
    });

    this.tipoRelatoriosSharedCollection = this.tipoRelatorioService.addTipoRelatorioToCollectionIfMissing(
      this.tipoRelatoriosSharedCollection,
      lembrete.tipoRelatorio
    );
    this.tipoOperacaosSharedCollection = this.tipoOperacaoService.addTipoOperacaoToCollectionIfMissing(
      this.tipoOperacaosSharedCollection,
      lembrete.tipoOperacao
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoRelatorioService
      .query()
      .pipe(map((res: HttpResponse<ITipoRelatorio[]>) => res.body ?? []))
      .pipe(
        map((tipoRelatorios: ITipoRelatorio[]) =>
          this.tipoRelatorioService.addTipoRelatorioToCollectionIfMissing(tipoRelatorios, this.editForm.get('tipoRelatorio')!.value)
        )
      )
      .subscribe((tipoRelatorios: ITipoRelatorio[]) => (this.tipoRelatoriosSharedCollection = tipoRelatorios));

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

  protected createFromForm(): ILembrete {
    return {
      ...new Lembrete(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? dayjs(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? dayjs(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tipoRelatorio: this.editForm.get(['tipoRelatorio'])!.value,
      tipoOperacao: this.editForm.get(['tipoOperacao'])!.value,
    };
  }
}
