import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOperacao, Operacao } from '../operacao.model';

import { OperacaoService } from './operacao.service';

describe('Operacao Service', () => {
  let service: OperacaoService;
  let httpMock: HttpTestingController;
  let elemDefault: IOperacao;
  let expectedResult: IOperacao | IOperacao[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperacaoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      descricao: 'AAAAAAA',
      volumePeso: 0,
      inicio: currentDate,
      fim: currentDate,
      quantidadeAmostras: 0,
      observacao: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          inicio: currentDate.format(DATE_TIME_FORMAT),
          fim: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Operacao', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          inicio: currentDate.format(DATE_TIME_FORMAT),
          fim: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inicio: currentDate,
          fim: currentDate,
        },
        returnedFromService
      );

      service.create(new Operacao()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Operacao', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descricao: 'BBBBBB',
          volumePeso: 1,
          inicio: currentDate.format(DATE_TIME_FORMAT),
          fim: currentDate.format(DATE_TIME_FORMAT),
          quantidadeAmostras: 1,
          observacao: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inicio: currentDate,
          fim: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Operacao', () => {
      const patchObject = Object.assign(
        {
          descricao: 'BBBBBB',
          fim: currentDate.format(DATE_TIME_FORMAT),
          quantidadeAmostras: 1,
        },
        new Operacao()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          inicio: currentDate,
          fim: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Operacao', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descricao: 'BBBBBB',
          volumePeso: 1,
          inicio: currentDate.format(DATE_TIME_FORMAT),
          fim: currentDate.format(DATE_TIME_FORMAT),
          quantidadeAmostras: 1,
          observacao: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inicio: currentDate,
          fim: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Operacao', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOperacaoToCollectionIfMissing', () => {
      it('should add a Operacao to an empty array', () => {
        const operacao: IOperacao = { id: 123 };
        expectedResult = service.addOperacaoToCollectionIfMissing([], operacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operacao);
      });

      it('should not add a Operacao to an array that contains it', () => {
        const operacao: IOperacao = { id: 123 };
        const operacaoCollection: IOperacao[] = [
          {
            ...operacao,
          },
          { id: 456 },
        ];
        expectedResult = service.addOperacaoToCollectionIfMissing(operacaoCollection, operacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Operacao to an array that doesn't contain it", () => {
        const operacao: IOperacao = { id: 123 };
        const operacaoCollection: IOperacao[] = [{ id: 456 }];
        expectedResult = service.addOperacaoToCollectionIfMissing(operacaoCollection, operacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operacao);
      });

      it('should add only unique Operacao to an array', () => {
        const operacaoArray: IOperacao[] = [{ id: 123 }, { id: 456 }, { id: 77722 }];
        const operacaoCollection: IOperacao[] = [{ id: 123 }];
        expectedResult = service.addOperacaoToCollectionIfMissing(operacaoCollection, ...operacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operacao: IOperacao = { id: 123 };
        const operacao2: IOperacao = { id: 456 };
        expectedResult = service.addOperacaoToCollectionIfMissing([], operacao, operacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operacao);
        expect(expectedResult).toContain(operacao2);
      });

      it('should accept null and undefined values', () => {
        const operacao: IOperacao = { id: 123 };
        expectedResult = service.addOperacaoToCollectionIfMissing([], null, operacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operacao);
      });

      it('should return initial array if no Operacao is added', () => {
        const operacaoCollection: IOperacao[] = [{ id: 123 }];
        expectedResult = service.addOperacaoToCollectionIfMissing(operacaoCollection, undefined, null);
        expect(expectedResult).toEqual(operacaoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
