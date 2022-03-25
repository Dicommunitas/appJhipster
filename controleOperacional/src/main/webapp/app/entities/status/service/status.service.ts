import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStatus, getStatusIdentifier } from '../status.model';

export type EntityResponseType = HttpResponse<IStatus>;
export type EntityArrayResponseType = HttpResponse<IStatus[]>;

@Injectable({ providedIn: 'root' })
export class StatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(status: IStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(status);
    return this.http
      .post<IStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(status: IStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(status);
    return this.http
      .put<IStatus>(`${this.resourceUrl}/${getStatusIdentifier(status) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(status: IStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(status);
    return this.http
      .patch<IStatus>(`${this.resourceUrl}/${getStatusIdentifier(status) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStatusToCollectionIfMissing(statusCollection: IStatus[], ...statusesToCheck: (IStatus | null | undefined)[]): IStatus[] {
    const statuses: IStatus[] = statusesToCheck.filter(isPresent);
    if (statuses.length > 0) {
      const statusCollectionIdentifiers = statusCollection.map(statusItem => getStatusIdentifier(statusItem)!);
      const statusesToAdd = statuses.filter(statusItem => {
        const statusIdentifier = getStatusIdentifier(statusItem);
        if (statusIdentifier == null || statusCollectionIdentifiers.includes(statusIdentifier)) {
          return false;
        }
        statusCollectionIdentifiers.push(statusIdentifier);
        return true;
      });
      return [...statusesToAdd, ...statusCollection];
    }
    return statusCollection;
  }

  protected convertDateFromClient(status: IStatus): IStatus {
    return Object.assign({}, status, {
      prazo: status.prazo?.isValid() ? status.prazo.format(DATE_FORMAT) : undefined,
      dataResolucao: status.dataResolucao?.isValid() ? status.dataResolucao.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.prazo = res.body.prazo ? dayjs(res.body.prazo) : undefined;
      res.body.dataResolucao = res.body.dataResolucao ? dayjs(res.body.dataResolucao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((status: IStatus) => {
        status.prazo = status.prazo ? dayjs(status.prazo) : undefined;
        status.dataResolucao = status.dataResolucao ? dayjs(status.dataResolucao) : undefined;
      });
    }
    return res;
  }
}
