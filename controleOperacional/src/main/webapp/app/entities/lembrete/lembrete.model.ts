import * as dayjs from 'dayjs';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';

export interface ILembrete {
  id?: number;
  data?: dayjs.Dayjs;
  nome?: string;
  texto?: string;
  tipoRelatorio?: ITipoRelatorio;
  tipoOperacao?: ITipoOperacao | null;
}

export class Lembrete implements ILembrete {
  constructor(
    public id?: number,
    public data?: dayjs.Dayjs,
    public nome?: string,
    public texto?: string,
    public tipoRelatorio?: ITipoRelatorio,
    public tipoOperacao?: ITipoOperacao | null
  ) {}
}

export function getLembreteIdentifier(lembrete: ILembrete): number | undefined {
  return lembrete.id;
}
