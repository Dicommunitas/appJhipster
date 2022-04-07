import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoOperacaoDetailComponent } from './tipo-operacao-detail.component';

describe('TipoOperacao Management Detail Component', () => {
  let comp: TipoOperacaoDetailComponent;
  let fixture: ComponentFixture<TipoOperacaoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoOperacaoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoOperacao: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoOperacaoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoOperacaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoOperacao on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoOperacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
