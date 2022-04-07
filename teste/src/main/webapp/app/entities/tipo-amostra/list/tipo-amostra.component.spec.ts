import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TipoAmostraService } from '../service/tipo-amostra.service';

import { TipoAmostraComponent } from './tipo-amostra.component';

describe('TipoAmostra Management Component', () => {
  let comp: TipoAmostraComponent;
  let fixture: ComponentFixture<TipoAmostraComponent>;
  let service: TipoAmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoAmostraComponent],
    })
      .overrideTemplate(TipoAmostraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoAmostraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoAmostraService);

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
    expect(comp.tipoAmostras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
