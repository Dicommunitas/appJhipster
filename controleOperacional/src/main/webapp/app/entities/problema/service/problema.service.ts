import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProblema, getProblemaIdentifier } from '../problema.model';

export type EntityResponseType = HttpResponse<IProblema>;
export type EntityArrayResponseType = HttpResponse<IProblema[]>;

@Injectable({ providedIn: 'root' })
export class ProblemaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/problemas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(problema: IProblema): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(problema);
    return this.http
      .post<IProblema>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(problema: IProblema): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(problema);
    return this.http
      .put<IProblema>(`${this.resourceUrl}/${getProblemaIdentifier(problema) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(problema: IProblema): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(problema);
    return this.http
      .patch<IProblema>(`${this.resourceUrl}/${getProblemaIdentifier(problema) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProblema>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProblema[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProblemaToCollectionIfMissing(problemaCollection: IProblema[], ...problemasToCheck: (IProblema | null | undefined)[]): IProblema[] {
    const problemas: IProblema[] = problemasToCheck.filter(isPresent);
    if (problemas.length > 0) {
      const problemaCollectionIdentifiers = problemaCollection.map(problemaItem => getProblemaIdentifier(problemaItem)!);
      const problemasToAdd = problemas.filter(problemaItem => {
        const problemaIdentifier = getProblemaIdentifier(problemaItem);
        if (problemaIdentifier == null || problemaCollectionIdentifiers.includes(problemaIdentifier)) {
          return false;
        }
        problemaCollectionIdentifiers.push(problemaIdentifier);
        return true;
      });
      return [...problemasToAdd, ...problemaCollection];
    }
    return problemaCollection;
  }

  protected convertDateFromClient(problema: IProblema): IProblema {
    return Object.assign({}, problema, {
      dataVerificacao: problema.dataVerificacao?.isValid() ? problema.dataVerificacao.format(DATE_FORMAT) : undefined,
      dataFinalizacao: problema.dataFinalizacao?.isValid() ? problema.dataFinalizacao.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataVerificacao = res.body.dataVerificacao ? dayjs(res.body.dataVerificacao) : undefined;
      res.body.dataFinalizacao = res.body.dataFinalizacao ? dayjs(res.body.dataFinalizacao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((problema: IProblema) => {
        problema.dataVerificacao = problema.dataVerificacao ? dayjs(problema.dataVerificacao) : undefined;
        problema.dataFinalizacao = problema.dataFinalizacao ? dayjs(problema.dataFinalizacao) : undefined;
      });
    }
    return res;
  }
}
