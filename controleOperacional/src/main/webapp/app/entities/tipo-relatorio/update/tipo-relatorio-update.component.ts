import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITipoRelatorio, TipoRelatorio } from '../tipo-relatorio.model';
import { TipoRelatorioService } from '../service/tipo-relatorio.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-tipo-relatorio-update',
  templateUrl: './tipo-relatorio-update.component.html',
})
export class TipoRelatorioUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    usuariosAuts: [],
  });

  constructor(
    protected tipoRelatorioService: TipoRelatorioService,
    protected usuarioService: UsuarioService,
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

  trackUsuarioById(index: number, item: IUsuario): number {
    return item.id!;
  }

  getSelectedUsuario(option: IUsuario, selectedVals?: IUsuario[]): IUsuario {
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

  protected updateForm(tipoRelatorio: ITipoRelatorio): void {
    this.editForm.patchValue({
      id: tipoRelatorio.id,
      nome: tipoRelatorio.nome,
      usuariosAuts: tipoRelatorio.usuariosAuts,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      ...(tipoRelatorio.usuariosAuts ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, ...(this.editForm.get('usuariosAuts')!.value ?? []))
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
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
