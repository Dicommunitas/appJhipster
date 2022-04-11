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

describe('Relatorio e2e test', () => {
  const relatorioPageUrl = '/relatorio';
  const relatorioPageUrlPattern = new RegExp('/relatorio(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const relatorioSample = { dataHora: '2022-04-11T06:17:44.678Z', relato: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' };

  let relatorio: any;
  //let user: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"Chapéu","firstName":"Beatriz","lastName":"Moreira"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/relatorios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/relatorios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/relatorios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/tipo-relatorios', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (relatorio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/relatorios/${relatorio.id}`,
      }).then(() => {
        relatorio = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Relatorios menu should load Relatorios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('relatorio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Relatorio').should('exist');
    cy.url().should('match', relatorioPageUrlPattern);
  });

  describe('Relatorio page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(relatorioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Relatorio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/relatorio/new$'));
        cy.getEntityCreateUpdateHeading('Relatorio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatorioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/relatorios',
          body: {
            ...relatorioSample,
            responsavel: user,
          },
        }).then(({ body }) => {
          relatorio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/relatorios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/relatorios?page=0&size=20>; rel="last",<http://localhost/api/relatorios?page=0&size=20>; rel="first"',
              },
              body: [relatorio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(relatorioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(relatorioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Relatorio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('relatorio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatorioPageUrlPattern);
      });

      it('edit button click should load edit Relatorio page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Relatorio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatorioPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Relatorio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('relatorio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatorioPageUrlPattern);

        relatorio = undefined;
      });
    });
  });

  describe('new Relatorio page', () => {
    beforeEach(() => {
      cy.visit(`${relatorioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Relatorio');
    });

    it.skip('should create an instance of Relatorio', () => {
      cy.get(`[data-cy="dataHora"]`).type('2022-04-11T08:13').should('have.value', '2022-04-11T08:13');

      cy.get(`[data-cy="relato"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="linksExternos"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="responsavel"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        relatorio = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', relatorioPageUrlPattern);
    });
  });
});
