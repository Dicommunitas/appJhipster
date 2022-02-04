import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TipoAmostraDetailComponent } from './tipo-amostra-detail.component';

describe('TipoAmostra Management Detail Component', () => {
  let comp: TipoAmostraDetailComponent;
  let fixture: ComponentFixture<TipoAmostraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TipoAmostraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tipoAmostra: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TipoAmostraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TipoAmostraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tipoAmostra on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tipoAmostra).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
