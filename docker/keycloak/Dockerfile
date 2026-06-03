FROM quay.io/keycloak/keycloak:26.2

COPY ./realm-export.json /opt/keycloak/data/import/realm-export.json

LABEL org.opencontainers.image.title="booklub-api-keycloak"
LABEL org.opencontainers.image.description="Keycloak image for local development of booklub-api"
LABEL org.opencontainers.image.environment="KEYCLOAK_ADMIN, KEYCLOAK_ADMIN_PASSWORD, KC_DB, KC_DB_URL_HOST, KC_DB_URL_DATABASE, KC_DB_USERNAME, KC_DB_PASSWORD, KC_DB_URL_PORT"

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
