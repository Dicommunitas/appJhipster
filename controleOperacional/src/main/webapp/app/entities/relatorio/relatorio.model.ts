import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IRelatorio {
  id?: number;
  relato?: string;
  linksExternos?: string | null;
  tipo?: ITipoRelatorio | null;
  responsavel?: IUsuario;
}

export class Relatorio implements IRelatorio {
  constructor(
    public id?: number,
    public relato?: string,
    public linksExternos?: string | null,
    public tipo?: ITipoRelatorio | null,
    public responsavel?: IUsuario
  ) {}
}

export function getRelatorioIdentifier(relatorio: IRelatorio): number | undefined {
  return relatorio.id;
}
