import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrigemAmostra, getOrigemAmostraIdentifier } from '../origem-amostra.model';

export type EntityResponseType = HttpResponse<IOrigemAmostra>;
export type EntityArrayResponseType = HttpResponse<IOrigemAmostra[]>;

@Injectable({ providedIn: 'root' })
export class OrigemAmostraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/origem-amostras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(origemAmostra: IOrigemAmostra): Observable<EntityResponseType> {
    return this.http.post<IOrigemAmostra>(this.resourceUrl, origemAmostra, { observe: 'response' });
  }

  update(origemAmostra: IOrigemAmostra): Observable<EntityResponseType> {
    return this.http.put<IOrigemAmostra>(`${this.resourceUrl}/${getOrigemAmostraIdentifier(origemAmostra) as number}`, origemAmostra, {
      observe: 'response',
    });
  }

  partialUpdate(origemAmostra: IOrigemAmostra): Observable<EntityResponseType> {
    return this.http.patch<IOrigemAmostra>(`${this.resourceUrl}/${getOrigemAmostraIdentifier(origemAmostra) as number}`, origemAmostra, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrigemAmostra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrigemAmostra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrigemAmostraToCollectionIfMissing(
    origemAmostraCollection: IOrigemAmostra[],
    ...origemAmostrasToCheck: (IOrigemAmostra | null | undefined)[]
  ): IOrigemAmostra[] {
    const origemAmostras: IOrigemAmostra[] = origemAmostrasToCheck.filter(isPresent);
    if (origemAmostras.length > 0) {
      const origemAmostraCollectionIdentifiers = origemAmostraCollection.map(
        origemAmostraItem => getOrigemAmostraIdentifier(origemAmostraItem)!
      );
      const origemAmostrasToAdd = origemAmostras.filter(origemAmostraItem => {
        const origemAmostraIdentifier = getOrigemAmostraIdentifier(origemAmostraItem);
        if (origemAmostraIdentifier == null || origemAmostraCollectionIdentifiers.includes(origemAmostraIdentifier)) {
          return false;
        }
        origemAmostraCollectionIdentifiers.push(origemAmostraIdentifier);
        return true;
      });
      return [...origemAmostrasToAdd, ...origemAmostraCollection];
    }
    return origemAmostraCollection;
  }
}
