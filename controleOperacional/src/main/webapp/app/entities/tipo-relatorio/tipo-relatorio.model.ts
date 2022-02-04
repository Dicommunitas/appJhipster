import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface ITipoRelatorio {
  id?: number;
  nome?: string;
  usuariosAuts?: IUsuario[] | null;
}

export class TipoRelatorio implements ITipoRelatorio {
  constructor(public id?: number, public nome?: string, public usuariosAuts?: IUsuario[] | null) {}
}

export function getTipoRelatorioIdentifier(tipoRelatorio: ITipoRelatorio): number | undefined {
  return tipoRelatorio.id;
}
