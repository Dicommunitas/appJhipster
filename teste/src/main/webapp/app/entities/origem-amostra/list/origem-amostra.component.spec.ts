import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrigemAmostraService } from '../service/origem-amostra.service';

import { OrigemAmostraComponent } from './origem-amostra.component';

describe('OrigemAmostra Management Component', () => {
  let comp: OrigemAmostraComponent;
  let fixture: ComponentFixture<OrigemAmostraComponent>;
  let service: OrigemAmostraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OrigemAmostraComponent],
    })
      .overrideTemplate(OrigemAmostraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrigemAmostraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrigemAmostraService);

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
    expect(comp.origemAmostras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
