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
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const problemaSample = {
    dataZonedDateTime: '2022-02-04T14:04:10.259Z',
    dataLocalDate: '2022-02-04',
    dataInstant: '2022-02-04T04:54:04.202Z',
    dataDuration: 77637,
    descricao: 'Re-engineered',
    criticidade: 'IMEDIATA',
    impacto: 'B2B users',
  };

  let problema: any;
  //let usuario: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/usuarios',
      body: {"chave":"onli","nome":"Avenida","linksExternos":"Dollar"},
    }).then(({ body }) => {
      usuario = body;
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

    cy.intercept('GET', '/api/usuarios', {
      statusCode: 200,
      body: [usuario],
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
    if (usuario) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/usuarios/${usuario.id}`,
      }).then(() => {
        usuario = undefined;
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
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/problema/new$'));
        cy.getEntityCreateUpdateHeading('Problema');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
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
            relator: usuario,
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
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', problemaPageUrlPattern);
      });

      it('edit button click should load edit Problema page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Problema');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', problemaPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Problema', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('problema').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
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
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Problema');
    });

    it.skip('should create an instance of Problema', () => {
      cy.get(`[data-cy="dataZonedDateTime"]`).type('2022-02-04T15:44').should('have.value', '2022-02-04T15:44');

      cy.get(`[data-cy="dataLocalDate"]`).type('2022-02-03').should('have.value', '2022-02-03');

      cy.get(`[data-cy="dataInstant"]`).type('2022-02-04T00:17').should('have.value', '2022-02-04T00:17');

      cy.get(`[data-cy="dataDuration"]`).type('PT54M').should('have.value', 'PT54M');

      cy.get(`[data-cy="descricao"]`).type('monitor Loan').should('have.value', 'monitor Loan');

      cy.get(`[data-cy="criticidade"]`).select('IMEDIATA');

      cy.get(`[data-cy="aceitarFinalizacao"]`).should('not.be.checked');
      cy.get(`[data-cy="aceitarFinalizacao"]`).click().should('be.checked');

      cy.setFieldImageAsBytesOfEntity('foto', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="impacto"]`).type('scale').should('have.value', 'scale');

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
