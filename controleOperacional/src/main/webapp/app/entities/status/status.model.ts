import * as dayjs from 'dayjs';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IProblema } from 'app/entities/problema/problema.model';

export interface IStatus {
  id?: number;
  descricao?: string;
  prazo?: dayjs.Dayjs;
  resolvido?: boolean | null;
  relator?: IUsuario;
  responsavel?: IUsuario;
  problema?: IProblema;
}

export class Status implements IStatus {
  constructor(
    public id?: number,
    public descricao?: string,
    public prazo?: dayjs.Dayjs,
    public resolvido?: boolean | null,
    public relator?: IUsuario,
    public responsavel?: IUsuario,
    public problema?: IProblema
  ) {
    this.resolvido = this.resolvido ?? false;
  }
}

export function getStatusIdentifier(status: IStatus): number | undefined {
  return status.id;
}
