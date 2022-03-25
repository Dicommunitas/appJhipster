import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LembreteService } from '../service/lembrete.service';

import { LembreteComponent } from './lembrete.component';

describe('Lembrete Management Component', () => {
  let comp: LembreteComponent;
  let fixture: ComponentFixture<LembreteComponent>;
  let service: LembreteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LembreteComponent],
    })
      .overrideTemplate(LembreteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LembreteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LembreteService);

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
    expect(comp.lembretes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
