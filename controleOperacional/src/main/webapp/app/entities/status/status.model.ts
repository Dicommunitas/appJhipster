import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProblema } from 'app/entities/problema/problema.model';

export interface IStatus {
  id?: number;
  descricao?: string;
  prazo?: dayjs.Dayjs;
  dataResolucao?: dayjs.Dayjs | null;
  relator?: IUser;
  responsavel?: IUser;
  problema?: IProblema;
}

export class Status implements IStatus {
  constructor(
    public id?: number,
    public descricao?: string,
    public prazo?: dayjs.Dayjs,
    public dataResolucao?: dayjs.Dayjs | null,
    public relator?: IUser,
    public responsavel?: IUser,
    public problema?: IProblema
  ) {}
}

export function getStatusIdentifier(status: IStatus): number | undefined {
  return status.id;
}
