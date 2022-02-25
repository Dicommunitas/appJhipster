import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
    return this.http.post<ILembrete>(this.resourceUrl, lembrete, { observe: 'response' });
  }

  update(lembrete: ILembrete): Observable<EntityResponseType> {
    return this.http.put<ILembrete>(`${this.resourceUrl}/${getLembreteIdentifier(lembrete) as number}`, lembrete, { observe: 'response' });
  }

  partialUpdate(lembrete: ILembrete): Observable<EntityResponseType> {
    return this.http.patch<ILembrete>(`${this.resourceUrl}/${getLembreteIdentifier(lembrete) as number}`, lembrete, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILembrete>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILembrete[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
