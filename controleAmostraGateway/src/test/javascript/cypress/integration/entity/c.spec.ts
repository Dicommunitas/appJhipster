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

describe('C e2e test', () => {
  const cPageUrl = '/c';
  const cPageUrlPattern = new RegExp('/c(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cSample = {};

  let c: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (c) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cs/${c.id}`,
      }).then(() => {
        c = undefined;
      });
    }
  });

  it('CS menu should load CS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('c');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('C').should('exist');
    cy.url().should('match', cPageUrlPattern);
  });

  describe('C page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create C page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/c/new$'));
        cy.getEntityCreateUpdateHeading('C');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cs',
          body: cSample,
        }).then(({ body }) => {
          c = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [c],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details C page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('c');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cPageUrlPattern);
      });

      it('edit button click should load edit C page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('C');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cPageUrlPattern);
      });

      it('last delete button click should delete instance of C', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('c').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cPageUrlPattern);

        c = undefined;
      });
    });
  });

  describe('new C page', () => {
    beforeEach(() => {
      cy.visit(`${cPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('C');
    });

    it('should create an instance of C', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        c = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cPageUrlPattern);
    });
  });
});
