import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TipoOperacaoService } from '../service/tipo-operacao.service';

import { TipoOperacaoComponent } from './tipo-operacao.component';

describe('TipoOperacao Management Component', () => {
  let comp: TipoOperacaoComponent;
  let fixture: ComponentFixture<TipoOperacaoComponent>;
  let service: TipoOperacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoOperacaoComponent],
    })
      .overrideTemplate(TipoOperacaoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoOperacaoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoOperacaoService);

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
    expect(comp.tipoOperacaos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
