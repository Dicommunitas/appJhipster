import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AlertaProdutoService } from '../service/alerta-produto.service';

import { AlertaProdutoComponent } from './alerta-produto.component';

describe('AlertaProduto Management Component', () => {
  let comp: AlertaProdutoComponent;
  let fixture: ComponentFixture<AlertaProdutoComponent>;
  let service: AlertaProdutoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AlertaProdutoComponent],
    })
      .overrideTemplate(AlertaProdutoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlertaProdutoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AlertaProdutoService);

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
    expect(comp.alertaProdutos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
