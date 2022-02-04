export interface IOrigemAmostra {
  id?: number;
  descricao?: string;
  emUso?: boolean;
}

export class OrigemAmostra implements IOrigemAmostra {
  constructor(public id?: number, public descricao?: string, public emUso?: boolean) {
    this.emUso = this.emUso ?? false;
  }
}

export function getOrigemAmostraIdentifier(origemAmostra: IOrigemAmostra): number | undefined {
  return origemAmostra.id;
}
