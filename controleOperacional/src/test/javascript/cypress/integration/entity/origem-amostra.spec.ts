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

describe('OrigemAmostra e2e test', () => {
  const origemAmostraPageUrl = '/origem-amostra';
  const origemAmostraPageUrlPattern = new RegExp('/origem-amostra(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const origemAmostraSample = { descricao: 'Loan Salada Infrastructure' };

  let origemAmostra: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/origem-amostras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/origem-amostras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/origem-amostras/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (origemAmostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/origem-amostras/${origemAmostra.id}`,
      }).then(() => {
        origemAmostra = undefined;
      });
    }
  });

  it('OrigemAmostras menu should load OrigemAmostras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('origem-amostra');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrigemAmostra').should('exist');
    cy.url().should('match', origemAmostraPageUrlPattern);
  });

  describe('OrigemAmostra page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(origemAmostraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrigemAmostra page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/origem-amostra/new$'));
        cy.getEntityCreateUpdateHeading('OrigemAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', origemAmostraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/origem-amostras',
          body: origemAmostraSample,
        }).then(({ body }) => {
          origemAmostra = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/origem-amostras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [origemAmostra],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(origemAmostraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrigemAmostra page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('origemAmostra');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', origemAmostraPageUrlPattern);
      });

      it('edit button click should load edit OrigemAmostra page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrigemAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', origemAmostraPageUrlPattern);
      });

      it('last delete button click should delete instance of OrigemAmostra', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('origemAmostra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', origemAmostraPageUrlPattern);

        origemAmostra = undefined;
      });
    });
  });

  describe('new OrigemAmostra page', () => {
    beforeEach(() => {
      cy.visit(`${origemAmostraPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('OrigemAmostra');
    });

    it('should create an instance of OrigemAmostra', () => {
      cy.get(`[data-cy="descricao"]`).type('Dollar Identity').should('have.value', 'Dollar Identity');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        origemAmostra = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', origemAmostraPageUrlPattern);
    });
  });
});
