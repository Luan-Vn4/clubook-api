package br.upe.booklubapi.app.user.dtos;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;

import jakarta.annotation.Nullable;

public record UpdateUserDTO(
    @JsonDeserialize(using = UUIDDeserializer.class)
    UUID id,
    @Nullable
    String username,
    @Nullable
    String email,
    @Nullable
    String firstName,
    @Nullable
    String lastName,
    @Nullable
    String birthDate,
    @Nullable
    MultipartFile image
) {}