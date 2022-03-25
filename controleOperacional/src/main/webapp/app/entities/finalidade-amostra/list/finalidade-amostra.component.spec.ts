import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FinalidadeAmostraService } from '../service/finalidade-amostra.service';

import { FinalidadeAmostraComponent } from './finalidade-amostra.component';

describe('FinalidadeAmostra Management Component', () => {
  let comp: FinalidadeAmostraComponent;
  let fixture: ComponentFixture<FinalidadeAmostraComponent>;
  let service: FinalidadeAmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FinalidadeAmostraComponent],
    })
      .overrideTemplate(FinalidadeAmostraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FinalidadeAmostraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FinalidadeAmostraService);

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
    expect(comp.finalidadeAmostras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
