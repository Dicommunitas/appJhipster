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

describe('AlertaProduto e2e test', () => {
  const alertaProdutoPageUrl = '/alerta-produto';
  const alertaProdutoPageUrlPattern = new RegExp('/alerta-produto(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const alertaProdutoSample = { descricao: 'Associate' };

  let alertaProduto: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/alerta-produtos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/alerta-produtos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/alerta-produtos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (alertaProduto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/alerta-produtos/${alertaProduto.id}`,
      }).then(() => {
        alertaProduto = undefined;
      });
    }
  });

  it('AlertaProdutos menu should load AlertaProdutos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('alerta-produto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AlertaProduto').should('exist');
    cy.url().should('match', alertaProdutoPageUrlPattern);
  });

  describe('AlertaProduto page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(alertaProdutoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AlertaProduto page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/alerta-produto/new$'));
        cy.getEntityCreateUpdateHeading('AlertaProduto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', alertaProdutoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/alerta-produtos',
          body: alertaProdutoSample,
        }).then(({ body }) => {
          alertaProduto = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/alerta-produtos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [alertaProduto],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(alertaProdutoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AlertaProduto page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('alertaProduto');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', alertaProdutoPageUrlPattern);
      });

      it('edit button click should load edit AlertaProduto page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AlertaProduto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', alertaProdutoPageUrlPattern);
      });

      it('last delete button click should delete instance of AlertaProduto', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('alertaProduto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', alertaProdutoPageUrlPattern);

        alertaProduto = undefined;
      });
    });
  });

  describe('new AlertaProduto page', () => {
    beforeEach(() => {
      cy.visit(`${alertaProdutoPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AlertaProduto');
    });

    it('should create an instance of AlertaProduto', () => {
      cy.get(`[data-cy="descricao"]`).type('Algodão synthesize Atum').should('have.value', 'Algodão synthesize Atum');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        alertaProduto = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', alertaProdutoPageUrlPattern);
    });
  });
});
