import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OperacaoDetailComponent } from './operacao-detail.component';

describe('Operacao Management Detail Component', () => {
  let comp: OperacaoDetailComponent;
  let fixture: ComponentFixture<OperacaoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperacaoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ operacao: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OperacaoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OperacaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load operacao on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.operacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
