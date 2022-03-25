import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAmostra, getAmostraIdentifier } from '../amostra.model';

export type EntityResponseType = HttpResponse<IAmostra>;
export type EntityArrayResponseType = HttpResponse<IAmostra[]>;

@Injectable({ providedIn: 'root' })
export class AmostraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amostras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(amostra: IAmostra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amostra);
    return this.http
      .post<IAmostra>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(amostra: IAmostra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amostra);
    return this.http
      .put<IAmostra>(`${this.resourceUrl}/${getAmostraIdentifier(amostra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(amostra: IAmostra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amostra);
    return this.http
      .patch<IAmostra>(`${this.resourceUrl}/${getAmostraIdentifier(amostra) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAmostra>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmostra[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAmostraToCollectionIfMissing(amostraCollection: IAmostra[], ...amostrasToCheck: (IAmostra | null | undefined)[]): IAmostra[] {
    const amostras: IAmostra[] = amostrasToCheck.filter(isPresent);
    if (amostras.length > 0) {
      const amostraCollectionIdentifiers = amostraCollection.map(amostraItem => getAmostraIdentifier(amostraItem)!);
      const amostrasToAdd = amostras.filter(amostraItem => {
        const amostraIdentifier = getAmostraIdentifier(amostraItem);
        if (amostraIdentifier == null || amostraCollectionIdentifiers.includes(amostraIdentifier)) {
          return false;
        }
        amostraCollectionIdentifiers.push(amostraIdentifier);
        return true;
      });
      return [...amostrasToAdd, ...amostraCollection];
    }
    return amostraCollection;
  }

  protected convertDateFromClient(amostra: IAmostra): IAmostra {
    return Object.assign({}, amostra, {
      dataHoraColeta: amostra.dataHoraColeta?.isValid() ? amostra.dataHoraColeta.toJSON() : undefined,
      recebimentoNoLaboratorio: amostra.recebimentoNoLaboratorio?.isValid() ? amostra.recebimentoNoLaboratorio.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataHoraColeta = res.body.dataHoraColeta ? dayjs(res.body.dataHoraColeta) : undefined;
      res.body.recebimentoNoLaboratorio = res.body.recebimentoNoLaboratorio ? dayjs(res.body.recebimentoNoLaboratorio) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((amostra: IAmostra) => {
        amostra.dataHoraColeta = amostra.dataHoraColeta ? dayjs(amostra.dataHoraColeta) : undefined;
        amostra.recebimentoNoLaboratorio = amostra.recebimentoNoLaboratorio ? dayjs(amostra.recebimentoNoLaboratorio) : undefined;
      });
    }
    return res;
  }
}
