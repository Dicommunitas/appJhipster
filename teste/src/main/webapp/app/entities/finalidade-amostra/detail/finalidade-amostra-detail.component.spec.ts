import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FinalidadeAmostraDetailComponent } from './finalidade-amostra-detail.component';

describe('FinalidadeAmostra Management Detail Component', () => {
  let comp: FinalidadeAmostraDetailComponent;
  let fixture: ComponentFixture<FinalidadeAmostraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FinalidadeAmostraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ finalidadeAmostra: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FinalidadeAmostraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FinalidadeAmostraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load finalidadeAmostra on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.finalidadeAmostra).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
