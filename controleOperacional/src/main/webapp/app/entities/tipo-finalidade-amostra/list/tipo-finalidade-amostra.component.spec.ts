import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TipoFinalidadeAmostraService } from '../service/tipo-finalidade-amostra.service';

import { TipoFinalidadeAmostraComponent } from './tipo-finalidade-amostra.component';

describe('TipoFinalidadeAmostra Management Component', () => {
  let comp: TipoFinalidadeAmostraComponent;
  let fixture: ComponentFixture<TipoFinalidadeAmostraComponent>;
  let service: TipoFinalidadeAmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoFinalidadeAmostraComponent],
    })
      .overrideTemplate(TipoFinalidadeAmostraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoFinalidadeAmostraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoFinalidadeAmostraService);

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
    expect(comp.tipoFinalidadeAmostras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
