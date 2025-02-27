package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.data.dao.impl.AuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;


public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private final AuthUserDao authUserDao = new AuthUserDAOJdbc();
    private final AuthorityDao authorityDao = new AuthorityDaoJdbc();

    @Nonnull
    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        authUserDao.create(authUser);
        authorityDao.create(authUser.getAuthorities().toArray(new AuthorityEntity[0]));
        return authUser;
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return authUserDao.findById(id);
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return authUserDao.findByUsername(username);
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDao.remove(user);
    }
}
