import { IAlertaProduto } from 'app/entities/alerta-produto/alerta-produto.model';

export interface IProduto {
  id?: number;
  codigoBDEMQ?: string;
  nomeCurto?: string;
  nomeCompleto?: string;
  alertas?: IAlertaProduto[] | null;
}

export class Produto implements IProduto {
  constructor(
    public id?: number,
    public codigoBDEMQ?: string,
    public nomeCurto?: string,
    public nomeCompleto?: string,
    public alertas?: IAlertaProduto[] | null
  ) {}
}

export function getProdutoIdentifier(produto: IProduto): number | undefined {
  return produto.id;
}
