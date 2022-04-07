import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TipoRelatorioService } from '../service/tipo-relatorio.service';

import { TipoRelatorioComponent } from './tipo-relatorio.component';

describe('TipoRelatorio Management Component', () => {
  let comp: TipoRelatorioComponent;
  let fixture: ComponentFixture<TipoRelatorioComponent>;
  let service: TipoRelatorioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoRelatorioComponent],
    })
      .overrideTemplate(TipoRelatorioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoRelatorioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoRelatorioService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.tipoRelatorios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
