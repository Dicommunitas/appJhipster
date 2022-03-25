import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinalidadeAmostra, getFinalidadeAmostraIdentifier } from '../finalidade-amostra.model';

export type EntityResponseType = HttpResponse<IFinalidadeAmostra>;
export type EntityArrayResponseType = HttpResponse<IFinalidadeAmostra[]>;

@Injectable({ providedIn: 'root' })
export class FinalidadeAmostraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/finalidade-amostras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(finalidadeAmostra: IFinalidadeAmostra): Observable<EntityResponseType> {
    return this.http.post<IFinalidadeAmostra>(this.resourceUrl, finalidadeAmostra, { observe: 'response' });
  }

  update(finalidadeAmostra: IFinalidadeAmostra): Observable<EntityResponseType> {
    return this.http.put<IFinalidadeAmostra>(
      `${this.resourceUrl}/${getFinalidadeAmostraIdentifier(finalidadeAmostra) as number}`,
      finalidadeAmostra,
      { observe: 'response' }
    );
  }

  partialUpdate(finalidadeAmostra: IFinalidadeAmostra): Observable<EntityResponseType> {
    return this.http.patch<IFinalidadeAmostra>(
      `${this.resourceUrl}/${getFinalidadeAmostraIdentifier(finalidadeAmostra) as number}`,
      finalidadeAmostra,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFinalidadeAmostra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFinalidadeAmostra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFinalidadeAmostraToCollectionIfMissing(
    finalidadeAmostraCollection: IFinalidadeAmostra[],
    ...finalidadeAmostrasToCheck: (IFinalidadeAmostra | null | undefined)[]
  ): IFinalidadeAmostra[] {
    const finalidadeAmostras: IFinalidadeAmostra[] = finalidadeAmostrasToCheck.filter(isPresent);
    if (finalidadeAmostras.length > 0) {
      const finalidadeAmostraCollectionIdentifiers = finalidadeAmostraCollection.map(
        finalidadeAmostraItem => getFinalidadeAmostraIdentifier(finalidadeAmostraItem)!
      );
      const finalidadeAmostrasToAdd = finalidadeAmostras.filter(finalidadeAmostraItem => {
        const finalidadeAmostraIdentifier = getFinalidadeAmostraIdentifier(finalidadeAmostraItem);
        if (finalidadeAmostraIdentifier == null || finalidadeAmostraCollectionIdentifiers.includes(finalidadeAmostraIdentifier)) {
          return false;
        }
        finalidadeAmostraCollectionIdentifiers.push(finalidadeAmostraIdentifier);
        return true;
      });
      return [...finalidadeAmostrasToAdd, ...finalidadeAmostraCollection];
    }
    return finalidadeAmostraCollection;
  }
}
