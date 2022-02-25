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

describe('Lembrete e2e test', () => {
  const lembretePageUrl = '/lembrete';
  const lembretePageUrlPattern = new RegExp('/lembrete(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const lembreteSample = { nome: 'concept Rústico', descricao: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let lembrete: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lembretes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lembretes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lembretes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (lembrete) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lembretes/${lembrete.id}`,
      }).then(() => {
        lembrete = undefined;
      });
    }
  });

  it('Lembretes menu should load Lembretes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lembrete');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Lembrete').should('exist');
    cy.url().should('match', lembretePageUrlPattern);
  });

  describe('Lembrete page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(lembretePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Lembrete page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lembrete/new$'));
        cy.getEntityCreateUpdateHeading('Lembrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', lembretePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lembretes',
          body: lembreteSample,
        }).then(({ body }) => {
          lembrete = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lembretes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [lembrete],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(lembretePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Lembrete page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('lembrete');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', lembretePageUrlPattern);
      });

      it('edit button click should load edit Lembrete page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lembrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', lembretePageUrlPattern);
      });

      it('last delete button click should delete instance of Lembrete', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('lembrete').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', lembretePageUrlPattern);

        lembrete = undefined;
      });
    });
  });

  describe('new Lembrete page', () => {
    beforeEach(() => {
      cy.visit(`${lembretePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Lembrete');
    });

    it('should create an instance of Lembrete', () => {
      cy.get(`[data-cy="nome"]`).type('e-services value-added monitor').should('have.value', 'e-services value-added monitor');

      cy.get(`[data-cy="descricao"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        lembrete = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', lembretePageUrlPattern);
    });
  });
});