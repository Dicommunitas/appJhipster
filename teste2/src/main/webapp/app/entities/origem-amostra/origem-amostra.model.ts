export interface IOrigemAmostra {
  id?: number;
  descricao?: string;
  obrigatoriaDescricao?: boolean | null;
}

export class OrigemAmostra implements IOrigemAmostra {
  constructor(public id?: number, public descricao?: string, public obrigatoriaDescricao?: boolean | null) {
    this.obrigatoriaDescricao = this.obrigatoriaDescricao ?? false;
  }
}

export function getOrigemAmostraIdentifier(origemAmostra: IOrigemAmostra): number | undefined {
  return origemAmostra.id;
}
