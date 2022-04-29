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
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const lembreteSample = { nome: 'Berkshire User-friendly', descricao: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let lembrete: any;

  beforeEach(() => {
    cy.login(username, password);
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
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/lembrete/new$'));
        cy.getEntityCreateUpdateHeading('Lembrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
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
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', lembretePageUrlPattern);
      });

      it('edit button click should load edit Lembrete page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lembrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', lembretePageUrlPattern);
      });

      it('last delete button click should delete instance of Lembrete', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('lembrete').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
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
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Lembrete');
    });

    it('should create an instance of Lembrete', () => {
      cy.get(`[data-cy="nome"]`).type('e-services value-added monitor').should('have.value', 'e-services value-added monitor');

      cy.get(`[data-cy="descricao"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="createdBy"]`).type('concept Rústico').should('have.value', 'concept Rústico');

      cy.get(`[data-cy="createdDate"]`).type('2022-04-28T10:14').should('have.value', '2022-04-28T10:14');

      cy.get(`[data-cy="lastModifiedBy"]`).type('turquesa Acre').should('have.value', 'turquesa Acre');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2022-04-28T04:24').should('have.value', '2022-04-28T04:24');

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
