import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoRelatorioDetailComponent } from './tipo-relatorio-detail.component';

describe('TipoRelatorio Management Detail Component', () => {
  let comp: TipoRelatorioDetailComponent;
  let fixture: ComponentFixture<TipoRelatorioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoRelatorioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoRelatorio: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoRelatorioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoRelatorioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoRelatorio on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoRelatorio).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
