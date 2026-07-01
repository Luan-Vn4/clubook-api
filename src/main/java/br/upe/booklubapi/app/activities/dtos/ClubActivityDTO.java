package br.upe.booklubapi.app.activities.dtos;

import jakarta.annotation.Nullable;

import java.util.UUID;

public interface ClubActivityDTO extends ActivityDTO {

    UUID clubId();

    @Nullable String clubName();

    @Nullable String clubPhotoUrl();

}
