#!/bin/sh
set -e

REQUIRED_VARS="
DB_URL
DB_USERNAME
DB_PASSWORD
KEYCLOAK_CLIENT_ID
KEYCLOAK_CLIENT_SECRET
KEYCLOAK_REALM
KEYCLOAK_AUTH_URL
S3_URL
S3_ACCESS_NAME
S3_ACCESS_SECRET
"

for var in $REQUIRED_VARS; do
  value=$(printenv "$var")
  if [ -z "$value" ]; then
    echo "Error: Environment variable '$var' is not set. Exiting."
    exit 1
  fi
done

echo "All required environment variables are set."
exec java -jar app.jar