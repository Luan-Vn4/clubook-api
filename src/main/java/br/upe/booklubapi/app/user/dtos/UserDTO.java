package br.upe.booklubapi.app.user.dtos;

public record UserDTO(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String imageUrl,
        String birthDate,
        Integer totalClubs) {
}
