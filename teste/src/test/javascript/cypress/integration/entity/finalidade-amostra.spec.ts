import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('FinalidadeAmostra e2e test', () => {
  const finalidadeAmostraPageUrl = '/finalidade-amostra';
  const finalidadeAmostraPageUrlPattern = new RegExp('/finalidade-amostra(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const finalidadeAmostraSample = {};

  let finalidadeAmostra: any;
  //let tipoFinalidadeAmostra: any;
  //let amostra: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/tipo-finalidade-amostras',
      body: {"descricao":"Rodovia Chapéu","obrigatorioLacre":false},
    }).then(({ body }) => {
      tipoFinalidadeAmostra = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/amostras',
      body: {"dataHoraColeta":"2022-04-11T16:52:45.305Z","observacao":"copying","identificadorExterno":"Prático FTP","recebimentoNoLaboratorio":"2022-04-11T05:13:24.785Z","createdBy":"user-centric Marginal","createdDate":"2022-04-11T11:35:08.451Z","lastModifiedBy":"protocol Monitored","lastModifiedDate":"2022-04-11T08:28:54.311Z"},
    }).then(({ body }) => {
      amostra = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/finalidade-amostras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/finalidade-amostras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/finalidade-amostras/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/tipo-finalidade-amostras', {
      statusCode: 200,
      body: [tipoFinalidadeAmostra],
    });

    cy.intercept('GET', '/api/amostras', {
      statusCode: 200,
      body: [amostra],
    });

  });
   */

  afterEach(() => {
    if (finalidadeAmostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/finalidade-amostras/${finalidadeAmostra.id}`,
      }).then(() => {
        finalidadeAmostra = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (tipoFinalidadeAmostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-finalidade-amostras/${tipoFinalidadeAmostra.id}`,
      }).then(() => {
        tipoFinalidadeAmostra = undefined;
      });
    }
    if (amostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amostras/${amostra.id}`,
      }).then(() => {
        amostra = undefined;
      });
    }
  });
   */

  it('FinalidadeAmostras menu should load FinalidadeAmostras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('finalidade-amostra');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FinalidadeAmostra').should('exist');
    cy.url().should('match', finalidadeAmostraPageUrlPattern);
  });

  describe('FinalidadeAmostra page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(finalidadeAmostraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FinalidadeAmostra page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/finalidade-amostra/new$'));
        cy.getEntityCreateUpdateHeading('FinalidadeAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', finalidadeAmostraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/finalidade-amostras',
          body: {
            ...finalidadeAmostraSample,
            tipoFinalidadeAmostra: tipoFinalidadeAmostra,
            amostra: amostra,
          },
        }).then(({ body }) => {
          finalidadeAmostra = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/finalidade-amostras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [finalidadeAmostra],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(finalidadeAmostraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(finalidadeAmostraPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details FinalidadeAmostra page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('finalidadeAmostra');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', finalidadeAmostraPageUrlPattern);
      });

      it('edit button click should load edit FinalidadeAmostra page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinalidadeAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', finalidadeAmostraPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of FinalidadeAmostra', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('finalidadeAmostra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', finalidadeAmostraPageUrlPattern);

        finalidadeAmostra = undefined;
      });
    });
  });

  describe('new FinalidadeAmostra page', () => {
    beforeEach(() => {
      cy.visit(`${finalidadeAmostraPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FinalidadeAmostra');
    });

    it.skip('should create an instance of FinalidadeAmostra', () => {
      cy.get(`[data-cy="lacre"]`).type('35119').should('have.value', '35119');

      cy.get(`[data-cy="tipoFinalidadeAmostra"]`).select(1);
      cy.get(`[data-cy="amostra"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        finalidadeAmostra = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', finalidadeAmostraPageUrlPattern);
    });
  });
});
