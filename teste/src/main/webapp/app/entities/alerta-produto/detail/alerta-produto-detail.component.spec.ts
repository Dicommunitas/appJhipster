import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AlertaProdutoDetailComponent } from './alerta-produto-detail.component';

describe('AlertaProduto Management Detail Component', () => {
  let comp: AlertaProdutoDetailComponent;
  let fixture: ComponentFixture<AlertaProdutoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AlertaProdutoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ alertaProduto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AlertaProdutoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AlertaProdutoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load alertaProduto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.alertaProduto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
