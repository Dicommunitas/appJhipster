import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoFinalidadeAmostra, TipoFinalidadeAmostra } from '../tipo-finalidade-amostra.model';

import { TipoFinalidadeAmostraService } from './tipo-finalidade-amostra.service';

describe('TipoFinalidadeAmostra Service', () => {
  let service: TipoFinalidadeAmostraService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoFinalidadeAmostra;
  let expectedResult: ITipoFinalidadeAmostra | ITipoFinalidadeAmostra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoFinalidadeAmostraService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      descricao: 'AAAAAAA',
      obrigatorioLacre: false,
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

    it('should create a TipoFinalidadeAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoFinalidadeAmostra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoFinalidadeAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descricao: 'BBBBBB',
          obrigatorioLacre: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoFinalidadeAmostra', () => {
      const patchObject = Object.assign(
        {
          descricao: 'BBBBBB',
          obrigatorioLacre: true,
        },
        new TipoFinalidadeAmostra()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoFinalidadeAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descricao: 'BBBBBB',
          obrigatorioLacre: true,
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

    it('should delete a TipoFinalidadeAmostra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoFinalidadeAmostraToCollectionIfMissing', () => {
      it('should add a TipoFinalidadeAmostra to an empty array', () => {
        const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 123 };
        expectedResult = service.addTipoFinalidadeAmostraToCollectionIfMissing([], tipoFinalidadeAmostra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoFinalidadeAmostra);
      });

      it('should not add a TipoFinalidadeAmostra to an array that contains it', () => {
        const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 123 };
        const tipoFinalidadeAmostraCollection: ITipoFinalidadeAmostra[] = [
          {
            ...tipoFinalidadeAmostra,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoFinalidadeAmostraToCollectionIfMissing(tipoFinalidadeAmostraCollection, tipoFinalidadeAmostra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoFinalidadeAmostra to an array that doesn't contain it", () => {
        const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 123 };
        const tipoFinalidadeAmostraCollection: ITipoFinalidadeAmostra[] = [{ id: 456 }];
        expectedResult = service.addTipoFinalidadeAmostraToCollectionIfMissing(tipoFinalidadeAmostraCollection, tipoFinalidadeAmostra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoFinalidadeAmostra);
      });

      it('should add only unique TipoFinalidadeAmostra to an array', () => {
        const tipoFinalidadeAmostraArray: ITipoFinalidadeAmostra[] = [{ id: 123 }, { id: 456 }, { id: 84675 }];
        const tipoFinalidadeAmostraCollection: ITipoFinalidadeAmostra[] = [{ id: 123 }];
        expectedResult = service.addTipoFinalidadeAmostraToCollectionIfMissing(
          tipoFinalidadeAmostraCollection,
          ...tipoFinalidadeAmostraArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 123 };
        const tipoFinalidadeAmostra2: ITipoFinalidadeAmostra = { id: 456 };
        expectedResult = service.addTipoFinalidadeAmostraToCollectionIfMissing([], tipoFinalidadeAmostra, tipoFinalidadeAmostra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoFinalidadeAmostra);
        expect(expectedResult).toContain(tipoFinalidadeAmostra2);
      });

      it('should accept null and undefined values', () => {
        const tipoFinalidadeAmostra: ITipoFinalidadeAmostra = { id: 123 };
        expectedResult = service.addTipoFinalidadeAmostraToCollectionIfMissing([], null, tipoFinalidadeAmostra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoFinalidadeAmostra);
      });

      it('should return initial array if no TipoFinalidadeAmostra is added', () => {
        const tipoFinalidadeAmostraCollection: ITipoFinalidadeAmostra[] = [{ id: 123 }];
        expectedResult = service.addTipoFinalidadeAmostraToCollectionIfMissing(tipoFinalidadeAmostraCollection, undefined, null);
        expect(expectedResult).toEqual(tipoFinalidadeAmostraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
