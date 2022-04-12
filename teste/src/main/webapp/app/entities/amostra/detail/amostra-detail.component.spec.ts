import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AmostraDetailComponent } from './amostra-detail.component';

describe('Amostra Management Detail Component', () => {
  let comp: AmostraDetailComponent;
  let fixture: ComponentFixture<AmostraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmostraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ amostra: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AmostraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AmostraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load amostra on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.amostra).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
