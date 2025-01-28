package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthorityEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthorityDao {

    void create(AuthorityEntity... authority);

    Optional<AuthorityEntity> findById(UUID id);

    Optional<AuthorityEntity> findByUserId(String username);

    void deleteById(AuthorityEntity authAuthority);
}
