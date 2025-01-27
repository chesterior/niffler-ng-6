package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.AuthAuthorityEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {

    void create(AuthAuthorityEntity... authority);

    Optional<AuthAuthorityEntity> findById(UUID id);

    Optional<AuthAuthorityEntity> findByUserId(String username);

    void deleteById(AuthAuthorityEntity authAuthority);
}
