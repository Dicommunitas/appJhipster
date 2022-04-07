import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoFinalidadeAmostraDetailComponent } from './tipo-finalidade-amostra-detail.component';

describe('TipoFinalidadeAmostra Management Detail Component', () => {
  let comp: TipoFinalidadeAmostraDetailComponent;
  let fixture: ComponentFixture<TipoFinalidadeAmostraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoFinalidadeAmostraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoFinalidadeAmostra: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoFinalidadeAmostraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoFinalidadeAmostraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoFinalidadeAmostra on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoFinalidadeAmostra).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
