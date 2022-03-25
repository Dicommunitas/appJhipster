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

describe('TipoAmostra e2e test', () => {
  const tipoAmostraPageUrl = '/tipo-amostra';
  const tipoAmostraPageUrlPattern = new RegExp('/tipo-amostra(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tipoAmostraSample = { descricao: 'Jamaica Algodão' };

  let tipoAmostra: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-amostras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-amostras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-amostras/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoAmostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-amostras/${tipoAmostra.id}`,
      }).then(() => {
        tipoAmostra = undefined;
      });
    }
  });

  it('TipoAmostras menu should load TipoAmostras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-amostra');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoAmostra').should('exist');
    cy.url().should('match', tipoAmostraPageUrlPattern);
  });

  describe('TipoAmostra page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoAmostraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoAmostra page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/tipo-amostra/new$'));
        cy.getEntityCreateUpdateHeading('TipoAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoAmostraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-amostras',
          body: tipoAmostraSample,
        }).then(({ body }) => {
          tipoAmostra = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-amostras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tipoAmostra],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoAmostraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoAmostra page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoAmostra');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoAmostraPageUrlPattern);
      });

      it('edit button click should load edit TipoAmostra page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoAmostraPageUrlPattern);
      });

      it('last delete button click should delete instance of TipoAmostra', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoAmostra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoAmostraPageUrlPattern);

        tipoAmostra = undefined;
      });
    });
  });

  describe('new TipoAmostra page', () => {
    beforeEach(() => {
      cy.visit(`${tipoAmostraPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TipoAmostra');
    });

    it('should create an instance of TipoAmostra', () => {
      cy.get(`[data-cy="descricao"]`).type('Dynamic even-keeled proactive').should('have.value', 'Dynamic even-keeled proactive');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tipoAmostra = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoAmostraPageUrlPattern);
    });
  });
});
