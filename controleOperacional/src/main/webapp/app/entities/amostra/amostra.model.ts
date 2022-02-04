import * as dayjs from 'dayjs';
import { IFinalidadeAmostra } from 'app/entities/finalidade-amostra/finalidade-amostra.model';
import { IOperacao } from 'app/entities/operacao/operacao.model';
import { IOrigemAmostra } from 'app/entities/origem-amostra/origem-amostra.model';
import { IProduto } from 'app/entities/produto/produto.model';
import { ITipoAmostra } from 'app/entities/tipo-amostra/tipo-amostra.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IAmostra {
  id?: number;
  dataHora?: dayjs.Dayjs | null;
  observacao?: string | null;
  identificadorExterno?: string | null;
  amostraNoLaboratorio?: boolean | null;
  finalidades?: IFinalidadeAmostra[] | null;
  operacao?: IOperacao;
  origemAmostra?: IOrigemAmostra;
  produto?: IProduto;
  tipoAmostra?: ITipoAmostra;
  amostradaPor?: IUsuario | null;
  recebidaPor?: IUsuario | null;
}

export class Amostra implements IAmostra {
  constructor(
    public id?: number,
    public dataHora?: dayjs.Dayjs | null,
    public observacao?: string | null,
    public identificadorExterno?: string | null,
    public amostraNoLaboratorio?: boolean | null,
    public finalidades?: IFinalidadeAmostra[] | null,
    public operacao?: IOperacao,
    public origemAmostra?: IOrigemAmostra,
    public produto?: IProduto,
    public tipoAmostra?: ITipoAmostra,
    public amostradaPor?: IUsuario | null,
    public recebidaPor?: IUsuario | null
  ) {
    this.amostraNoLaboratorio = this.amostraNoLaboratorio ?? false;
  }
}

export function getAmostraIdentifier(amostra: IAmostra): number | undefined {
  return amostra.id;
}
