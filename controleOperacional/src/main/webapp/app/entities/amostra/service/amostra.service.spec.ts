import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAmostra, Amostra } from '../amostra.model';

import { AmostraService } from './amostra.service';

describe('Amostra Service', () => {
  let service: AmostraService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmostra;
  let expectedResult: IAmostra | IAmostra[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmostraService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dataHora: currentDate,
      observacao: 'AAAAAAA',
      identificadorExterno: 'AAAAAAA',
      amostraNoLaboratorio: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dataHora: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Amostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dataHora: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataHora: currentDate,
        },
        returnedFromService
      );

      service.create(new Amostra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Amostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dataHora: currentDate.format(DATE_TIME_FORMAT),
          observacao: 'BBBBBB',
          identificadorExterno: 'BBBBBB',
          amostraNoLaboratorio: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataHora: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Amostra', () => {
      const patchObject = Object.assign(
        {
          dataHora: currentDate.format(DATE_TIME_FORMAT),
          observacao: 'BBBBBB',
        },
        new Amostra()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dataHora: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Amostra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dataHora: currentDate.format(DATE_TIME_FORMAT),
          observacao: 'BBBBBB',
          identificadorExterno: 'BBBBBB',
          amostraNoLaboratorio: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataHora: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Amostra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmostraToCollectionIfMissing', () => {
      it('should add a Amostra to an empty array', () => {
        const amostra: IAmostra = { id: 123 };
        expectedResult = service.addAmostraToCollectionIfMissing([], amostra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amostra);
      });

      it('should not add a Amostra to an array that contains it', () => {
        const amostra: IAmostra = { id: 123 };
        const amostraCollection: IAmostra[] = [
          {
            ...amostra,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmostraToCollectionIfMissing(amostraCollection, amostra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Amostra to an array that doesn't contain it", () => {
        const amostra: IAmostra = { id: 123 };
        const amostraCollection: IAmostra[] = [{ id: 456 }];
        expectedResult = service.addAmostraToCollectionIfMissing(amostraCollection, amostra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amostra);
      });

      it('should add only unique Amostra to an array', () => {
        const amostraArray: IAmostra[] = [{ id: 123 }, { id: 456 }, { id: 55185 }];
        const amostraCollection: IAmostra[] = [{ id: 123 }];
        expectedResult = service.addAmostraToCollectionIfMissing(amostraCollection, ...amostraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amostra: IAmostra = { id: 123 };
        const amostra2: IAmostra = { id: 456 };
        expectedResult = service.addAmostraToCollectionIfMissing([], amostra, amostra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amostra);
        expect(expectedResult).toContain(amostra2);
      });

      it('should accept null and undefined values', () => {
        const amostra: IAmostra = { id: 123 };
        expectedResult = service.addAmostraToCollectionIfMissing([], null, amostra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amostra);
      });

      it('should return initial array if no Amostra is added', () => {
        const amostraCollection: IAmostra[] = [{ id: 123 }];
        expectedResult = service.addAmostraToCollectionIfMissing(amostraCollection, undefined, null);
        expect(expectedResult).toEqual(amostraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
