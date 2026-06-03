package br.upe.booklubapi.infra.users;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import br.upe.booklubapi.domain.users.entities.QUser;
import br.upe.booklubapi.domain.users.entities.User;
import br.upe.booklubapi.domain.users.repository.UserRepository;
import br.upe.booklubapi.domain.users.repository.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Objects;


@Repository
public interface JpaUserRepository
                extends JpaRepository<User, UUID>, UserRepository, QuerydslPredicateExecutor<User> {

}

@Component
@AllArgsConstructor
class JpaUserRepositoryCustomImpl implements UserRepositoryCustom {
    private EntityManager em;

    private BooleanExpression discoverableUsersOnly(QUser user) {
        // user_entity is shared with Keycloak; exclude service accounts and built-in users.
        return user.email.isNotNull()
            .and(user.email.ne(""))
            .and(user.username.notLike("service-account-%"));
    }

    @Override
    public Page<User> findByUsernameContaining(String username, Pageable pageable) {
        final QUser user = QUser.user;
        final boolean hasSearchTerm = username != null && !username.isBlank();

        BooleanExpression filter = discoverableUsersOnly(user);
        if (hasSearchTerm) {
            filter = filter.and(user.username.containsIgnoreCase(username.trim()));
        }

        final List<User> result = new JPAQuery<>(em)
            .select(user)
            .from(user)
            .where(filter)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        final Long total = new JPAQuery<>(em)
            .select(user.count())
            .from(user)
            .where(filter)
            .fetchOne();

        return new PageImpl<>(result, pageable, Objects.requireNonNullElse(total, 0L));
    }
}
