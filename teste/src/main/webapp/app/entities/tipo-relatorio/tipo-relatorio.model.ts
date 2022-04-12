import { IUser } from 'app/entities/user/user.model';

export interface ITipoRelatorio {
  id?: number;
  nome?: string;
  usuariosAuts?: IUser[] | null;
}

export class TipoRelatorio implements ITipoRelatorio {
  constructor(public id?: number, public nome?: string, public usuariosAuts?: IUser[] | null) {}
}

export function getTipoRelatorioIdentifier(tipoRelatorio: ITipoRelatorio): number | undefined {
  return tipoRelatorio.id;
}
