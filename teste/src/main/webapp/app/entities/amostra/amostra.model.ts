import dayjs from 'dayjs/esm';
import { IFinalidadeAmostra } from 'app/entities/finalidade-amostra/finalidade-amostra.model';
import { IOperacao } from 'app/entities/operacao/operacao.model';
import { IOrigemAmostra } from 'app/entities/origem-amostra/origem-amostra.model';
import { ITipoAmostra } from 'app/entities/tipo-amostra/tipo-amostra.model';
import { IUser } from 'app/entities/user/user.model';

export interface IAmostra {
  id?: number;
  dataHoraColeta?: dayjs.Dayjs | null;
  descricaoDeOrigen?: string | null;
  observacao?: string | null;
  identificadorExterno?: string | null;
  recebimentoNoLaboratorio?: dayjs.Dayjs | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  finalidades?: IFinalidadeAmostra[] | null;
  operacao?: IOperacao;
  origemAmostra?: IOrigemAmostra;
  tipoAmostra?: ITipoAmostra;
  amostradaPor?: IUser | null;
  recebidaPor?: IUser | null;
}

export class Amostra implements IAmostra {
  constructor(
    public id?: number,
    public dataHoraColeta?: dayjs.Dayjs | null,
    public descricaoDeOrigen?: string | null,
    public observacao?: string | null,
    public identificadorExterno?: string | null,
    public recebimentoNoLaboratorio?: dayjs.Dayjs | null,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public finalidades?: IFinalidadeAmostra[] | null,
    public operacao?: IOperacao,
    public origemAmostra?: IOrigemAmostra,
    public tipoAmostra?: ITipoAmostra,
    public amostradaPor?: IUser | null,
    public recebidaPor?: IUser | null
  ) {}
}

export function getAmostraIdentifier(amostra: IAmostra): number | undefined {
  return amostra.id;
}
