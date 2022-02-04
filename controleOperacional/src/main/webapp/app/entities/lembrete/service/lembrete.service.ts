import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILembrete, getLembreteIdentifier } from '../lembrete.model';

export type EntityResponseType = HttpResponse<ILembrete>;
export type EntityArrayResponseType = HttpResponse<ILembrete[]>;

@Injectable({ providedIn: 'root' })
export class LembreteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lembretes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lembrete: ILembrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lembrete);
    return this.http
      .post<ILembrete>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lembrete: ILembrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lembrete);
    return this.http
      .put<ILembrete>(`${this.resourceUrl}/${getLembreteIdentifier(lembrete) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lembrete: ILembrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lembrete);
    return this.http
      .patch<ILembrete>(`${this.resourceUrl}/${getLembreteIdentifier(lembrete) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILembrete>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILembrete[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLembreteToCollectionIfMissing(lembreteCollection: ILembrete[], ...lembretesToCheck: (ILembrete | null | undefined)[]): ILembrete[] {
    const lembretes: ILembrete[] = lembretesToCheck.filter(isPresent);
    if (lembretes.length > 0) {
      const lembreteCollectionIdentifiers = lembreteCollection.map(lembreteItem => getLembreteIdentifier(lembreteItem)!);
      const lembretesToAdd = lembretes.filter(lembreteItem => {
        const lembreteIdentifier = getLembreteIdentifier(lembreteItem);
        if (lembreteIdentifier == null || lembreteCollectionIdentifiers.includes(lembreteIdentifier)) {
          return false;
        }
        lembreteCollectionIdentifiers.push(lembreteIdentifier);
        return true;
      });
      return [...lembretesToAdd, ...lembreteCollection];
    }
    return lembreteCollection;
  }

  protected convertDateFromClient(lembrete: ILembrete): ILembrete {
    return Object.assign({}, lembrete, {
      data: lembrete.data?.isValid() ? lembrete.data.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.data = res.body.data ? dayjs(res.body.data) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((lembrete: ILembrete) => {
        lembrete.data = lembrete.data ? dayjs(lembrete.data) : undefined;
      });
    }
    return res;
  }
}
