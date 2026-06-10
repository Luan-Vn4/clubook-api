package br.upe.booklubapi.domain.users.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import br.upe.booklubapi.domain.core.repositories.CrudRepository;
import br.upe.booklubapi.domain.core.repositories.QueryableRepository;
import br.upe.booklubapi.domain.users.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Integer countUserClubs(UUID userId);
}
