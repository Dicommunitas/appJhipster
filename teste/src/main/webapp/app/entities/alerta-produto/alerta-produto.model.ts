import { IProduto } from 'app/entities/produto/produto.model';

export interface IAlertaProduto {
  id?: number;
  descricao?: string;
  produtos?: IProduto[] | null;
}

export class AlertaProduto implements IAlertaProduto {
  constructor(public id?: number, public descricao?: string, public produtos?: IProduto[] | null) {}
}

export function getAlertaProdutoIdentifier(alertaProduto: IAlertaProduto): number | undefined {
  return alertaProduto.id;
}
