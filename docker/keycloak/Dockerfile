FROM quay.io/keycloak/keycloak:26.2

COPY ./realm-export.json /opt/keycloak/data/import/realm-export.json

LABEL org.opencontainers.image.title="booklub-api-keycloak"
LABEL org.opencontainers.image.description="Keycloak image for local development of booklub-api"
LABEL org.opencontainers.image.environment="KEYCLOAK_ADMIN, KEYCLOAK_ADMIN_PASSWORD, KC_DB, KC_DB_URL_HOST, KC_DB_URL_DATABASE, KC_DB_USERNAME, KC_DB_PASSWORD, KC_DB_URL_PORT"

<<<<<<< HEAD
# Startup flags live in compose.yml only (avoid duplicating --import-realm).
=======
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
>>>>>>> 2cd68dc20da9c6724737186e3c454150291d3b88
