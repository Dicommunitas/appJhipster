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

describe('TipoFinalidadeAmostra e2e test', () => {
  const tipoFinalidadeAmostraPageUrl = '/tipo-finalidade-amostra';
  const tipoFinalidadeAmostraPageUrlPattern = new RegExp('/tipo-finalidade-amostra(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tipoFinalidadeAmostraSample = { descricao: 'Avenida Rústico', obrigatorioLacre: false };

  let tipoFinalidadeAmostra: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-finalidade-amostras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-finalidade-amostras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-finalidade-amostras/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoFinalidadeAmostra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-finalidade-amostras/${tipoFinalidadeAmostra.id}`,
      }).then(() => {
        tipoFinalidadeAmostra = undefined;
      });
    }
  });

  it('TipoFinalidadeAmostras menu should load TipoFinalidadeAmostras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-finalidade-amostra');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoFinalidadeAmostra').should('exist');
    cy.url().should('match', tipoFinalidadeAmostraPageUrlPattern);
  });

  describe('TipoFinalidadeAmostra page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoFinalidadeAmostraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoFinalidadeAmostra page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/tipo-finalidade-amostra/new$'));
        cy.getEntityCreateUpdateHeading('TipoFinalidadeAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFinalidadeAmostraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-finalidade-amostras',
          body: tipoFinalidadeAmostraSample,
        }).then(({ body }) => {
          tipoFinalidadeAmostra = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-finalidade-amostras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tipoFinalidadeAmostra],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoFinalidadeAmostraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoFinalidadeAmostra page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoFinalidadeAmostra');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFinalidadeAmostraPageUrlPattern);
      });

      it('edit button click should load edit TipoFinalidadeAmostra page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoFinalidadeAmostra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFinalidadeAmostraPageUrlPattern);
      });

      it('last delete button click should delete instance of TipoFinalidadeAmostra', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoFinalidadeAmostra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFinalidadeAmostraPageUrlPattern);

        tipoFinalidadeAmostra = undefined;
      });
    });
  });

  describe('new TipoFinalidadeAmostra page', () => {
    beforeEach(() => {
      cy.visit(`${tipoFinalidadeAmostraPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TipoFinalidadeAmostra');
    });

    it('should create an instance of TipoFinalidadeAmostra', () => {
      cy.get(`[data-cy="descricao"]`).type('Livros invoice recontextualize').should('have.value', 'Livros invoice recontextualize');

      cy.get(`[data-cy="obrigatorioLacre"]`).should('not.be.checked');
      cy.get(`[data-cy="obrigatorioLacre"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tipoFinalidadeAmostra = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoFinalidadeAmostraPageUrlPattern);
    });
  });
});
