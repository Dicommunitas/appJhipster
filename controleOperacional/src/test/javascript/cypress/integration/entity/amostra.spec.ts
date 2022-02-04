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

describe('Amostra e2e test', () => {
  const amostraPageUrl = '/amostra';
  const amostraPageUrlPattern = new RegExp('/amostra(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const amostraSample = {};

  let amostra: any;
  //let operacao: any;
  //let origemAmostra: any;
  //let produto: any;
  //let tipoAmostra: any;

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
      url: '/api/operacaos',
      body: {"descricao":"Orchestrator","volume":32077,"inicio":"2022-02-03T16:54:45.755Z","fim":"2022-02-04T05:50:08.791Z","quantidadeAmostras":73104,"observacao":"payment TCP"},
    }).then(({ body }) => {
      operacao = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/origem-amostras',
      body: {"descricao":"Catarina navigate","emUso":false},
    }).then(({ body }) => {
      origemAmostra = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/produtos',
      body: {"codigo":"Cas","nomeCurto":"Turismo virtual maximized","nomeCompleto":"optical SQL"},
    }).then(({ body }) => {
      produto = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/tipo-amostras',
      body: {"descricao":"integrate"},
    }).then(({ body }) => {
      tipoAmostra = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/amostras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/amostras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/amostras/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/finalidade-amostras', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/operacaos', {
      statusCode: 200,
      body: [operacao],
    });

    cy.intercept('GET', '/api/origem-amostras', {
      statusCode: 200,
      body: [origemAmostra],
    });

    cy.intercept('GET', '/api/produtos', {
      statusCode: 200,
      body: [produto],
    });

    cy.intercept('GET', '/api/tipo-amostras', {
      statusCode: 200,
      body: [tipoAmostra],
    });

    cy.intercept('GET', '/api/usuarios', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (amostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amostras/${amostra.id}`,
      }).then(() => {
        amostra = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (operacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/operacaos/${operacao.id}`,
      }).then(() => {
        operacao = undefined;
      });
    }
    if (origemAmostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/origem-amostras/${origemAmostra.id}`,
      }).then(() => {
        origemAmostra = undefined;
      });
    }
    if (produto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/produtos/${produto.id}`,
      }).then(() => {
        produto = undefined;
      });
    }
    if (tipoAmostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-amostras/${tipoAmostra.id}`,
      }).then(() => {
        tipoAmostra = undefined;
      });
    }
  });
   */

  it('Amostras menu should load Amostras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('amostra');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Amostra').should('exist');
    cy.url().should('match', amostraPageUrlPattern);
  });

  describe('Amostra page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(amostraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Amostra page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/amostra/new$'));
        cy.getEntityCreateUpdateHeading('Amostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amostraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/amostras',
  
          body: {
            ...amostraSample,
            operacao: operacao,
            origemAmostra: origemAmostra,
            produto: produto,
            tipoAmostra: tipoAmostra,
          },
        }).then(({ body }) => {
          amostra = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/amostras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [amostra],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(amostraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(amostraPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Amostra page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('amostra');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amostraPageUrlPattern);
      });

      it('edit button click should load edit Amostra page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Amostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amostraPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Amostra', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('amostra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amostraPageUrlPattern);

        amostra = undefined;
      });
    });
  });

  describe('new Amostra page', () => {
    beforeEach(() => {
      cy.visit(`${amostraPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Amostra');
    });

    it.skip('should create an instance of Amostra', () => {
      cy.get(`[data-cy="dataHora"]`).type('2022-02-04T04:21').should('have.value', '2022-02-04T04:21');

      cy.get(`[data-cy="observacao"]`).type('Filipinas').should('have.value', 'Filipinas');

      cy.get(`[data-cy="identificadorExterno"]`).type('Focused').should('have.value', 'Focused');

      cy.get(`[data-cy="amostraNoLaboratorio"]`).should('not.be.checked');
      cy.get(`[data-cy="amostraNoLaboratorio"]`).click().should('be.checked');

      cy.get(`[data-cy="operacao"]`).select(1);
      cy.get(`[data-cy="origemAmostra"]`).select(1);
      cy.get(`[data-cy="produto"]`).select(1);
      cy.get(`[data-cy="tipoAmostra"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        amostra = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', amostraPageUrlPattern);
    });
  });
});
