import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFinalidadeAmostra, FinalidadeAmostra } from '../finalidade-amostra.model';

import { FinalidadeAmostraService } from './finalidade-amostra.service';

describe('FinalidadeAmostra Service', () => {
  let service: FinalidadeAmostraService;
  let httpMock: HttpTestingController;
  let elemDefault: IFinalidadeAmostra;
  let expectedResult: IFinalidadeAmostra | IFinalidadeAmostra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FinalidadeAmostraService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      lacre: 0,
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

    it('should create a FinalidadeAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FinalidadeAmostra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FinalidadeAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          lacre: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FinalidadeAmostra', () => {
      const patchObject = Object.assign(
        {
          lacre: 1,
        },
        new FinalidadeAmostra()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FinalidadeAmostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          lacre: 1,
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

    it('should delete a FinalidadeAmostra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFinalidadeAmostraToCollectionIfMissing', () => {
      it('should add a FinalidadeAmostra to an empty array', () => {
        const finalidadeAmostra: IFinalidadeAmostra = { id: 123 };
        expectedResult = service.addFinalidadeAmostraToCollectionIfMissing([], finalidadeAmostra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(finalidadeAmostra);
      });

      it('should not add a FinalidadeAmostra to an array that contains it', () => {
        const finalidadeAmostra: IFinalidadeAmostra = { id: 123 };
        const finalidadeAmostraCollection: IFinalidadeAmostra[] = [
          {
            ...finalidadeAmostra,
          },
          { id: 456 },
        ];
        expectedResult = service.addFinalidadeAmostraToCollectionIfMissing(finalidadeAmostraCollection, finalidadeAmostra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FinalidadeAmostra to an array that doesn't contain it", () => {
        const finalidadeAmostra: IFinalidadeAmostra = { id: 123 };
        const finalidadeAmostraCollection: IFinalidadeAmostra[] = [{ id: 456 }];
        expectedResult = service.addFinalidadeAmostraToCollectionIfMissing(finalidadeAmostraCollection, finalidadeAmostra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(finalidadeAmostra);
      });

      it('should add only unique FinalidadeAmostra to an array', () => {
        const finalidadeAmostraArray: IFinalidadeAmostra[] = [{ id: 123 }, { id: 456 }, { id: 78950 }];
        const finalidadeAmostraCollection: IFinalidadeAmostra[] = [{ id: 123 }];
        expectedResult = service.addFinalidadeAmostraToCollectionIfMissing(finalidadeAmostraCollection, ...finalidadeAmostraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const finalidadeAmostra: IFinalidadeAmostra = { id: 123 };
        const finalidadeAmostra2: IFinalidadeAmostra = { id: 456 };
        expectedResult = service.addFinalidadeAmostraToCollectionIfMissing([], finalidadeAmostra, finalidadeAmostra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(finalidadeAmostra);
        expect(expectedResult).toContain(finalidadeAmostra2);
      });

      it('should accept null and undefined values', () => {
        const finalidadeAmostra: IFinalidadeAmostra = { id: 123 };
        expectedResult = service.addFinalidadeAmostraToCollectionIfMissing([], null, finalidadeAmostra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(finalidadeAmostra);
      });

      it('should return initial array if no FinalidadeAmostra is added', () => {
        const finalidadeAmostraCollection: IFinalidadeAmostra[] = [{ id: 123 }];
        expectedResult = service.addFinalidadeAmostraToCollectionIfMissing(finalidadeAmostraCollection, undefined, null);
        expect(expectedResult).toEqual(finalidadeAmostraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
