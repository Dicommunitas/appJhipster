import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoRelatorio, TipoRelatorio } from '../tipo-relatorio.model';

import { TipoRelatorioService } from './tipo-relatorio.service';

describe('TipoRelatorio Service', () => {
  let service: TipoRelatorioService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoRelatorio;
  let expectedResult: ITipoRelatorio | ITipoRelatorio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoRelatorioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
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

    it('should create a TipoRelatorio', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TipoRelatorio()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoRelatorio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoRelatorio', () => {
      const patchObject = Object.assign({}, new TipoRelatorio());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoRelatorio', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
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

    it('should delete a TipoRelatorio', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoRelatorioToCollectionIfMissing', () => {
      it('should add a TipoRelatorio to an empty array', () => {
        const tipoRelatorio: ITipoRelatorio = { id: 123 };
        expectedResult = service.addTipoRelatorioToCollectionIfMissing([], tipoRelatorio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoRelatorio);
      });

      it('should not add a TipoRelatorio to an array that contains it', () => {
        const tipoRelatorio: ITipoRelatorio = { id: 123 };
        const tipoRelatorioCollection: ITipoRelatorio[] = [
          {
            ...tipoRelatorio,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoRelatorioToCollectionIfMissing(tipoRelatorioCollection, tipoRelatorio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoRelatorio to an array that doesn't contain it", () => {
        const tipoRelatorio: ITipoRelatorio = { id: 123 };
        const tipoRelatorioCollection: ITipoRelatorio[] = [{ id: 456 }];
        expectedResult = service.addTipoRelatorioToCollectionIfMissing(tipoRelatorioCollection, tipoRelatorio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoRelatorio);
      });

      it('should add only unique TipoRelatorio to an array', () => {
        const tipoRelatorioArray: ITipoRelatorio[] = [{ id: 123 }, { id: 456 }, { id: 77264 }];
        const tipoRelatorioCollection: ITipoRelatorio[] = [{ id: 123 }];
        expectedResult = service.addTipoRelatorioToCollectionIfMissing(tipoRelatorioCollection, ...tipoRelatorioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoRelatorio: ITipoRelatorio = { id: 123 };
        const tipoRelatorio2: ITipoRelatorio = { id: 456 };
        expectedResult = service.addTipoRelatorioToCollectionIfMissing([], tipoRelatorio, tipoRelatorio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoRelatorio);
        expect(expectedResult).toContain(tipoRelatorio2);
      });

      it('should accept null and undefined values', () => {
        const tipoRelatorio: ITipoRelatorio = { id: 123 };
        expectedResult = service.addTipoRelatorioToCollectionIfMissing([], null, tipoRelatorio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoRelatorio);
      });

      it('should return initial array if no TipoRelatorio is added', () => {
        const tipoRelatorioCollection: ITipoRelatorio[] = [{ id: 123 }];
        expectedResult = service.addTipoRelatorioToCollectionIfMissing(tipoRelatorioCollection, undefined, null);
        expect(expectedResult).toEqual(tipoRelatorioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
