package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJDBC;
import guru.qa.niffler.data.dao.impl.AuthorityDaoSpringJDBC;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.authJdbcUrl();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJDBC();
    private final AuthorityDao authorityDao = new AuthorityDaoSpringJDBC();

    @Nonnull
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        authUserDao.create(user);
        authorityDao.create(user.getAuthorities().toArray(new AuthorityEntity[0]));
        return user;
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                    SELECT a.id as authority_id,
                                   authority,
                                   user_id as id,
                                   u.username,
                                   u.password,
                                   u.enabled,
                                   u.account_non_expired,
                                   u.account_non_locked,
                                   u.credentials_non_expired
                                   FROM "user" u join authority a on u.id = a.user_id WHERE u.id = ?
                                """,
                        AuthUserEntityExtractor.instance,
                        id
                )
        );
    }

    @Nonnull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                    SELECT a.id as authority_id,
                                   authority,
                                   user_id as id,
                                   u.username,
                                   u.password,
                                   u.enabled,
                                   u.account_non_expired,
                                   u.account_non_locked,
                                   u.credentials_non_expired
                                   FROM "user" u join authority a on u.id = a.user_id WHERE u.username = ?
                                """,
                        AuthUserEntityExtractor.instance,
                        username
                )
        );
    }

    @Override
    public void remove(AuthUserEntity authUser) {
        jdbcTemplate.update(
                "DELETE FROM \"user\" WHERE id =?", authUser.getId()
        );
    }
}
