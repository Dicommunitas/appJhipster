import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlertaProduto, getAlertaProdutoIdentifier } from '../alerta-produto.model';

export type EntityResponseType = HttpResponse<IAlertaProduto>;
export type EntityArrayResponseType = HttpResponse<IAlertaProduto[]>;

@Injectable({ providedIn: 'root' })
export class AlertaProdutoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alerta-produtos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(alertaProduto: IAlertaProduto): Observable<EntityResponseType> {
    return this.http.post<IAlertaProduto>(this.resourceUrl, alertaProduto, { observe: 'response' });
  }

  update(alertaProduto: IAlertaProduto): Observable<EntityResponseType> {
    return this.http.put<IAlertaProduto>(`${this.resourceUrl}/${getAlertaProdutoIdentifier(alertaProduto) as number}`, alertaProduto, {
      observe: 'response',
    });
  }

  partialUpdate(alertaProduto: IAlertaProduto): Observable<EntityResponseType> {
    return this.http.patch<IAlertaProduto>(`${this.resourceUrl}/${getAlertaProdutoIdentifier(alertaProduto) as number}`, alertaProduto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAlertaProduto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAlertaProduto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAlertaProdutoToCollectionIfMissing(
    alertaProdutoCollection: IAlertaProduto[],
    ...alertaProdutosToCheck: (IAlertaProduto | null | undefined)[]
  ): IAlertaProduto[] {
    const alertaProdutos: IAlertaProduto[] = alertaProdutosToCheck.filter(isPresent);
    if (alertaProdutos.length > 0) {
      const alertaProdutoCollectionIdentifiers = alertaProdutoCollection.map(
        alertaProdutoItem => getAlertaProdutoIdentifier(alertaProdutoItem)!
      );
      const alertaProdutosToAdd = alertaProdutos.filter(alertaProdutoItem => {
        const alertaProdutoIdentifier = getAlertaProdutoIdentifier(alertaProdutoItem);
        if (alertaProdutoIdentifier == null || alertaProdutoCollectionIdentifiers.includes(alertaProdutoIdentifier)) {
          return false;
        }
        alertaProdutoCollectionIdentifiers.push(alertaProdutoIdentifier);
        return true;
      });
      return [...alertaProdutosToAdd, ...alertaProdutoCollection];
    }
    return alertaProdutoCollection;
  }
}
