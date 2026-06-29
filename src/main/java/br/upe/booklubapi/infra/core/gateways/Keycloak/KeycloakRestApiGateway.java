package br.upe.booklubapi.infra.core.gateways.Keycloak;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.upe.booklubapi.utils.KeycloakUtils;
import br.upe.booklubapi.app.auth.dto.AuthBody;
import br.upe.booklubapi.app.auth.dto.AuthResponseDTO;
import br.upe.booklubapi.app.auth.dto.TokenDTO;
import br.upe.booklubapi.app.user.dtos.CreateUserDTO;
import br.upe.booklubapi.app.user.dtos.UserDTO;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;

import br.upe.booklubapi.config.security.KeycloakProperties;
import br.upe.booklubapi.domain.users.entities.User;

import java.util.UUID;

@Component
public class KeycloakRestApiGateway {

        private final WebClient keycloakWebClient;

        private final KeycloakUtils keycloakUtils;

        private final KeycloakProperties keycloakProperties;

        private final ObjectMapper objectMapper;

        public KeycloakRestApiGateway(
                @Qualifier("keycloakWebClient") WebClient keycloakWebClient,
                KeycloakUtils keycloakUtils,
                KeycloakProperties keycloakProperties,
                ObjectMapper objectMapper
        ) {
                this.keycloakWebClient = keycloakWebClient;
                this.keycloakUtils = keycloakUtils;
                this.keycloakProperties = keycloakProperties;
                this.objectMapper = objectMapper;
        }

        public Mono<Void> deleteUserById(UUID uuid) {
                String adminToken = keycloakUtils.getAdminToken();

                return keycloakWebClient
                                .delete()
                                .uri("/admin/realms/" + keycloakProperties.getClientRealm()
                                                + "/users/" + uuid)
                                .header("Authorization", "Bearer " + adminToken)
                                .retrieve()
                                .bodyToMono(Void.class);
        }

        public Mono<Void> updateUserById(User updatedUser, UUID uuid) {
                String adminToken = keycloakUtils.getAdminToken();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("username", updatedUser.getUsername());
                userMap.put("email", updatedUser.getEmail());
                userMap.put("firstName", updatedUser.getFirstName());
                userMap.put("lastName", updatedUser.getLastName());
                userMap.put("enabled", true);

                Map<String, Object> attributes = new HashMap<>();
                String imageUrl = updatedUser.getAttributes().get("imageUrl");
                if (imageUrl != null) {
                        attributes.put("imageUrl", List.of(imageUrl));
                }
                String birthDate = updatedUser.getAttributes().get("birthDate");
                if (birthDate != null) {
                        attributes.put("birthDate", List.of(birthDate));
                }

                userMap.put("attributes", attributes);

                String userJson = "";
                try {
                        userJson = objectMapper.writeValueAsString(userMap);
                } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error converting user map to JSON", e);
                }

                return keycloakWebClient
                                .put()
                                .uri("/admin/realms/" + keycloakProperties.getClientRealm()
                                                + "/users/" + uuid)
                                .header("Authorization", "Bearer " + adminToken)
                                .header("Content-Type", "application/json")
                                .bodyValue(userJson)
                                .retrieve()
                                .bodyToMono(Void.class);
        }

        public Mono<Void> resetUserPassword(String newPassword, UUID uuid) {
                String adminToken = keycloakUtils.getAdminToken();

                Map<String, Object> credentialsRepresentationMap = new HashMap<>();
                credentialsRepresentationMap.put("type", "password");
                credentialsRepresentationMap.put("temporary", "false");
                credentialsRepresentationMap.put("value", newPassword);

                String credentialsJson = "";
                try {
                        credentialsJson = objectMapper.writeValueAsString(credentialsRepresentationMap);
                } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error converting credentials to JSON", e);
                }

                return keycloakWebClient
                                .put()
                                .uri("/admin/realms/" + keycloakProperties.getClientRealm()
                                                + "/users/" + uuid + "/reset-password")
                                .header("Authorization", "Bearer " + adminToken)
                                .header("Content-Type", "application/json")
                                .bodyValue(credentialsJson)
                                .retrieve()
                                .bodyToMono(Void.class);
        }

        public Mono<Void> recoverUserPassword(UUID uuid) {
                String adminToken = keycloakUtils.getAdminToken();

                List<String> actions = Arrays.asList("UPDATE_PASSWORD");

                return keycloakWebClient
                                .put()
                                .uri("/admin/realms/" + keycloakProperties.getClientRealm()
                                                + "/users/" + uuid + "/execute-actions-email?lifespan=1800")
                                .header("Authorization", "Bearer " + adminToken)
                                .header("Content-Type", "application/json")
                                .bodyValue(actions)
                                .retrieve()
                                .bodyToMono(Void.class);
        }

        public Mono<Void> updateProfilePicturePathById(String newProfilePicturePath, UserDTO userBeingUpdated,
                        UUID id) {
                String adminToken = keycloakUtils.getAdminToken();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("username", userBeingUpdated.username());
                userMap.put("email", userBeingUpdated.email());
                userMap.put("firstName", userBeingUpdated.firstName());
                userMap.put("lastName", userBeingUpdated.lastName());

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("imageUrl", List.of(newProfilePicturePath));
                if (userBeingUpdated.birthDate() != null) {
                        attributes.put("birthDate", List.of(userBeingUpdated.birthDate()));
                }
                userMap.put("attributes", attributes);

                String userJson = "";
                try {
                        userJson = objectMapper.writeValueAsString(userMap);
                } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error converting user map to JSON", e);
                }

                return keycloakWebClient
                                .put()
                                .uri("/admin/realms/" + keycloakProperties.getClientRealm()
                                                + "/users/" + id)
                                .header("Authorization", "Bearer " + adminToken)
                                .header("Content-Type", "application/json")
                                .bodyValue(userJson)
                                .retrieve()
                                .bodyToMono(Void.class);
        }

        public Mono<Void> register(CreateUserDTO userDTO) {
                String adminToken = keycloakUtils.getAdminToken();

                Map<String, Object> userMap = new HashMap<>();

                userMap.put("username", userDTO.username());
                userMap.put("email", userDTO.email());
                userMap.put("firstName", userDTO.firstName());
                userMap.put("lastName", userDTO.lastName());
                userMap.put("enabled", true);

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("imageUrl", List.of("null"));//gambiarra pra parar um bug de update user no dia da apresentçaop de elisson
                if (userDTO.birthDate() != null) {
                        attributes.put("birthDate", List.of(userDTO.birthDate()));
                }
                userMap.put("attributes", attributes);

                List<Map<String, Object>> credentials = new ArrayList<>();

                Map<String, Object> credentialsMap = new HashMap<>();

                credentialsMap.put("type", "password");
                credentialsMap.put("temporary", "false");
                credentialsMap.put("value", userDTO.password());

                credentials.add(credentialsMap);

                userMap.put("credentials", credentials);

                String userJson = "";
                try {
                        userJson = objectMapper.writeValueAsString(userMap);
                } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error converting user map to JSON", e);
                }

                return keycloakWebClient
                                .post()
                                .uri("/admin/realms/" + keycloakProperties.getClientRealm()
                                                + "/users")
                                .header("Authorization", "Bearer " + adminToken)
                                .header("Content-Type", "application/json")
                                .bodyValue(userJson)
                                .retrieve()
                                .bodyToMono(Void.class);
        }

        public TokenDTO login(AuthBody authBody) {

                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("client_id", keycloakProperties.getClientId());
                formData.add("client_secret", keycloakProperties.getClientSecret());
                formData.add("username", authBody.username());
                formData.add("password", authBody.password());
                formData.add("grant_type", "password");

                String tokenUrl = "/realms/" + keycloakProperties.getClientRealm()
                                + "/protocol/openid-connect/token";

                JsonNode response = keycloakWebClient.post()
                                .uri(tokenUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(BodyInserters.fromFormData(formData))
                                .retrieve()
                                .bodyToMono(JsonNode.class)
                                .block();

                String accessToken = response.get("access_token").asText();
                String tokenType = response.get("token_type").asText();
                ZonedDateTime expiration = Instant.now().atZone(ZoneId.of("America/Sao_Paulo"))
                                .plusSeconds(response.get("expires_in").asInt());

                return new TokenDTO(accessToken, expiration, tokenType);
        }

}
