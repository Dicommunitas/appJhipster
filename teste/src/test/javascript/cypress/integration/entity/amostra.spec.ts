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
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const amostraSample = {};

  let amostra: any;
  //let operacao: any;
  //let origemAmostra: any;
  //let tipoAmostra: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/operacaos',
      body: {"descricao":"Orchestrator","volumePeso":32077,"inicio":"2022-04-10T20:09:29.755Z","fim":"2022-04-11T09:04:52.791Z","quantidadeAmostras":73104,"observacao":"payment TCP","createdBy":"Forward Computador envisioneer","createdDate":"2022-04-11T02:43:06.240Z","lastModifiedBy":"applications","lastModifiedDate":"2022-04-11T04:49:41.708Z"},
    }).then(({ body }) => {
      operacao = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/origem-amostras',
      body: {"descricao":"Catarina navigate"},
    }).then(({ body }) => {
      origemAmostra = body;
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

    cy.intercept('GET', '/api/tipo-amostras', {
      statusCode: 200,
      body: [tipoAmostra],
    });

    cy.intercept('GET', '/api/users', {
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
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/amostra/new$'));
        cy.getEntityCreateUpdateHeading('Amostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
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
              headers: {
                link: '<http://localhost/api/amostras?page=0&size=20>; rel="last",<http://localhost/api/amostras?page=0&size=20>; rel="first"',
              },
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
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amostraPageUrlPattern);
      });

      it('edit button click should load edit Amostra page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Amostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amostraPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Amostra', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('amostra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
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
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Amostra');
    });

    it.skip('should create an instance of Amostra', () => {
      cy.get(`[data-cy="dataHoraColeta"]`).type('2022-04-11T07:36').should('have.value', '2022-04-11T07:36');

      cy.get(`[data-cy="observacao"]`).type('Filipinas').should('have.value', 'Filipinas');

      cy.get(`[data-cy="identificadorExterno"]`).type('Focused').should('have.value', 'Focused');

      cy.get(`[data-cy="recebimentoNoLaboratorio"]`).type('2022-04-11T09:36').should('have.value', '2022-04-11T09:36');

      cy.get(`[data-cy="createdBy"]`).type('Associate').should('have.value', 'Associate');

      cy.get(`[data-cy="createdDate"]`).type('2022-04-11T16:31').should('have.value', '2022-04-11T16:31');

      cy.get(`[data-cy="lastModifiedBy"]`).type('integrate schemas').should('have.value', 'integrate schemas');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2022-04-11T07:15').should('have.value', '2022-04-11T07:15');

      cy.get(`[data-cy="operacao"]`).select(1);
      cy.get(`[data-cy="origemAmostra"]`).select(1);
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
