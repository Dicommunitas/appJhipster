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

describe('Status e2e test', () => {
  const statusPageUrl = '/status';
  const statusPageUrlPattern = new RegExp('/status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const statusSample = { descricao: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', prazo: '2022-02-25' };

  let status: any;
  //let usuario: any;
  //let problema: any;

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
      body: {"chave":"Prin","nome":"Plástico","linksExternos":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ="},
    }).then(({ body }) => {
      usuario = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/problemas',
      body: {"data":"2022-02-24","descricao":"Account transmitter","criticidade":"IMEDIATA","aceitarFinalizacao":true,"foto":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fotoContentType":"unknown","impacto":"Macio"},
    }).then(({ body }) => {
      problema = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/statuses/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/usuarios', {
      statusCode: 200,
      body: [usuario],
    });

    cy.intercept('GET', '/api/problemas', {
      statusCode: 200,
      body: [problema],
    });

  });
   */

  afterEach(() => {
    if (status) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/statuses/${status.id}`,
      }).then(() => {
        status = undefined;
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
    if (problema) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/problemas/${problema.id}`,
      }).then(() => {
        problema = undefined;
      });
    }
  });
   */

  it('Statuses menu should load Statuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Status').should('exist');
    cy.url().should('match', statusPageUrlPattern);
  });

  describe('Status page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Status page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/status/new$'));
        cy.getEntityCreateUpdateHeading('Status');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', statusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/statuses',
  
          body: {
            ...statusSample,
            relator: usuario,
            responsavel: usuario,
            problema: problema,
          },
        }).then(({ body }) => {
          status = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [status],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(statusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(statusPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Status page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('status');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', statusPageUrlPattern);
      });

      it('edit button click should load edit Status page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Status');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', statusPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Status', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('status').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', statusPageUrlPattern);

        status = undefined;
      });
    });
  });

  describe('new Status page', () => {
    beforeEach(() => {
      cy.visit(`${statusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Status');
    });

    it.skip('should create an instance of Status', () => {
      cy.get(`[data-cy="descricao"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="prazo"]`).type('2022-02-25').should('have.value', '2022-02-25');

      cy.get(`[data-cy="resolvido"]`).should('not.be.checked');
      cy.get(`[data-cy="resolvido"]`).click().should('be.checked');

      cy.get(`[data-cy="relator"]`).select(1);
      cy.get(`[data-cy="responsavel"]`).select(1);
      cy.get(`[data-cy="problema"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        status = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', statusPageUrlPattern);
    });
  });
});
