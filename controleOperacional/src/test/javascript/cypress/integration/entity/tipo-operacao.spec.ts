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

describe('TipoOperacao e2e test', () => {
  const tipoOperacaoPageUrl = '/tipo-operacao';
  const tipoOperacaoPageUrlPattern = new RegExp('/tipo-operacao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tipoOperacaoSample = { descricao: 'compress Travessa Designer' };

  let tipoOperacao: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-operacaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-operacaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-operacaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoOperacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-operacaos/${tipoOperacao.id}`,
      }).then(() => {
        tipoOperacao = undefined;
      });
    }
  });

  it('TipoOperacaos menu should load TipoOperacaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-operacao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoOperacao').should('exist');
    cy.url().should('match', tipoOperacaoPageUrlPattern);
  });

  describe('TipoOperacao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoOperacaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoOperacao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tipo-operacao/new$'));
        cy.getEntityCreateUpdateHeading('TipoOperacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoOperacaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-operacaos',
          body: tipoOperacaoSample,
        }).then(({ body }) => {
          tipoOperacao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-operacaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tipoOperacao],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoOperacaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoOperacao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoOperacao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoOperacaoPageUrlPattern);
      });

      it('edit button click should load edit TipoOperacao page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoOperacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoOperacaoPageUrlPattern);
      });

      it('last delete button click should delete instance of TipoOperacao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoOperacao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoOperacaoPageUrlPattern);

        tipoOperacao = undefined;
      });
    });
  });

  describe('new TipoOperacao page', () => {
    beforeEach(() => {
      cy.visit(`${tipoOperacaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TipoOperacao');
    });

    it('should create an instance of TipoOperacao', () => {
      cy.get(`[data-cy="descricao"]`).type('Central parse').should('have.value', 'Central parse');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tipoOperacao = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoOperacaoPageUrlPattern);
    });
  });
});
