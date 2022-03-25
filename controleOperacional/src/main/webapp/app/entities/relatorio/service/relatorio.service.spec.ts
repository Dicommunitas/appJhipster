import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRelatorio, Relatorio } from '../relatorio.model';

import { RelatorioService } from './relatorio.service';

describe('Relatorio Service', () => {
  let service: RelatorioService;
  let httpMock: HttpTestingController;
  let elemDefault: IRelatorio;
  let expectedResult: IRelatorio | IRelatorio[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RelatorioService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dataHora: currentDate,
      relato: 'AAAAAAA',
      linksExternos: 'AAAAAAA',
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

    it('should create a Relatorio', () => {
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

      service.create(new Relatorio()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Relatorio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dataHora: currentDate.format(DATE_TIME_FORMAT),
          relato: 'BBBBBB',
          linksExternos: 'BBBBBB',
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

    it('should partial update a Relatorio', () => {
      const patchObject = Object.assign({}, new Relatorio());

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

    it('should return a list of Relatorio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dataHora: currentDate.format(DATE_TIME_FORMAT),
          relato: 'BBBBBB',
          linksExternos: 'BBBBBB',
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

    it('should delete a Relatorio', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRelatorioToCollectionIfMissing', () => {
      it('should add a Relatorio to an empty array', () => {
        const relatorio: IRelatorio = { id: 123 };
        expectedResult = service.addRelatorioToCollectionIfMissing([], relatorio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(relatorio);
      });

      it('should not add a Relatorio to an array that contains it', () => {
        const relatorio: IRelatorio = { id: 123 };
        const relatorioCollection: IRelatorio[] = [
          {
            ...relatorio,
          },
          { id: 456 },
        ];
        expectedResult = service.addRelatorioToCollectionIfMissing(relatorioCollection, relatorio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Relatorio to an array that doesn't contain it", () => {
        const relatorio: IRelatorio = { id: 123 };
        const relatorioCollection: IRelatorio[] = [{ id: 456 }];
        expectedResult = service.addRelatorioToCollectionIfMissing(relatorioCollection, relatorio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(relatorio);
      });

      it('should add only unique Relatorio to an array', () => {
        const relatorioArray: IRelatorio[] = [{ id: 123 }, { id: 456 }, { id: 34998 }];
        const relatorioCollection: IRelatorio[] = [{ id: 123 }];
        expectedResult = service.addRelatorioToCollectionIfMissing(relatorioCollection, ...relatorioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const relatorio: IRelatorio = { id: 123 };
        const relatorio2: IRelatorio = { id: 456 };
        expectedResult = service.addRelatorioToCollectionIfMissing([], relatorio, relatorio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(relatorio);
        expect(expectedResult).toContain(relatorio2);
      });

      it('should accept null and undefined values', () => {
        const relatorio: IRelatorio = { id: 123 };
        expectedResult = service.addRelatorioToCollectionIfMissing([], null, relatorio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(relatorio);
      });

      it('should return initial array if no Relatorio is added', () => {
        const relatorioCollection: IRelatorio[] = [{ id: 123 }];
        expectedResult = service.addRelatorioToCollectionIfMissing(relatorioCollection, undefined, null);
        expect(expectedResult).toEqual(relatorioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
