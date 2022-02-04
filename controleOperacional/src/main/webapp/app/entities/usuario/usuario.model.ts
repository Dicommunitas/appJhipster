import { IUser } from 'app/entities/user/user.model';
import { ITipoRelatorio } from 'app/entities/tipo-relatorio/tipo-relatorio.model';

export interface IUsuario {
  id?: number;
  chave?: string;
  nome?: string;
  linksExternos?: string | null;
  user?: IUser;
  relAutorizados?: ITipoRelatorio[] | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public chave?: string,
    public nome?: string,
    public linksExternos?: string | null,
    public user?: IUser,
    public relAutorizados?: ITipoRelatorio[] | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
