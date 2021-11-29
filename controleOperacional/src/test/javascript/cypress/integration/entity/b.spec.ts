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

describe('B e2e test', () => {
  const bPageUrl = '/b';
  const bPageUrlPattern = new RegExp('/b(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const bSample = {};

  let b: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (b) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bs/${b.id}`,
      }).then(() => {
        b = undefined;
      });
    }
  });

  it('BS menu should load BS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('b');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('B').should('exist');
    cy.url().should('match', bPageUrlPattern);
  });

  describe('B page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create B page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/b/new$'));
        cy.getEntityCreateUpdateHeading('B');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bs',
          body: bSample,
        }).then(({ body }) => {
          b = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [b],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(bPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details B page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('b');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bPageUrlPattern);
      });

      it('edit button click should load edit B page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('B');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bPageUrlPattern);
      });

      it('last delete button click should delete instance of B', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('b').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bPageUrlPattern);

        b = undefined;
      });
    });
  });

  describe('new B page', () => {
    beforeEach(() => {
      cy.visit(`${bPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('B');
    });

    it('should create an instance of B', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        b = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', bPageUrlPattern);
    });
  });
});
