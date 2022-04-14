import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoFinalidadeAmostra, getTipoFinalidadeAmostraIdentifier } from '../tipo-finalidade-amostra.model';

export type EntityResponseType = HttpResponse<ITipoFinalidadeAmostra>;
export type EntityArrayResponseType = HttpResponse<ITipoFinalidadeAmostra[]>;

@Injectable({ providedIn: 'root' })
export class TipoFinalidadeAmostraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-finalidade-amostras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoFinalidadeAmostra: ITipoFinalidadeAmostra): Observable<EntityResponseType> {
    return this.http.post<ITipoFinalidadeAmostra>(this.resourceUrl, tipoFinalidadeAmostra, { observe: 'response' });
  }

  update(tipoFinalidadeAmostra: ITipoFinalidadeAmostra): Observable<EntityResponseType> {
    return this.http.put<ITipoFinalidadeAmostra>(
      `${this.resourceUrl}/${getTipoFinalidadeAmostraIdentifier(tipoFinalidadeAmostra) as number}`,
      tipoFinalidadeAmostra,
      { observe: 'response' }
    );
  }

  partialUpdate(tipoFinalidadeAmostra: ITipoFinalidadeAmostra): Observable<EntityResponseType> {
    return this.http.patch<ITipoFinalidadeAmostra>(
      `${this.resourceUrl}/${getTipoFinalidadeAmostraIdentifier(tipoFinalidadeAmostra) as number}`,
      tipoFinalidadeAmostra,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoFinalidadeAmostra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoFinalidadeAmostra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoFinalidadeAmostraToCollectionIfMissing(
    tipoFinalidadeAmostraCollection: ITipoFinalidadeAmostra[],
    ...tipoFinalidadeAmostrasToCheck: (ITipoFinalidadeAmostra | null | undefined)[]
  ): ITipoFinalidadeAmostra[] {
    const tipoFinalidadeAmostras: ITipoFinalidadeAmostra[] = tipoFinalidadeAmostrasToCheck.filter(isPresent);
    if (tipoFinalidadeAmostras.length > 0) {
      const tipoFinalidadeAmostraCollectionIdentifiers = tipoFinalidadeAmostraCollection.map(
        tipoFinalidadeAmostraItem => getTipoFinalidadeAmostraIdentifier(tipoFinalidadeAmostraItem)!
      );
      const tipoFinalidadeAmostrasToAdd = tipoFinalidadeAmostras.filter(tipoFinalidadeAmostraItem => {
        const tipoFinalidadeAmostraIdentifier = getTipoFinalidadeAmostraIdentifier(tipoFinalidadeAmostraItem);
        if (
          tipoFinalidadeAmostraIdentifier == null ||
          tipoFinalidadeAmostraCollectionIdentifiers.includes(tipoFinalidadeAmostraIdentifier)
        ) {
          return false;
        }
        tipoFinalidadeAmostraCollectionIdentifiers.push(tipoFinalidadeAmostraIdentifier);
        return true;
      });
      return [...tipoFinalidadeAmostrasToAdd, ...tipoFinalidadeAmostraCollection];
    }
    return tipoFinalidadeAmostraCollection;
  }
}
