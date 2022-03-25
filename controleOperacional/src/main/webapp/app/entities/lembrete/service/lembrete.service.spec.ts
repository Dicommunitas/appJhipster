import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILembrete, Lembrete } from '../lembrete.model';

import { LembreteService } from './lembrete.service';

describe('Lembrete Service', () => {
  let service: LembreteService;
  let httpMock: HttpTestingController;
  let elemDefault: ILembrete;
  let expectedResult: ILembrete | ILembrete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LembreteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
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

    it('should create a Lembrete', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Lembrete()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lembrete', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
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

    it('should partial update a Lembrete', () => {
      const patchObject = Object.assign(
        {
          nome: 'BBBBBB',
        },
        new Lembrete()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lembrete', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
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

    it('should delete a Lembrete', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLembreteToCollectionIfMissing', () => {
      it('should add a Lembrete to an empty array', () => {
        const lembrete: ILembrete = { id: 123 };
        expectedResult = service.addLembreteToCollectionIfMissing([], lembrete);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lembrete);
      });

      it('should not add a Lembrete to an array that contains it', () => {
        const lembrete: ILembrete = { id: 123 };
        const lembreteCollection: ILembrete[] = [
          {
            ...lembrete,
          },
          { id: 456 },
        ];
        expectedResult = service.addLembreteToCollectionIfMissing(lembreteCollection, lembrete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lembrete to an array that doesn't contain it", () => {
        const lembrete: ILembrete = { id: 123 };
        const lembreteCollection: ILembrete[] = [{ id: 456 }];
        expectedResult = service.addLembreteToCollectionIfMissing(lembreteCollection, lembrete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lembrete);
      });

      it('should add only unique Lembrete to an array', () => {
        const lembreteArray: ILembrete[] = [{ id: 123 }, { id: 456 }, { id: 23692 }];
        const lembreteCollection: ILembrete[] = [{ id: 123 }];
        expectedResult = service.addLembreteToCollectionIfMissing(lembreteCollection, ...lembreteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lembrete: ILembrete = { id: 123 };
        const lembrete2: ILembrete = { id: 456 };
        expectedResult = service.addLembreteToCollectionIfMissing([], lembrete, lembrete2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lembrete);
        expect(expectedResult).toContain(lembrete2);
      });

      it('should accept null and undefined values', () => {
        const lembrete: ILembrete = { id: 123 };
        expectedResult = service.addLembreteToCollectionIfMissing([], null, lembrete, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lembrete);
      });

      it('should return initial array if no Lembrete is added', () => {
        const lembreteCollection: ILembrete[] = [{ id: 123 }];
        expectedResult = service.addLembreteToCollectionIfMissing(lembreteCollection, undefined, null);
        expect(expectedResult).toEqual(lembreteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
