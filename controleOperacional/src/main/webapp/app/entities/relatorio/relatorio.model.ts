import * as dayjs from 'dayjs';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { IUser } from 'app/entities/user/user.model';

export interface IRelatorio {
  id?: number;
  dataHora?: dayjs.Dayjs;
  relato?: string;
  linksExternos?: string | null;
  tipo?: ITipoRelatorio | null;
  responsavel?: IUser;
}

export class Relatorio implements IRelatorio {
  constructor(
    public id?: number,
    public dataHora?: dayjs.Dayjs,
    public relato?: string,
    public linksExternos?: string | null,
    public tipo?: ITipoRelatorio | null,
    public responsavel?: IUser
  ) {}
}

export function getRelatorioIdentifier(relatorio: IRelatorio): number | undefined {
  return relatorio.id;
}
