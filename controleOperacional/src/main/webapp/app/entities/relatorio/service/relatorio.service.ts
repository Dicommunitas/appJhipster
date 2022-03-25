import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRelatorio, getRelatorioIdentifier } from '../relatorio.model';

export type EntityResponseType = HttpResponse<IRelatorio>;
export type EntityArrayResponseType = HttpResponse<IRelatorio[]>;

@Injectable({ providedIn: 'root' })
export class RelatorioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/relatorios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(relatorio: IRelatorio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(relatorio);
    return this.http
      .post<IRelatorio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(relatorio: IRelatorio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(relatorio);
    return this.http
      .put<IRelatorio>(`${this.resourceUrl}/${getRelatorioIdentifier(relatorio) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(relatorio: IRelatorio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(relatorio);
    return this.http
      .patch<IRelatorio>(`${this.resourceUrl}/${getRelatorioIdentifier(relatorio) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRelatorio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRelatorio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRelatorioToCollectionIfMissing(
    relatorioCollection: IRelatorio[],
    ...relatoriosToCheck: (IRelatorio | null | undefined)[]
  ): IRelatorio[] {
    const relatorios: IRelatorio[] = relatoriosToCheck.filter(isPresent);
    if (relatorios.length > 0) {
      const relatorioCollectionIdentifiers = relatorioCollection.map(relatorioItem => getRelatorioIdentifier(relatorioItem)!);
      const relatoriosToAdd = relatorios.filter(relatorioItem => {
        const relatorioIdentifier = getRelatorioIdentifier(relatorioItem);
        if (relatorioIdentifier == null || relatorioCollectionIdentifiers.includes(relatorioIdentifier)) {
          return false;
        }
        relatorioCollectionIdentifiers.push(relatorioIdentifier);
        return true;
      });
      return [...relatoriosToAdd, ...relatorioCollection];
    }
    return relatorioCollection;
  }

  protected convertDateFromClient(relatorio: IRelatorio): IRelatorio {
    return Object.assign({}, relatorio, {
      dataHora: relatorio.dataHora?.isValid() ? relatorio.dataHora.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataHora = res.body.dataHora ? dayjs(res.body.dataHora) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((relatorio: IRelatorio) => {
        relatorio.dataHora = relatorio.dataHora ? dayjs(relatorio.dataHora) : undefined;
      });
    }
    return res;
  }
}
