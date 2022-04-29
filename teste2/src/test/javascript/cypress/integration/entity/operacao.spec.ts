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

describe('Operacao e2e test', () => {
  const operacaoPageUrl = '/operacao';
  const operacaoPageUrlPattern = new RegExp('/operacao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const operacaoSample = { descricao: 'Peixe hierarchy Open-source', volumePeso: 32003 };

  let operacao: any;
  let produto: any;
  let tipoOperacao: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/produtos',
      body: { codigoBDEMQ: 'Fre', nomeCurto: '(customarily stable interface', nomeCompleto: 'Genérico Lead' },
    }).then(({ body }) => {
      produto = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/tipo-operacaos',
      body: { descricao: 'reinvent sexy' },
    }).then(({ body }) => {
      tipoOperacao = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/operacaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/operacaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/operacaos/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/produtos', {
      statusCode: 200,
      body: [produto],
    });

    cy.intercept('GET', '/api/tipo-operacaos', {
      statusCode: 200,
      body: [tipoOperacao],
    });
  });

  afterEach(() => {
    if (operacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/operacaos/${operacao.id}`,
      }).then(() => {
        operacao = undefined;
      });
    }
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
    if (tipoOperacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-operacaos/${tipoOperacao.id}`,
      }).then(() => {
        tipoOperacao = undefined;
      });
    }
  });

  it('Operacaos menu should load Operacaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('operacao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Operacao').should('exist');
    cy.url().should('match', operacaoPageUrlPattern);
  });

  describe('Operacao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(operacaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Operacao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/operacao/new$'));
        cy.getEntityCreateUpdateHeading('Operacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operacaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/operacaos',
          body: {
            ...operacaoSample,
            produto: produto,
            tipoOperacao: tipoOperacao,
          },
        }).then(({ body }) => {
          operacao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/operacaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/operacaos?page=0&size=20>; rel="last",<http://localhost/api/operacaos?page=0&size=20>; rel="first"',
              },
              body: [operacao],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(operacaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Operacao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('operacao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operacaoPageUrlPattern);
      });

      it('edit button click should load edit Operacao page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Operacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operacaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Operacao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('operacao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operacaoPageUrlPattern);

        operacao = undefined;
      });
    });
  });

  describe('new Operacao page', () => {
    beforeEach(() => {
      cy.visit(`${operacaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Operacao');
    });

    it('should create an instance of Operacao', () => {
      cy.get(`[data-cy="descricao"]`).type('quantify parallelism').should('have.value', 'quantify parallelism');

      cy.get(`[data-cy="volumePeso"]`).type('14105').should('have.value', '14105');

      cy.get(`[data-cy="inicio"]`).type('2022-04-28T13:46').should('have.value', '2022-04-28T13:46');

      cy.get(`[data-cy="fim"]`).type('2022-04-28T15:07').should('have.value', '2022-04-28T15:07');

      cy.get(`[data-cy="quantidadeAmostras"]`).type('60880').should('have.value', '60880');

      cy.get(`[data-cy="observacao"]`).type('azul').should('have.value', 'azul');

      cy.get(`[data-cy="createdBy"]`).type('set').should('have.value', 'set');

      cy.get(`[data-cy="createdDate"]`).type('2022-04-28T23:24').should('have.value', '2022-04-28T23:24');

      cy.get(`[data-cy="lastModifiedBy"]`).type('e-commerce program').should('have.value', 'e-commerce program');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2022-04-28T23:30').should('have.value', '2022-04-28T23:30');

      cy.get(`[data-cy="produto"]`).select(1);
      cy.get(`[data-cy="tipoOperacao"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        operacao = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', operacaoPageUrlPattern);
    });
  });
});
