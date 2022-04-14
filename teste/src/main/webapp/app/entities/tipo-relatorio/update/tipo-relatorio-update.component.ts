import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITipoRelatorio, TipoRelatorio } from '../tipo-relatorio.model';
import { TipoRelatorioService } from '../service/tipo-relatorio.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-tipo-relatorio-update',
  templateUrl: './tipo-relatorio-update.component.html',
})
export class TipoRelatorioUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    usuariosAuts: [],
  });

  constructor(
    protected tipoRelatorioService: TipoRelatorioService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoRelatorio }) => {
      this.updateForm(tipoRelatorio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoRelatorio = this.createFromForm();
    if (tipoRelatorio.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoRelatorioService.update(tipoRelatorio));
    } else {
      this.subscribeToSaveResponse(this.tipoRelatorioService.create(tipoRelatorio));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  getSelectedUser(option: IUser, selectedVals?: IUser[]): IUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoRelatorio>>): void {
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

  protected updateForm(tipoRelatorio: ITipoRelatorio): void {
    this.editForm.patchValue({
      id: tipoRelatorio.id,
      nome: tipoRelatorio.nome,
      usuariosAuts: tipoRelatorio.usuariosAuts,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      ...(tipoRelatorio.usuariosAuts ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, ...(this.editForm.get('usuariosAuts')!.value ?? [])))
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITipoRelatorio {
    return {
      ...new TipoRelatorio(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      usuariosAuts: this.editForm.get(['usuariosAuts'])!.value,
    };
  }
}
