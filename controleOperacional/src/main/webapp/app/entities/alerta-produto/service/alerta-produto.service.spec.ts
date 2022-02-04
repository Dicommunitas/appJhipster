import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAlertaProduto, AlertaProduto } from '../alerta-produto.model';

import { AlertaProdutoService } from './alerta-produto.service';

describe('AlertaProduto Service', () => {
  let service: AlertaProdutoService;
  let httpMock: HttpTestingController;
  let elemDefault: IAlertaProduto;
  let expectedResult: IAlertaProduto | IAlertaProduto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AlertaProdutoService);
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

    it('should create a AlertaProduto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AlertaProduto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AlertaProduto', () => {
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

    it('should partial update a AlertaProduto', () => {
      const patchObject = Object.assign({}, new AlertaProduto());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AlertaProduto', () => {
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

    it('should delete a AlertaProduto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAlertaProdutoToCollectionIfMissing', () => {
      it('should add a AlertaProduto to an empty array', () => {
        const alertaProduto: IAlertaProduto = { id: 123 };
        expectedResult = service.addAlertaProdutoToCollectionIfMissing([], alertaProduto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alertaProduto);
      });

      it('should not add a AlertaProduto to an array that contains it', () => {
        const alertaProduto: IAlertaProduto = { id: 123 };
        const alertaProdutoCollection: IAlertaProduto[] = [
          {
            ...alertaProduto,
          },
          { id: 456 },
        ];
        expectedResult = service.addAlertaProdutoToCollectionIfMissing(alertaProdutoCollection, alertaProduto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AlertaProduto to an array that doesn't contain it", () => {
        const alertaProduto: IAlertaProduto = { id: 123 };
        const alertaProdutoCollection: IAlertaProduto[] = [{ id: 456 }];
        expectedResult = service.addAlertaProdutoToCollectionIfMissing(alertaProdutoCollection, alertaProduto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alertaProduto);
      });

      it('should add only unique AlertaProduto to an array', () => {
        const alertaProdutoArray: IAlertaProduto[] = [{ id: 123 }, { id: 456 }, { id: 13110 }];
        const alertaProdutoCollection: IAlertaProduto[] = [{ id: 123 }];
        expectedResult = service.addAlertaProdutoToCollectionIfMissing(alertaProdutoCollection, ...alertaProdutoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alertaProduto: IAlertaProduto = { id: 123 };
        const alertaProduto2: IAlertaProduto = { id: 456 };
        expectedResult = service.addAlertaProdutoToCollectionIfMissing([], alertaProduto, alertaProduto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alertaProduto);
        expect(expectedResult).toContain(alertaProduto2);
      });

      it('should accept null and undefined values', () => {
        const alertaProduto: IAlertaProduto = { id: 123 };
        expectedResult = service.addAlertaProdutoToCollectionIfMissing([], null, alertaProduto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alertaProduto);
      });

      it('should return initial array if no AlertaProduto is added', () => {
        const alertaProdutoCollection: IAlertaProduto[] = [{ id: 123 }];
        expectedResult = service.addAlertaProdutoToCollectionIfMissing(alertaProdutoCollection, undefined, null);
        expect(expectedResult).toEqual(alertaProdutoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
