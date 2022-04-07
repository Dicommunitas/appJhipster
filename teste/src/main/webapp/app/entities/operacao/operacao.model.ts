import dayjs from 'dayjs/esm';
import { IProduto } from 'app/entities/produto/produto.model';
import { ITipoOperacao } from 'app/entities/tipo-operacao/tipo-operacao.model';

export interface IOperacao {
  id?: number;
  descricao?: string;
  volumePeso?: number;
  inicio?: dayjs.Dayjs | null;
  fim?: dayjs.Dayjs | null;
  quantidadeAmostras?: number | null;
  observacao?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  produto?: IProduto;
  tipoOperacao?: ITipoOperacao;
}

export class Operacao implements IOperacao {
  constructor(
    public id?: number,
    public descricao?: string,
    public volumePeso?: number,
    public inicio?: dayjs.Dayjs | null,
    public fim?: dayjs.Dayjs | null,
    public quantidadeAmostras?: number | null,
    public observacao?: string | null,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public produto?: IProduto,
    public tipoOperacao?: ITipoOperacao
  ) {}
}

export function getOperacaoIdentifier(operacao: IOperacao): number | undefined {
  return operacao.id;
}
