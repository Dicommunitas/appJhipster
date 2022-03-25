import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoOperacao, getTipoOperacaoIdentifier } from '../tipo-operacao.model';

export type EntityResponseType = HttpResponse<ITipoOperacao>;
export type EntityArrayResponseType = HttpResponse<ITipoOperacao[]>;

@Injectable({ providedIn: 'root' })
export class TipoOperacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-operacaos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoOperacao: ITipoOperacao): Observable<EntityResponseType> {
    return this.http.post<ITipoOperacao>(this.resourceUrl, tipoOperacao, { observe: 'response' });
  }

  update(tipoOperacao: ITipoOperacao): Observable<EntityResponseType> {
    return this.http.put<ITipoOperacao>(`${this.resourceUrl}/${getTipoOperacaoIdentifier(tipoOperacao) as number}`, tipoOperacao, {
      observe: 'response',
    });
  }

  partialUpdate(tipoOperacao: ITipoOperacao): Observable<EntityResponseType> {
    return this.http.patch<ITipoOperacao>(`${this.resourceUrl}/${getTipoOperacaoIdentifier(tipoOperacao) as number}`, tipoOperacao, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoOperacao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoOperacao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoOperacaoToCollectionIfMissing(
    tipoOperacaoCollection: ITipoOperacao[],
    ...tipoOperacaosToCheck: (ITipoOperacao | null | undefined)[]
  ): ITipoOperacao[] {
    const tipoOperacaos: ITipoOperacao[] = tipoOperacaosToCheck.filter(isPresent);
    if (tipoOperacaos.length > 0) {
      const tipoOperacaoCollectionIdentifiers = tipoOperacaoCollection.map(
        tipoOperacaoItem => getTipoOperacaoIdentifier(tipoOperacaoItem)!
      );
      const tipoOperacaosToAdd = tipoOperacaos.filter(tipoOperacaoItem => {
        const tipoOperacaoIdentifier = getTipoOperacaoIdentifier(tipoOperacaoItem);
        if (tipoOperacaoIdentifier == null || tipoOperacaoCollectionIdentifiers.includes(tipoOperacaoIdentifier)) {
          return false;
        }
        tipoOperacaoCollectionIdentifiers.push(tipoOperacaoIdentifier);
        return true;
      });
      return [...tipoOperacaosToAdd, ...tipoOperacaoCollection];
    }
    return tipoOperacaoCollection;
  }
}
