import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Criticidade } from 'app/entities/enumerations/criticidade.model';
import { IProblema, Problema } from '../problema.model';

import { ProblemaService } from './problema.service';

describe('Problema Service', () => {
  let service: ProblemaService;
  let httpMock: HttpTestingController;
  let elemDefault: IProblema;
  let expectedResult: IProblema | IProblema[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProblemaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      data: currentDate,
      descricao: 'AAAAAAA',
      criticidade: Criticidade.BAIXA,
      aceitarFinalizacao: false,
      fotoContentType: 'image/png',
      foto: 'AAAAAAA',
      impacto: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          data: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Problema', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          data: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          data: currentDate,
        },
        returnedFromService
      );

      service.create(new Problema()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Problema', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          data: currentDate.format(DATE_FORMAT),
          descricao: 'BBBBBB',
          criticidade: 'BBBBBB',
          aceitarFinalizacao: true,
          foto: 'BBBBBB',
          impacto: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          data: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Problema', () => {
      const patchObject = Object.assign(
        {
          data: currentDate.format(DATE_FORMAT),
          descricao: 'BBBBBB',
          foto: 'BBBBBB',
          impacto: 'BBBBBB',
        },
        new Problema()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          data: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Problema', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          data: currentDate.format(DATE_FORMAT),
          descricao: 'BBBBBB',
          criticidade: 'BBBBBB',
          aceitarFinalizacao: true,
          foto: 'BBBBBB',
          impacto: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          data: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Problema', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProblemaToCollectionIfMissing', () => {
      it('should add a Problema to an empty array', () => {
        const problema: IProblema = { id: 123 };
        expectedResult = service.addProblemaToCollectionIfMissing([], problema);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(problema);
      });

      it('should not add a Problema to an array that contains it', () => {
        const problema: IProblema = { id: 123 };
        const problemaCollection: IProblema[] = [
          {
            ...problema,
          },
          { id: 456 },
        ];
        expectedResult = service.addProblemaToCollectionIfMissing(problemaCollection, problema);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Problema to an array that doesn't contain it", () => {
        const problema: IProblema = { id: 123 };
        const problemaCollection: IProblema[] = [{ id: 456 }];
        expectedResult = service.addProblemaToCollectionIfMissing(problemaCollection, problema);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(problema);
      });

      it('should add only unique Problema to an array', () => {
        const problemaArray: IProblema[] = [{ id: 123 }, { id: 456 }, { id: 85340 }];
        const problemaCollection: IProblema[] = [{ id: 123 }];
        expectedResult = service.addProblemaToCollectionIfMissing(problemaCollection, ...problemaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const problema: IProblema = { id: 123 };
        const problema2: IProblema = { id: 456 };
        expectedResult = service.addProblemaToCollectionIfMissing([], problema, problema2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(problema);
        expect(expectedResult).toContain(problema2);
      });

      it('should accept null and undefined values', () => {
        const problema: IProblema = { id: 123 };
        expectedResult = service.addProblemaToCollectionIfMissing([], null, problema, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(problema);
      });

      it('should return initial array if no Problema is added', () => {
        const problemaCollection: IProblema[] = [{ id: 123 }];
        expectedResult = service.addProblemaToCollectionIfMissing(problemaCollection, undefined, null);
        expect(expectedResult).toEqual(problemaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
