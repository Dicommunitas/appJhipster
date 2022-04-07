import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrigemAmostra, OrigemAmostra } from '../origem-amostra.model';

import { OrigemAmostraService } from './origem-amostra.service';

describe('OrigemAmostra Service', () => {
  let service: OrigemAmostraService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrigemAmostra;
  let expectedResult: IOrigemAmostra | IOrigemAmostra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrigemAmostraService);
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

    it('should create a OrigemAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OrigemAmostra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrigemAmostra', () => {
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

    it('should partial update a OrigemAmostra', () => {
      const patchObject = Object.assign({}, new OrigemAmostra());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrigemAmostra', () => {
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

    it('should delete a OrigemAmostra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrigemAmostraToCollectionIfMissing', () => {
      it('should add a OrigemAmostra to an empty array', () => {
        const origemAmostra: IOrigemAmostra = { id: 123 };
        expectedResult = service.addOrigemAmostraToCollectionIfMissing([], origemAmostra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(origemAmostra);
      });

      it('should not add a OrigemAmostra to an array that contains it', () => {
        const origemAmostra: IOrigemAmostra = { id: 123 };
        const origemAmostraCollection: IOrigemAmostra[] = [
          {
            ...origemAmostra,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrigemAmostraToCollectionIfMissing(origemAmostraCollection, origemAmostra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrigemAmostra to an array that doesn't contain it", () => {
        const origemAmostra: IOrigemAmostra = { id: 123 };
        const origemAmostraCollection: IOrigemAmostra[] = [{ id: 456 }];
        expectedResult = service.addOrigemAmostraToCollectionIfMissing(origemAmostraCollection, origemAmostra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(origemAmostra);
      });

      it('should add only unique OrigemAmostra to an array', () => {
        const origemAmostraArray: IOrigemAmostra[] = [{ id: 123 }, { id: 456 }, { id: 58604 }];
        const origemAmostraCollection: IOrigemAmostra[] = [{ id: 123 }];
        expectedResult = service.addOrigemAmostraToCollectionIfMissing(origemAmostraCollection, ...origemAmostraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const origemAmostra: IOrigemAmostra = { id: 123 };
        const origemAmostra2: IOrigemAmostra = { id: 456 };
        expectedResult = service.addOrigemAmostraToCollectionIfMissing([], origemAmostra, origemAmostra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(origemAmostra);
        expect(expectedResult).toContain(origemAmostra2);
      });

      it('should accept null and undefined values', () => {
        const origemAmostra: IOrigemAmostra = { id: 123 };
        expectedResult = service.addOrigemAmostraToCollectionIfMissing([], null, origemAmostra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(origemAmostra);
      });

      it('should return initial array if no OrigemAmostra is added', () => {
        const origemAmostraCollection: IOrigemAmostra[] = [{ id: 123 }];
        expectedResult = service.addOrigemAmostraToCollectionIfMissing(origemAmostraCollection, undefined, null);
        expect(expectedResult).toEqual(origemAmostraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
