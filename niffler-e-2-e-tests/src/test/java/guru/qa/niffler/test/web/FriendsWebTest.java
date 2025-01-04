package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtensions.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtensions.UserType.Type.*;

@WebTest
public class FriendsWebTest extends BaseTest {

    @Test
    @DisplayName("Friend should be present in friends table")
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIENDS) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password());

        mainPage.openFriendsPage();
        friendsPage.checkThatFriendPresentInFriendsTable(user.friend());
    }

    @Test
    @DisplayName("Friend table should be empty for new user")
    void friendTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password());

        mainPage.openFriendsPage();
        friendsPage.checkThatFriendsTableIsEmpty();
    }

    @Test
    @DisplayName("Income invitation be present in friends table")
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password());

        mainPage.openFriendsPage();
        friendsPage.checkThatIncomeInvitationInFriendsTable(user.income());
    }

    @Test
    @DisplayName("Outcome invitation be present in all peoples table")
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.password());

        mainPage.openFriendsPage();
        friendsPage.openAllPeopleSection();
        friendsPage.checkThatOutcomeInvitationInFriendsTable(user.outcome());
    }
}
