import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStatus, Status } from '../status.model';
import { StatusService } from '../service/status.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProblema } from 'app/entities/problema/problema.model';
import { ProblemaService } from 'app/entities/problema/service/problema.service';

@Component({
  selector: 'jhi-status-update',
  templateUrl: './status-update.component.html',
})
export class StatusUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  problemasSharedCollection: IProblema[] = [];

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
    prazo: [null, [Validators.required]],
    dataResolucao: [],
    relator: [null, Validators.required],
    responsavel: [null, Validators.required],
    problema: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected statusService: StatusService,
    protected userService: UserService,
    protected problemaService: ProblemaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ status }) => {
      this.updateForm(status);

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
    const status = this.createFromForm();
    if (status.id !== undefined) {
      this.subscribeToSaveResponse(this.statusService.update(status));
    } else {
      this.subscribeToSaveResponse(this.statusService.create(status));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackProblemaById(index: number, item: IProblema): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatus>>): void {
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

  protected updateForm(status: IStatus): void {
    this.editForm.patchValue({
      id: status.id,
      descricao: status.descricao,
      prazo: status.prazo,
      dataResolucao: status.dataResolucao,
      relator: status.relator,
      responsavel: status.responsavel,
      problema: status.problema,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      status.relator,
      status.responsavel
    );
    this.problemasSharedCollection = this.problemaService.addProblemaToCollectionIfMissing(this.problemasSharedCollection, status.problema);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('relator')!.value, this.editForm.get('responsavel')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.problemaService
      .query()
      .pipe(map((res: HttpResponse<IProblema[]>) => res.body ?? []))
      .pipe(
        map((problemas: IProblema[]) =>
          this.problemaService.addProblemaToCollectionIfMissing(problemas, this.editForm.get('problema')!.value)
        )
      )
      .subscribe((problemas: IProblema[]) => (this.problemasSharedCollection = problemas));
  }

  protected createFromForm(): IStatus {
    return {
      ...new Status(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      prazo: this.editForm.get(['prazo'])!.value,
      dataResolucao: this.editForm.get(['dataResolucao'])!.value,
      relator: this.editForm.get(['relator'])!.value,
      responsavel: this.editForm.get(['responsavel'])!.value,
      problema: this.editForm.get(['problema'])!.value,
    };
  }
}
