package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorityDao {

    void create(AuthorityEntity... authority);

    Optional<AuthorityEntity> findById(UUID id);

    Optional<AuthorityEntity> findByUserId(UUID username);

    void remove(AuthorityEntity authAuthority);

    List<AuthorityEntity> findAll();
}
