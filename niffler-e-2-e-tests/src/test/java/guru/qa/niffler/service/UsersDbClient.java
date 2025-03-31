package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UdUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UdUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.RandomDataUtils.randomUsername;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UdUserRepository udUserRepository = new UdUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserRepository.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    @Override
    public List<String> addFriend(UserJson targetUser, int count) {
        List<String> userNames = new ArrayList<>();
        if (count > 0) {
            String username = randomUsername();
            UserEntity targetEntity = udUserRepository.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepository.create(authUser);
                    UserEntity friendEntity = udUserRepository.create(userEntity(username));
                    udUserRepository.addFriend(targetEntity, friendEntity);
                    return null;
                });
            }
            userNames.add(username);
        }
        return userNames;
    }

    @Override
    public List<String> addIncomeInvitation(UserJson targetUser, int count) {
        List<String> userNames = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepository.create(authUser);
                    UserEntity addressee = udUserRepository.create(userEntity(username));
                    udUserRepository.addIncomeInvitation(targetEntity, addressee);
                    return null;
                });
                userNames.add(username);
            }
        }
        return userNames;
    }

    @Override
    public List<String> addOutcomeInvitation(UserJson targetUser, int count) {
        List<String> userNames = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepository.create(authUser);
                    UserEntity addressee = udUserRepository.create(userEntity(username));
                    udUserRepository.addOutcomeInvitation(targetEntity, addressee);
                    return null;
                });
                userNames.add(username);
            }
        }
        return userNames;
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
