import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProblema, Problema } from '../problema.model';
import { ProblemaService } from '../service/problema.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { Criticidade } from 'app/entities/enumerations/criticidade.model';

@Component({
  selector: 'jhi-problema-update',
  templateUrl: './problema-update.component.html',
})
export class ProblemaUpdateComponent implements OnInit {
  isSaving = false;
  criticidadeValues = Object.keys(Criticidade);

  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    dataZonedDateTime: [null, [Validators.required]],
    dataLocalDate: [null, [Validators.required]],
    dataInstant: [null, [Validators.required]],
    dataDuration: [null, [Validators.required]],
    descricao: [null, [Validators.required]],
    criticidade: [null, [Validators.required]],
    aceitarFinalizacao: [],
    foto: [null, []],
    fotoContentType: [],
    impacto: [null, [Validators.required]],
    relator: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected problemaService: ProblemaService,
    protected usuarioService: UsuarioService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ problema }) => {
      if (problema.id === undefined) {
        const today = dayjs().startOf('day');
        problema.dataZonedDateTime = today;
        problema.dataInstant = today;
      }

      this.updateForm(problema);

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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const problema = this.createFromForm();
    if (problema.id !== undefined) {
      this.subscribeToSaveResponse(this.problemaService.update(problema));
    } else {
      this.subscribeToSaveResponse(this.problemaService.create(problema));
    }
  }

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProblema>>): void {
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

  protected updateForm(problema: IProblema): void {
    this.editForm.patchValue({
      id: problema.id,
      dataZonedDateTime: problema.dataZonedDateTime ? problema.dataZonedDateTime.format(DATE_TIME_FORMAT) : null,
      dataLocalDate: problema.dataLocalDate,
      dataInstant: problema.dataInstant ? problema.dataInstant.format(DATE_TIME_FORMAT) : null,
      dataDuration: problema.dataDuration,
      descricao: problema.descricao,
      criticidade: problema.criticidade,
      aceitarFinalizacao: problema.aceitarFinalizacao,
      foto: problema.foto,
      fotoContentType: problema.fotoContentType,
      impacto: problema.impacto,
      relator: problema.relator,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, problema.relator);
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('relator')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IProblema {
    return {
      ...new Problema(),
      id: this.editForm.get(['id'])!.value,
      dataZonedDateTime: this.editForm.get(['dataZonedDateTime'])!.value
        ? dayjs(this.editForm.get(['dataZonedDateTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dataLocalDate: this.editForm.get(['dataLocalDate'])!.value,
      dataInstant: this.editForm.get(['dataInstant'])!.value
        ? dayjs(this.editForm.get(['dataInstant'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dataDuration: this.editForm.get(['dataDuration'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      criticidade: this.editForm.get(['criticidade'])!.value,
      aceitarFinalizacao: this.editForm.get(['aceitarFinalizacao'])!.value,
      fotoContentType: this.editForm.get(['fotoContentType'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      impacto: this.editForm.get(['impacto'])!.value,
      relator: this.editForm.get(['relator'])!.value,
    };
  }
}
