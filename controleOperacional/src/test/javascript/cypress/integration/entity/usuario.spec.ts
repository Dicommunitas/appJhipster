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

describe('Usuario e2e test', () => {
  const usuarioPageUrl = '/usuario';
  const usuarioPageUrlPattern = new RegExp('/usuario(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const usuarioSample = { chave: 'Refi', nome: 'Gâmbia Division' };

  let usuario: any;
  //let user: any;

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
      url: '/api/users',
      body: {"login":"SSL connecting generate","firstName":"Henrique","lastName":"Nogueira"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/usuarios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/usuarios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/usuarios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/tipo-relatorios', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (usuario) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/usuarios/${usuario.id}`,
      }).then(() => {
        usuario = undefined;
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

  it('Usuarios menu should load Usuarios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('usuario');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Usuario').should('exist');
    cy.url().should('match', usuarioPageUrlPattern);
  });

  describe('Usuario page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(usuarioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Usuario page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/usuario/new$'));
        cy.getEntityCreateUpdateHeading('Usuario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', usuarioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/usuarios',
  
          body: {
            ...usuarioSample,
            user: user,
          },
        }).then(({ body }) => {
          usuario = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/usuarios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [usuario],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(usuarioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(usuarioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Usuario page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('usuario');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', usuarioPageUrlPattern);
      });

      it('edit button click should load edit Usuario page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Usuario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', usuarioPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Usuario', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('usuario').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', usuarioPageUrlPattern);

        usuario = undefined;
      });
    });
  });

  describe('new Usuario page', () => {
    beforeEach(() => {
      cy.visit(`${usuarioPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Usuario');
    });

    it.skip('should create an instance of Usuario', () => {
      cy.get(`[data-cy="chave"]`).type('Corp').should('have.value', 'Corp');

      cy.get(`[data-cy="nome"]`)
        .type('Borracha Cambridgeshire Cambridgeshire')
        .should('have.value', 'Borracha Cambridgeshire Cambridgeshire');

      cy.get(`[data-cy="linksExternos"]`).type('Account Avenida').should('have.value', 'Account Avenida');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        usuario = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', usuarioPageUrlPattern);
    });
  });
});
