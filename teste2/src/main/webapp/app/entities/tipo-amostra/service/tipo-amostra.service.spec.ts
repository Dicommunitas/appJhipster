import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoAmostra, TipoAmostra } from '../tipo-amostra.model';

import { TipoAmostraService } from './tipo-amostra.service';

describe('TipoAmostra Service', () => {
  let service: TipoAmostraService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoAmostra;
  let expectedResult: ITipoAmostra | ITipoAmostra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoAmostraService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      descricao: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TipoAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoAmostra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descricao: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoAmostra', () => {
      const patchObject = Object.assign({}, new TipoAmostra());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descricao: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TipoAmostra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoAmostraToCollectionIfMissing', () => {
      it('should add a TipoAmostra to an empty array', () => {
        const tipoAmostra: ITipoAmostra = { id: 123 };
        expectedResult = service.addTipoAmostraToCollectionIfMissing([], tipoAmostra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoAmostra);
      });

      it('should not add a TipoAmostra to an array that contains it', () => {
        const tipoAmostra: ITipoAmostra = { id: 123 };
        const tipoAmostraCollection: ITipoAmostra[] = [
          {
            ...tipoAmostra,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoAmostraToCollectionIfMissing(tipoAmostraCollection, tipoAmostra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoAmostra to an array that doesn't contain it", () => {
        const tipoAmostra: ITipoAmostra = { id: 123 };
        const tipoAmostraCollection: ITipoAmostra[] = [{ id: 456 }];
        expectedResult = service.addTipoAmostraToCollectionIfMissing(tipoAmostraCollection, tipoAmostra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoAmostra);
      });

      it('should add only unique TipoAmostra to an array', () => {
        const tipoAmostraArray: ITipoAmostra[] = [{ id: 123 }, { id: 456 }, { id: 59315 }];
        const tipoAmostraCollection: ITipoAmostra[] = [{ id: 123 }];
        expectedResult = service.addTipoAmostraToCollectionIfMissing(tipoAmostraCollection, ...tipoAmostraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoAmostra: ITipoAmostra = { id: 123 };
        const tipoAmostra2: ITipoAmostra = { id: 456 };
        expectedResult = service.addTipoAmostraToCollectionIfMissing([], tipoAmostra, tipoAmostra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoAmostra);
        expect(expectedResult).toContain(tipoAmostra2);
      });

      it('should accept null and undefined values', () => {
        const tipoAmostra: ITipoAmostra = { id: 123 };
        expectedResult = service.addTipoAmostraToCollectionIfMissing([], null, tipoAmostra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoAmostra);
      });

      it('should return initial array if no TipoAmostra is added', () => {
        const tipoAmostraCollection: ITipoAmostra[] = [{ id: 123 }];
        expectedResult = service.addTipoAmostraToCollectionIfMissing(tipoAmostraCollection, undefined, null);
        expect(expectedResult).toEqual(tipoAmostraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
