import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrigemAmostraDetailComponent } from './origem-amostra-detail.component';

describe('OrigemAmostra Management Detail Component', () => {
  let comp: OrigemAmostraDetailComponent;
  let fixture: ComponentFixture<OrigemAmostraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrigemAmostraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ origemAmostra: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrigemAmostraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrigemAmostraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load origemAmostra on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.origemAmostra).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
