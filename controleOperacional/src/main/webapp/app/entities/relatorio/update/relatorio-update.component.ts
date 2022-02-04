import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRelatorio, Relatorio } from '../relatorio.model';
import { RelatorioService } from '../service/relatorio.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { TipoRelatorioService } from 'app/entities/tipo-relatorio/service/tipo-relatorio.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-relatorio-update',
  templateUrl: './relatorio-update.component.html',
})
export class RelatorioUpdateComponent implements OnInit {
  isSaving = false;

  tipoRelatoriosSharedCollection: ITipoRelatorio[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    relato: [null, [Validators.required]],
    linksExternos: [],
    tipo: [],
    responsavel: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected relatorioService: RelatorioService,
    protected tipoRelatorioService: TipoRelatorioService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatorio }) => {
      this.updateForm(relatorio);

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
    const relatorio = this.createFromForm();
    if (relatorio.id !== undefined) {
      this.subscribeToSaveResponse(this.relatorioService.update(relatorio));
    } else {
      this.subscribeToSaveResponse(this.relatorioService.create(relatorio));
    }
  }

  trackTipoRelatorioById(index: number, item: ITipoRelatorio): number {
    return item.id!;
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRelatorio>>): void {
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

  protected updateForm(relatorio: IRelatorio): void {
    this.editForm.patchValue({
      id: relatorio.id,
      relato: relatorio.relato,
      linksExternos: relatorio.linksExternos,
      tipo: relatorio.tipo,
      responsavel: relatorio.responsavel,
    });

    this.tipoRelatoriosSharedCollection = this.tipoRelatorioService.addTipoRelatorioToCollectionIfMissing(
      this.tipoRelatoriosSharedCollection,
      relatorio.tipo
    );
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      relatorio.responsavel
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoRelatorioService
      .query()
      .pipe(map((res: HttpResponse<ITipoRelatorio[]>) => res.body ?? []))
      .pipe(
        map((tipoRelatorios: ITipoRelatorio[]) =>
          this.tipoRelatorioService.addTipoRelatorioToCollectionIfMissing(tipoRelatorios, this.editForm.get('tipo')!.value)
        )
      )
      .subscribe((tipoRelatorios: ITipoRelatorio[]) => (this.tipoRelatoriosSharedCollection = tipoRelatorios));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('responsavel')!.value)
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IRelatorio {
    return {
      ...new Relatorio(),
      id: this.editForm.get(['id'])!.value,
      relato: this.editForm.get(['relato'])!.value,
      linksExternos: this.editForm.get(['linksExternos'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      responsavel: this.editForm.get(['responsavel'])!.value,
    };
  }
}
