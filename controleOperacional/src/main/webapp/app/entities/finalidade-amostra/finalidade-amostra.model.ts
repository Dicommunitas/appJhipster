import { ITipoFinalidadeAmostra } from 'app/entities/tipo-finalidade-amostra/tipo-finalidade-amostra.model';
import { IAmostra } from 'app/entities/amostra/amostra.model';

export interface IFinalidadeAmostra {
  id?: number;
  lacre?: string | null;
  tipoFinalidadeAmostra?: ITipoFinalidadeAmostra;
  amostra?: IAmostra;
}

export class FinalidadeAmostra implements IFinalidadeAmostra {
  constructor(
    public id?: number,
    public lacre?: string | null,
    public tipoFinalidadeAmostra?: ITipoFinalidadeAmostra,
    public amostra?: IAmostra
  ) {}
}

export function getFinalidadeAmostraIdentifier(finalidadeAmostra: IFinalidadeAmostra): number | undefined {
  return finalidadeAmostra.id;
}
