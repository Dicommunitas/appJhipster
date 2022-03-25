import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';

export interface ILembrete {
  id?: number;
  nome?: string;
  descricao?: string;
  tipoRelatorio?: ITipoRelatorio | null;
  tipoOperacao?: ITipoOperacao | null;
}

export class Lembrete implements ILembrete {
  constructor(
    public id?: number,
    public nome?: string,
    public descricao?: string,
    public tipoRelatorio?: ITipoRelatorio | null,
    public tipoOperacao?: ITipoOperacao | null
  ) {}
}

export function getLembreteIdentifier(lembrete: ILembrete): number | undefined {
  return lembrete.id;
}
