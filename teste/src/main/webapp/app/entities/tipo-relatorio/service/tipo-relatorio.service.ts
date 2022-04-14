import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoRelatorio, getTipoRelatorioIdentifier } from '../tipo-relatorio.model';

export type EntityResponseType = HttpResponse<ITipoRelatorio>;
export type EntityArrayResponseType = HttpResponse<ITipoRelatorio[]>;

@Injectable({ providedIn: 'root' })
export class TipoRelatorioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-relatorios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoRelatorio: ITipoRelatorio): Observable<EntityResponseType> {
    return this.http.post<ITipoRelatorio>(this.resourceUrl, tipoRelatorio, { observe: 'response' });
  }

  update(tipoRelatorio: ITipoRelatorio): Observable<EntityResponseType> {
    return this.http.put<ITipoRelatorio>(`${this.resourceUrl}/${getTipoRelatorioIdentifier(tipoRelatorio) as number}`, tipoRelatorio, {
      observe: 'response',
    });
  }

  partialUpdate(tipoRelatorio: ITipoRelatorio): Observable<EntityResponseType> {
    return this.http.patch<ITipoRelatorio>(`${this.resourceUrl}/${getTipoRelatorioIdentifier(tipoRelatorio) as number}`, tipoRelatorio, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoRelatorio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoRelatorio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoRelatorioToCollectionIfMissing(
    tipoRelatorioCollection: ITipoRelatorio[],
    ...tipoRelatoriosToCheck: (ITipoRelatorio | null | undefined)[]
  ): ITipoRelatorio[] {
    const tipoRelatorios: ITipoRelatorio[] = tipoRelatoriosToCheck.filter(isPresent);
    if (tipoRelatorios.length > 0) {
      const tipoRelatorioCollectionIdentifiers = tipoRelatorioCollection.map(
        tipoRelatorioItem => getTipoRelatorioIdentifier(tipoRelatorioItem)!
      );
      const tipoRelatoriosToAdd = tipoRelatorios.filter(tipoRelatorioItem => {
        const tipoRelatorioIdentifier = getTipoRelatorioIdentifier(tipoRelatorioItem);
        if (tipoRelatorioIdentifier == null || tipoRelatorioCollectionIdentifiers.includes(tipoRelatorioIdentifier)) {
          return false;
        }
        tipoRelatorioCollectionIdentifiers.push(tipoRelatorioIdentifier);
        return true;
      });
      return [...tipoRelatoriosToAdd, ...tipoRelatorioCollection];
    }
    return tipoRelatorioCollection;
  }
}
