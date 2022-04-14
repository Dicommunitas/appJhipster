import dayjs from 'dayjs/esm';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';

export interface ILembrete {
  id?: number;
  nome?: string;
  descricao?: string;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  tipoRelatorio?: ITipoRelatorio | null;
  tipoOperacao?: ITipoOperacao | null;
}

export class Lembrete implements ILembrete {
  constructor(
    public id?: number,
    public nome?: string,
    public descricao?: string,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public tipoRelatorio?: ITipoRelatorio | null,
    public tipoOperacao?: ITipoOperacao | null
  ) {}
}

export function getLembreteIdentifier(lembrete: ILembrete): number | undefined {
  return lembrete.id;
}
