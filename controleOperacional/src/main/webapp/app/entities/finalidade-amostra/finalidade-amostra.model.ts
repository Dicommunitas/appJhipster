import { ITipoFinalidadeAmostra } from 'app/entities/tipo-finalidade-amostra/tipo-finalidade-amostra.model';
import { IAmostra } from 'app/entities/amostra/amostra.model';

export interface IFinalidadeAmostra {
  id?: number;
  lacre?: number | null;
  tipoFinalidadeAmostra?: ITipoFinalidadeAmostra;
  amostra?: IAmostra;
}

export class FinalidadeAmostra implements IFinalidadeAmostra {
  constructor(
    public id?: number,
    public lacre?: number | null,
    public tipoFinalidadeAmostra?: ITipoFinalidadeAmostra,
    public amostra?: IAmostra
  ) {}
}

export function getFinalidadeAmostraIdentifier(finalidadeAmostra: IFinalidadeAmostra): number | undefined {
  return finalidadeAmostra.id;
}
