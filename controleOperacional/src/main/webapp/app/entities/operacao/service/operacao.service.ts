import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperacao, getOperacaoIdentifier } from '../operacao.model';

export type EntityResponseType = HttpResponse<IOperacao>;
export type EntityArrayResponseType = HttpResponse<IOperacao[]>;

@Injectable({ providedIn: 'root' })
export class OperacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operacaos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operacao: IOperacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacao);
    return this.http
      .post<IOperacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(operacao: IOperacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacao);
    return this.http
      .put<IOperacao>(`${this.resourceUrl}/${getOperacaoIdentifier(operacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(operacao: IOperacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operacao);
    return this.http
      .patch<IOperacao>(`${this.resourceUrl}/${getOperacaoIdentifier(operacao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOperacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOperacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOperacaoToCollectionIfMissing(operacaoCollection: IOperacao[], ...operacaosToCheck: (IOperacao | null | undefined)[]): IOperacao[] {
    const operacaos: IOperacao[] = operacaosToCheck.filter(isPresent);
    if (operacaos.length > 0) {
      const operacaoCollectionIdentifiers = operacaoCollection.map(operacaoItem => getOperacaoIdentifier(operacaoItem)!);
      const operacaosToAdd = operacaos.filter(operacaoItem => {
        const operacaoIdentifier = getOperacaoIdentifier(operacaoItem);
        if (operacaoIdentifier == null || operacaoCollectionIdentifiers.includes(operacaoIdentifier)) {
          return false;
        }
        operacaoCollectionIdentifiers.push(operacaoIdentifier);
        return true;
      });
      return [...operacaosToAdd, ...operacaoCollection];
    }
    return operacaoCollection;
  }

  protected convertDateFromClient(operacao: IOperacao): IOperacao {
    return Object.assign({}, operacao, {
      inicio: operacao.inicio?.isValid() ? operacao.inicio.toJSON() : undefined,
      fim: operacao.fim?.isValid() ? operacao.fim.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inicio = res.body.inicio ? dayjs(res.body.inicio) : undefined;
      res.body.fim = res.body.fim ? dayjs(res.body.fim) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((operacao: IOperacao) => {
        operacao.inicio = operacao.inicio ? dayjs(operacao.inicio) : undefined;
        operacao.fim = operacao.fim ? dayjs(operacao.fim) : undefined;
      });
    }
    return res;
  }
}
