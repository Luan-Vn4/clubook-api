package br.upe.booklubapi.app.user.dtos;

import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
    @NotNull
    String username,
    
    @NotNull
    String email,
    
    @NotNull
    String firstName,
    
    @NotNull
    String lastName,

    @Nullable
    String birthDate,

    @Nullable
    MultipartFile image,

    @NotNull
    String password
) {}