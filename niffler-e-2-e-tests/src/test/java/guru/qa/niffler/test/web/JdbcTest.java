package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "testName-5",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        100.0,
                        "test desc",
                        "duck"
                )
        );
        System.out.println(spend);
    }

    @Test
    void addFriendRepositoryTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserRepository(
                new UserJson(
                        null,
                        "alex-1",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        UserJson user2 = usersDbClient.createUserRepository(
                new UserJson(
                        null,
                        "alex-2",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        usersDbClient.addFriend(user, user2);
    }

    @Test
    void addInvitationRepositoryTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserRepository(
                new UserJson(
                        null,
                        "alex-3",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        UserJson user2 = usersDbClient.createUserRepository(
                new UserJson(
                        null,
                        "alex-4",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );

        usersDbClient.addInvitation(user, user2);
    }

    @Test
    void springJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();

        UserJson user = usersDbClient.createUserRepository(
                new UserJson(
                        null,
                        "valentin-2",
                        CurrencyValues.RUB,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }
}
