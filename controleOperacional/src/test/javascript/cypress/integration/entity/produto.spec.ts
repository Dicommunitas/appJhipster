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

describe('Produto e2e test', () => {
  const produtoPageUrl = '/produto';
  const produtoPageUrlPattern = new RegExp('/produto(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const produtoSample = { codigoBDEMQ: 'unl', nomeCurto: 'Account Libéria', nomeCompleto: 'pixel navigate Luvas' };

  let produto: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/produtos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/produtos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/produtos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (produto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/produtos/${produto.id}`,
      }).then(() => {
        produto = undefined;
      });
    }
  });

  it('Produtos menu should load Produtos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('produto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Produto').should('exist');
    cy.url().should('match', produtoPageUrlPattern);
  });

  describe('Produto page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(produtoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Produto page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/produto/new$'));
        cy.getEntityCreateUpdateHeading('Produto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', produtoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/produtos',
          body: produtoSample,
        }).then(({ body }) => {
          produto = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/produtos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [produto],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(produtoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Produto page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('produto');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', produtoPageUrlPattern);
      });

      it('edit button click should load edit Produto page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Produto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', produtoPageUrlPattern);
      });

      it('last delete button click should delete instance of Produto', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('produto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', produtoPageUrlPattern);

        produto = undefined;
      });
    });
  });

  describe('new Produto page', () => {
    beforeEach(() => {
      cy.visit(`${produtoPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Produto');
    });

    it('should create an instance of Produto', () => {
      cy.get(`[data-cy="codigoBDEMQ"]`).type('pro').should('have.value', 'pro');

      cy.get(`[data-cy="nomeCurto"]`).type('bi-directional Licenciado connect').should('have.value', 'bi-directional Licenciado connect');

      cy.get(`[data-cy="nomeCompleto"]`).type('Amazonas').should('have.value', 'Amazonas');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        produto = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', produtoPageUrlPattern);
    });
  });
});
