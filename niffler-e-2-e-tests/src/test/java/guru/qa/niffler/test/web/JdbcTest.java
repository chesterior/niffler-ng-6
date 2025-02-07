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

//    @Test
//    void xaTxTest() {
//        UsersDbClient usersDbClient = new UsersDbClient();
//
//        UserJson user = usersDbClient.createUser(
//                new UserJson(
//                        null,
//                        "valentin",
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null
//                )
//        );
//        System.out.println(user);
//    }

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
