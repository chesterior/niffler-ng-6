package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement createNewAccountButton = $("a.form__register");
    private final SelenideElement badCredentials = $(".form__error");
    private final SelenideElement headerLogin = $x(".//h1[text()='Log in']");
    private final SelenideElement errorContainer = $(".form__error");

    public MainPage successLogin(String username, String password) {
        login(username, password);
        return new MainPage();
    }

    public void openRegistrationPage() {
        createNewAccountButton.click();
    }

    public LoginPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return this;
    }

    @Step("Check message bad credentials")
    public void checkBadCredentials() {
        badCredentials.shouldBe(visible);
    }

    @Step("Check header Login on Login Page")
    public void checkHeaderLogin() {
        headerLogin.shouldBe(visible);
    }

    @Step("Check that page is loaded")
    public LoginPage checkThatPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        return this;
    }

    @Step("Check error on page: {error}")
    public LoginPage checkError(String error) {
        errorContainer.shouldHave(text(error));
        return this;
    }
}
