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

describe('TipoRelatorio e2e test', () => {
  const tipoRelatorioPageUrl = '/tipo-relatorio';
  const tipoRelatorioPageUrlPattern = new RegExp('/tipo-relatorio(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tipoRelatorioSample = { nome: 'flexibility Aço' };

  let tipoRelatorio: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-relatorios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-relatorios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-relatorios/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoRelatorio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-relatorios/${tipoRelatorio.id}`,
      }).then(() => {
        tipoRelatorio = undefined;
      });
    }
  });

  it('TipoRelatorios menu should load TipoRelatorios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-relatorio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoRelatorio').should('exist');
    cy.url().should('match', tipoRelatorioPageUrlPattern);
  });

  describe('TipoRelatorio page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoRelatorioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoRelatorio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tipo-relatorio/new$'));
        cy.getEntityCreateUpdateHeading('TipoRelatorio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoRelatorioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-relatorios',
          body: tipoRelatorioSample,
        }).then(({ body }) => {
          tipoRelatorio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-relatorios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tipoRelatorio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoRelatorioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoRelatorio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoRelatorio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoRelatorioPageUrlPattern);
      });

      it('edit button click should load edit TipoRelatorio page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoRelatorio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoRelatorioPageUrlPattern);
      });

      it('last delete button click should delete instance of TipoRelatorio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoRelatorio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoRelatorioPageUrlPattern);

        tipoRelatorio = undefined;
      });
    });
  });

  describe('new TipoRelatorio page', () => {
    beforeEach(() => {
      cy.visit(`${tipoRelatorioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TipoRelatorio');
    });

    it('should create an instance of TipoRelatorio', () => {
      cy.get(`[data-cy="nome"]`).type('Director XML integrated').should('have.value', 'Director XML integrated');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tipoRelatorio = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoRelatorioPageUrlPattern);
    });
  });
});
