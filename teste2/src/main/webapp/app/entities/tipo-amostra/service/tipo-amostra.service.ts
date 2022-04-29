import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoAmostra, getTipoAmostraIdentifier } from '../tipo-amostra.model';

export type EntityResponseType = HttpResponse<ITipoAmostra>;
export type EntityArrayResponseType = HttpResponse<ITipoAmostra[]>;

@Injectable({ providedIn: 'root' })
export class TipoAmostraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-amostras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoAmostra: ITipoAmostra): Observable<EntityResponseType> {
    return this.http.post<ITipoAmostra>(this.resourceUrl, tipoAmostra, { observe: 'response' });
  }

  update(tipoAmostra: ITipoAmostra): Observable<EntityResponseType> {
    return this.http.put<ITipoAmostra>(`${this.resourceUrl}/${getTipoAmostraIdentifier(tipoAmostra) as number}`, tipoAmostra, {
      observe: 'response',
    });
  }

  partialUpdate(tipoAmostra: ITipoAmostra): Observable<EntityResponseType> {
    return this.http.patch<ITipoAmostra>(`${this.resourceUrl}/${getTipoAmostraIdentifier(tipoAmostra) as number}`, tipoAmostra, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoAmostra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoAmostra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoAmostraToCollectionIfMissing(
    tipoAmostraCollection: ITipoAmostra[],
    ...tipoAmostrasToCheck: (ITipoAmostra | null | undefined)[]
  ): ITipoAmostra[] {
    const tipoAmostras: ITipoAmostra[] = tipoAmostrasToCheck.filter(isPresent);
    if (tipoAmostras.length > 0) {
      const tipoAmostraCollectionIdentifiers = tipoAmostraCollection.map(tipoAmostraItem => getTipoAmostraIdentifier(tipoAmostraItem)!);
      const tipoAmostrasToAdd = tipoAmostras.filter(tipoAmostraItem => {
        const tipoAmostraIdentifier = getTipoAmostraIdentifier(tipoAmostraItem);
        if (tipoAmostraIdentifier == null || tipoAmostraCollectionIdentifiers.includes(tipoAmostraIdentifier)) {
          return false;
        }
        tipoAmostraCollectionIdentifiers.push(tipoAmostraIdentifier);
        return true;
      });
      return [...tipoAmostrasToAdd, ...tipoAmostraCollection];
    }
    return tipoAmostraCollection;
  }
}
