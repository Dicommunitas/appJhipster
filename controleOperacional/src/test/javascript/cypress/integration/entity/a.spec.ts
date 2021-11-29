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

describe('A e2e test', () => {
  const aPageUrl = '/a';
  const aPageUrlPattern = new RegExp('/a(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const aSample = {};

  let a: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/as+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/as').as('postEntityRequest');
    cy.intercept('DELETE', '/api/as/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (a) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/as/${a.id}`,
      }).then(() => {
        a = undefined;
      });
    }
  });

  it('AS menu should load AS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('a');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('A').should('exist');
    cy.url().should('match', aPageUrlPattern);
  });

  describe('A page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create A page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/a/new$'));
        cy.getEntityCreateUpdateHeading('A');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', aPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/as',
          body: aSample,
        }).then(({ body }) => {
          a = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/as+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [a],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(aPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details A page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('a');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', aPageUrlPattern);
      });

      it('edit button click should load edit A page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('A');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', aPageUrlPattern);
      });

      it('last delete button click should delete instance of A', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('a').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', aPageUrlPattern);

        a = undefined;
      });
    });
  });

  describe('new A page', () => {
    beforeEach(() => {
      cy.visit(`${aPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('A');
    });

    it('should create an instance of A', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        a = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', aPageUrlPattern);
    });
  });
});
