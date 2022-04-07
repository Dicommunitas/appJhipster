export interface ITipoFinalidadeAmostra {
  id?: number;
  descricao?: string;
  obrigatorioLacre?: boolean;
}

export class TipoFinalidadeAmostra implements ITipoFinalidadeAmostra {
  constructor(public id?: number, public descricao?: string, public obrigatorioLacre?: boolean) {
    this.obrigatorioLacre = this.obrigatorioLacre ?? false;
  }
}

export function getTipoFinalidadeAmostraIdentifier(tipoFinalidadeAmostra: ITipoFinalidadeAmostra): number | undefined {
  return tipoFinalidadeAmostra.id;
}
