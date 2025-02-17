package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UdUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UdUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UdUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;
import static utils.RandomDataUtils.randomUsername;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    //JDBC
    private final AuthUserDao authUserDaoJDBC = new AuthUserDAOJdbc();
    private final AuthorityDao authorityDaoJDBC = new AuthorityDaoJdbc();
    private final UdUserDao udUserDaoJDBC = new UdUserDaoJdbc();

    //SpringJDBC
    private final AuthUserDao authUserDaoSpringJDBC = new AuthUserDaoSpringJDBC();
    private final AuthorityDao authorityDaoSpringJDBC = new AuthorityDaoSpringJDBC();
    private final UdUserDao udUserDaoSpringJDBC = new UdUserDaoSpringJDBC();

    //RepositoryJdbc
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UdUserRepository udUserRepository = new UdUserRepositoryJdbc();

    //RepositoryHibernate
    private final AuthUserRepository authUserRepositoryHibernate = new AuthUserRepositoryHibernate();
    private final UdUserRepository udUserRepositoryHibernate = new UdUserRepositoryHibernate();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new ChainedTransactionManager
                    (new JdbcTransactionManager(dataSource(CFG.authJdbcUrl())),
                            new JdbcTransactionManager(dataSource(CFG.userdataJdbcUrl())))

    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );


    //JDBC
    public UserJson createUserJdbcTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("123456"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoJDBC.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authorityDaoJDBC.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDaoJDBC.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUserJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("123456"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoJDBC.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authorityDaoJDBC.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDaoJDBC.create(UserEntity.fromJson(user)),
                null
        );
    }


    //SpringJdbs
    public UserJson createUserSpringJdbsTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("123456"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoSpringJDBC.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authorityDaoSpringJDBC.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDaoSpringJDBC.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("123456"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoSpringJDBC.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authorityDaoSpringJDBC.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDaoSpringJDBC.create(UserEntity.fromJson(user)),
                null
        );
    }


    //RepositoryJdbc
    public UserJson createUserRepository(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("123456"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);
                    authUser.setAuthorities(Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(authUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toList());

                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserDaoSpringJDBC.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public void addFriend(UserJson requester, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
                    udUserRepository.addFriend(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
                    return null;
                }
        );
    }

    public void addIncomeInvitation(UserJson requester, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
                    udUserRepository.addIncomeInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
                    return null;
                }
        );
    }

    public void addOutcomeInvitation(UserJson requester, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
                    udUserRepository.addOutcomeInvitation(UserEntity.fromJson(addressee), UserEntity.fromJson(requester));
                    return null;
                }
        );
    }


    //RepositoryHibernate
    public UserJson createUserRepositoryHibernate(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepositoryHibernate.create(authUser);
                    return UserJson.fromEntity(
                            udUserRepositoryHibernate.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    public void addFriendHibernate(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                    String username = randomUsername();
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepositoryHibernate.create(authUser);
                    UserEntity addressee = udUserRepositoryHibernate.create(userEntity(username));
                    udUserRepositoryHibernate.addFriend(targetEntity, addressee);
                    return null;
                });
            }
        }
    }

    public void addIncomeInvitationHibernate(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                    String username = randomUsername();
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepositoryHibernate.create(authUser);
                    UserEntity addressee = udUserRepositoryHibernate.create(userEntity(username));
                    udUserRepositoryHibernate.addIncomeInvitation(targetEntity, addressee);
                    return null;
                });
            }
        }
    }

    public void addOutcomeInvitationHibernate(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                    String username = randomUsername();
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepositoryHibernate.create(authUser);
                    UserEntity addressee = udUserRepositoryHibernate.create(userEntity(username));
                    udUserRepositoryHibernate.addOutcomeInvitation(targetEntity, addressee);
                    return null;
                });
            }
        }
    }

    private UserEntity userEntity(String username) {
        UserEntity us = new UserEntity();
        us.setUsername(username);
        us.setCurrency(CurrencyValues.RUB);
        return us;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}
