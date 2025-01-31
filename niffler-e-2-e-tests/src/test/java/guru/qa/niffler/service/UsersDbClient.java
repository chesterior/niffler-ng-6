package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJDBC;
import guru.qa.niffler.data.dao.impl.AuthorityDaoSpringJDBC;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJDBC;
import guru.qa.niffler.data.entity.AuthUserEntity;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.dataSource;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(CFG.authJdbcUrl())
            )
    );

    public UserJson createUserSpringJdbs(UserJson user) {
        transactionTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword("123456");
                    authUser.setEnable(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = new AuthUserDaoSpringJDBC((dataSource(CFG.authJdbcUrl())))
                            .create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    new AuthorityDaoSpringJDBC((dataSource(CFG.authJdbcUrl())))
                            .create(authorityEntities);
                    return null;
                }
        );

        return UserJson.fromEntity(
                new UdUserDaoSpringJDBC(dataSource(CFG.userdataJdbcUrl()))
                        .createUser(
                                UserEntity.fromJson(user)
                        ), null
        );
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnable(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUserId(authUser.getId());
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

}
