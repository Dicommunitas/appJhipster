import * as dayjs from 'dayjs';
import { IStatus } from 'app/entities/status/status.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { Criticidade } from 'app/entities/enumerations/criticidade.model';

export interface IProblema {
  id?: number;
  data?: dayjs.Dayjs;
  descricao?: string;
  criticidade?: Criticidade;
  aceitarFinalizacao?: boolean | null;
  fotoContentType?: string | null;
  foto?: string | null;
  impacto?: string;
  statuses?: IStatus[] | null;
  relator?: IUsuario;
}

export class Problema implements IProblema {
  constructor(
    public id?: number,
    public data?: dayjs.Dayjs,
    public descricao?: string,
    public criticidade?: Criticidade,
    public aceitarFinalizacao?: boolean | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public impacto?: string,
    public statuses?: IStatus[] | null,
    public relator?: IUsuario
  ) {
    this.aceitarFinalizacao = this.aceitarFinalizacao ?? false;
  }
}

export function getProblemaIdentifier(problema: IProblema): number | undefined {
  return problema.id;
}
