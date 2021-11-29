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

describe('D e2e test', () => {
  const dPageUrl = '/d';
  const dPageUrlPattern = new RegExp('/d(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const dSample = {};

  let d: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ds+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ds').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ds/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (d) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ds/${d.id}`,
      }).then(() => {
        d = undefined;
      });
    }
  });

  it('DS menu should load DS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('d');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('D').should('exist');
    cy.url().should('match', dPageUrlPattern);
  });

  describe('D page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create D page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/d/new$'));
        cy.getEntityCreateUpdateHeading('D');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ds',
          body: dSample,
        }).then(({ body }) => {
          d = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ds+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [d],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details D page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('d');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dPageUrlPattern);
      });

      it('edit button click should load edit D page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('D');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dPageUrlPattern);
      });

      it('last delete button click should delete instance of D', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('d').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dPageUrlPattern);

        d = undefined;
      });
    });
  });

  describe('new D page', () => {
    beforeEach(() => {
      cy.visit(`${dPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('D');
    });

    it('should create an instance of D', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        d = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', dPageUrlPattern);
    });
  });
});
