package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@WebTest
public class FriendsWebTest extends BaseTest {

    @Test
    @User(friends = 1)
    @DisplayName("Friend should be present in friends table")
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password());

        mainPage.openFriendsPage();
        friendsPage.checkThatFriendPresentInFriendsTable(user.testData().friends().getFirst());
    }

    @Test
    @User
    @DisplayName("Friend table should be empty for new user")
    void friendTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password());

        mainPage.openFriendsPage();
        friendsPage.checkThatFriendsTableIsEmpty();
    }

    @Test
    @User(incomeInvitations = 1)
    @DisplayName("Income invitation be present in friends table")
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password());

        mainPage.openFriendsPage();
        friendsPage.checkThatIncomeInvitationInFriendsTable(user.testData().incomeInvitations().get(0));
    }

    @Test
    @User(outcomeInvitations = 1)
    @DisplayName("Outcome invitation be present in all peoples table")
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password());

        mainPage.openFriendsPage();
        friendsPage.openAllPeopleSection();
        friendsPage.checkThatOutcomeInvitationInFriendsTable(user.testData().outcomeInvitations().get(0));
    }
}
