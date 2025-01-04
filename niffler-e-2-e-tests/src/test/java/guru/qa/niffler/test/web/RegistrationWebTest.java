package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static utils.RandomDataUtils.*;

@WebTest
public class RegistrationWebTest extends BaseTest {

    @Test
    void shouldRegisterNewUser() {
        String userName = randomUsername;
        String password = randomPassword;
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationPage();

        registerPage.successSignUp(userName, password);
        registerPage.checkSuccessSignUpAndClickSignIn();
        loginPage.login(userName, password);
        mainPage.checkingDisplayOfMainComponents();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String userName = randomUsername;
        String password = randomPassword;

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationPage();

        registerPage.successSignUp(userName, password);
        registerPage.checkSuccessSignUpAndClickSignIn();
        loginPage.openRegistrationPage();
        registerPage.successSignUp(userName, password);
        registerPage.usernameAlreadyExists(userName);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .openRegistrationPage();

        registerPage.signUpWithNotEqualPasswords(randomUsername, randomPassword, "456377");
        registerPage.checkPasswordsNotEqual();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(randomUsername, randomPassword);

        loginPage.checkBadCredentials();
        loginPage.checkHeaderLogin();
    }
}
