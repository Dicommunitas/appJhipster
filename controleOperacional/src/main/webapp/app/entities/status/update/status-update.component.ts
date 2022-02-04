import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStatus, Status } from '../status.model';
import { StatusService } from '../service/status.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IProblema } from 'app/entities/problema/problema.model';
import { ProblemaService } from 'app/entities/problema/service/problema.service';

@Component({
  selector: 'jhi-status-update',
  templateUrl: './status-update.component.html',
})
export class StatusUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  problemasSharedCollection: IProblema[] = [];

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required]],
    prazo: [null, [Validators.required]],
    resolvido: [],
    relator: [null, Validators.required],
    responsavel: [null, Validators.required],
    problema: [null, Validators.required],
  });

  constructor(
    protected statusService: StatusService,
    protected usuarioService: UsuarioService,
    protected problemaService: ProblemaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ status }) => {
      if (status.id === undefined) {
        const today = dayjs().startOf('day');
        status.prazo = today;
      }

      this.updateForm(status);

      this.loadRelationshipsOptions();
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

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  trackProblemaById(index: number, item: IProblema): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatus>>): void {
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

  protected updateForm(status: IStatus): void {
    this.editForm.patchValue({
      id: status.id,
      descricao: status.descricao,
      prazo: status.prazo ? status.prazo.format(DATE_TIME_FORMAT) : null,
      resolvido: status.resolvido,
      relator: status.relator,
      responsavel: status.responsavel,
      problema: status.problema,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      status.relator,
      status.responsavel
    );
    this.problemasSharedCollection = this.problemaService.addProblemaToCollectionIfMissing(this.problemasSharedCollection, status.problema);
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(
            usuarios,
            this.editForm.get('relator')!.value,
            this.editForm.get('responsavel')!.value
          )
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

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
      prazo: this.editForm.get(['prazo'])!.value ? dayjs(this.editForm.get(['prazo'])!.value, DATE_TIME_FORMAT) : undefined,
      resolvido: this.editForm.get(['resolvido'])!.value,
      relator: this.editForm.get(['relator'])!.value,
      responsavel: this.editForm.get(['responsavel'])!.value,
      problema: this.editForm.get(['problema'])!.value,
    };
  }
}
