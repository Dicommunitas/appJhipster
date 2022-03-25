import dayjs from 'dayjs/esm';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';

export interface IOperacao {
  id?: number;
  descricao?: string;
  volumePeso?: number;
  inicio?: dayjs.Dayjs | null;
  fim?: dayjs.Dayjs | null;
  quantidadeAmostras?: number;
  observacao?: string | null;
  tipoOperacao?: ITipoOperacao;
}

export class Operacao implements IOperacao {
  constructor(
    public id?: number,
    public descricao?: string,
    public volumePeso?: number,
    public inicio?: dayjs.Dayjs | null,
    public fim?: dayjs.Dayjs | null,
    public quantidadeAmostras?: number,
    public observacao?: string | null,
    public tipoOperacao?: ITipoOperacao
  ) {}
}

export function getOperacaoIdentifier(operacao: IOperacao): number | undefined {
  return operacao.id;
}
