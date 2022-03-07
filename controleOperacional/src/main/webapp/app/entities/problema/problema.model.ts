import * as dayjs from 'dayjs';
import { IStatus } from 'app/entities/status/status.model';
import { IUser } from 'app/entities/user/user.model';
import { Criticidade } from 'app/entities/enumerations/criticidade.model';

export interface IProblema {
  id?: number;
  dataVerificacao?: dayjs.Dayjs;
  descricao?: string;
  criticidade?: Criticidade;
  impacto?: string;
  dataFinalizacao?: dayjs.Dayjs | null;
  fotoContentType?: string | null;
  foto?: string | null;
  statuses?: IStatus[] | null;
  relator?: IUser;
}

export class Problema implements IProblema {
  constructor(
    public id?: number,
    public dataVerificacao?: dayjs.Dayjs,
    public descricao?: string,
    public criticidade?: Criticidade,
    public impacto?: string,
    public dataFinalizacao?: dayjs.Dayjs | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public statuses?: IStatus[] | null,
    public relator?: IUser
  ) {}
}

export function getProblemaIdentifier(problema: IProblema): number | undefined {
  return problema.id;
}
