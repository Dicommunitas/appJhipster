import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProblema, Problema } from '../problema.model';
import { ProblemaService } from '../service/problema.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Criticidade } from 'app/entities/enumerations/criticidade.model';

@Component({
  selector: 'jhi-problema-update',
  templateUrl: './problema-update.component.html',
})
export class ProblemaUpdateComponent implements OnInit {
  isSaving = false;
  criticidadeValues = Object.keys(Criticidade);

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    dataVerificacao: [null, [Validators.required]],
    descricao: [null, [Validators.required]],
    criticidade: [null, [Validators.required]],
    impacto: [null, [Validators.required]],
    dataFinalizacao: [],
    foto: [null, []],
    fotoContentType: [],
    relator: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected problemaService: ProblemaService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ problema }) => {
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

  trackUserById(index: number, item: IUser): number {
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
      dataVerificacao: problema.dataVerificacao,
      descricao: problema.descricao,
      criticidade: problema.criticidade,
      impacto: problema.impacto,
      dataFinalizacao: problema.dataFinalizacao,
      foto: problema.foto,
      fotoContentType: problema.fotoContentType,
      relator: problema.relator,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, problema.relator);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('relator')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IProblema {
    return {
      ...new Problema(),
      id: this.editForm.get(['id'])!.value,
      dataVerificacao: this.editForm.get(['dataVerificacao'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      criticidade: this.editForm.get(['criticidade'])!.value,
      impacto: this.editForm.get(['impacto'])!.value,
      dataFinalizacao: this.editForm.get(['dataFinalizacao'])!.value,
      fotoContentType: this.editForm.get(['fotoContentType'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      relator: this.editForm.get(['relator'])!.value,
    };
  }
}
