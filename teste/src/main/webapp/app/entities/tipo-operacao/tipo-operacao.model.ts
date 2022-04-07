export interface ITipoOperacao {
  id?: number;
  descricao?: string;
}

export class TipoOperacao implements ITipoOperacao {
  constructor(public id?: number, public descricao?: string) {}
}

export function getTipoOperacaoIdentifier(tipoOperacao: ITipoOperacao): number | undefined {
  return tipoOperacao.id;
}
