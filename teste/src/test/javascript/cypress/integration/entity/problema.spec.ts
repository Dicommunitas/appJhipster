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

describe('Problema e2e test', () => {
  const problemaPageUrl = '/problema';
  const problemaPageUrlPattern = new RegExp('/problema(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const problemaSample = { dataVerificacao: '2022-04-12', descricao: 'B2B users', criticidade: 'IMEDIATA', impacto: 'Rodovia strategy' };

  let problema: any;
  //let user: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"multi-byte ROI Future-proofed","firstName":"Víctor","lastName":"Melo"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/problemas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/problemas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/problemas/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (problema) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/problemas/${problema.id}`,
      }).then(() => {
        problema = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Problemas menu should load Problemas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('problema');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Problema').should('exist');
    cy.url().should('match', problemaPageUrlPattern);
  });

  describe('Problema page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(problemaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Problema page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/problema/new$'));
        cy.getEntityCreateUpdateHeading('Problema');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', problemaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/problemas',
          body: {
            ...problemaSample,
            relator: user,
          },
        }).then(({ body }) => {
          problema = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/problemas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/problemas?page=0&size=20>; rel="last",<http://localhost/api/problemas?page=0&size=20>; rel="first"',
              },
              body: [problema],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(problemaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(problemaPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Problema page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('problema');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', problemaPageUrlPattern);
      });

      it('edit button click should load edit Problema page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Problema');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', problemaPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Problema', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('problema').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', problemaPageUrlPattern);

        problema = undefined;
      });
    });
  });

  describe('new Problema page', () => {
    beforeEach(() => {
      cy.visit(`${problemaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Problema');
    });

    it.skip('should create an instance of Problema', () => {
      cy.get(`[data-cy="dataVerificacao"]`).type('2022-04-12').should('have.value', '2022-04-12');

      cy.get(`[data-cy="descricao"]`).type('invoice monitor Loan').should('have.value', 'invoice monitor Loan');

      cy.get(`[data-cy="criticidade"]`).select('IMEDIATA');

      cy.get(`[data-cy="impacto"]`).type('Fantástico Prático Bicicleta').should('have.value', 'Fantástico Prático Bicicleta');

      cy.get(`[data-cy="dataFinalizacao"]`).type('2022-04-12').should('have.value', '2022-04-12');

      cy.setFieldImageAsBytesOfEntity('foto', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="relator"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        problema = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', problemaPageUrlPattern);
    });
  });
});
