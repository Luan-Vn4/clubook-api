FROM quay.io/keycloak/keycloak:26.2

# Criar diretório de importação antes de copiar
RUN mkdir -p /opt/keycloak/data/import

COPY ./realm-export.json /opt/keycloak/data/import/realm-export.json

# Comando completo do compose.yml - torna a imagem autocontida
CMD ["start-dev", "--import-realm", "--features=scripts", "--spi-scripting-scripts-enabled=true", "--health-enabled=true"]

LABEL org.opencontainers.image.title="booklub-api-keycloak"
LABEL org.opencontainers.image.description="Keycloak image for local development of booklub-api"
