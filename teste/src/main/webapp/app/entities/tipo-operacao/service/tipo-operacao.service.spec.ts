import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoOperacao, TipoOperacao } from '../tipo-operacao.model';

import { TipoOperacaoService } from './tipo-operacao.service';

describe('TipoOperacao Service', () => {
  let service: TipoOperacaoService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoOperacao;
  let expectedResult: ITipoOperacao | ITipoOperacao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoOperacaoService);
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

    it('should create a TipoOperacao', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoOperacao()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoOperacao', () => {
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

    it('should partial update a TipoOperacao', () => {
      const patchObject = Object.assign(
        {
          descricao: 'BBBBBB',
        },
        new TipoOperacao()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoOperacao', () => {
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

    it('should delete a TipoOperacao', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoOperacaoToCollectionIfMissing', () => {
      it('should add a TipoOperacao to an empty array', () => {
        const tipoOperacao: ITipoOperacao = { id: 123 };
        expectedResult = service.addTipoOperacaoToCollectionIfMissing([], tipoOperacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoOperacao);
      });

      it('should not add a TipoOperacao to an array that contains it', () => {
        const tipoOperacao: ITipoOperacao = { id: 123 };
        const tipoOperacaoCollection: ITipoOperacao[] = [
          {
            ...tipoOperacao,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoOperacaoToCollectionIfMissing(tipoOperacaoCollection, tipoOperacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoOperacao to an array that doesn't contain it", () => {
        const tipoOperacao: ITipoOperacao = { id: 123 };
        const tipoOperacaoCollection: ITipoOperacao[] = [{ id: 456 }];
        expectedResult = service.addTipoOperacaoToCollectionIfMissing(tipoOperacaoCollection, tipoOperacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoOperacao);
      });

      it('should add only unique TipoOperacao to an array', () => {
        const tipoOperacaoArray: ITipoOperacao[] = [{ id: 123 }, { id: 456 }, { id: 1042 }];
        const tipoOperacaoCollection: ITipoOperacao[] = [{ id: 123 }];
        expectedResult = service.addTipoOperacaoToCollectionIfMissing(tipoOperacaoCollection, ...tipoOperacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoOperacao: ITipoOperacao = { id: 123 };
        const tipoOperacao2: ITipoOperacao = { id: 456 };
        expectedResult = service.addTipoOperacaoToCollectionIfMissing([], tipoOperacao, tipoOperacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoOperacao);
        expect(expectedResult).toContain(tipoOperacao2);
      });

      it('should accept null and undefined values', () => {
        const tipoOperacao: ITipoOperacao = { id: 123 };
        expectedResult = service.addTipoOperacaoToCollectionIfMissing([], null, tipoOperacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoOperacao);
      });

      it('should return initial array if no TipoOperacao is added', () => {
        const tipoOperacaoCollection: ITipoOperacao[] = [{ id: 123 }];
        expectedResult = service.addTipoOperacaoToCollectionIfMissing(tipoOperacaoCollection, undefined, null);
        expect(expectedResult).toEqual(tipoOperacaoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
