package pageobjects.components;

import com.neovisionaries.i18n.CountryCode;
import driver.DriverBase;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.base.AbstractComponent;
import utils.TestReporter;

public class SidebarNotLoggedIn extends AbstractComponent {

  // Selectors
  private static final By emailInputBy = By.id("identification_email");
  private static final By passwordInputBy = By.name("password");
  private static final By continueToPasswordScreenButtonBy =
          By.cssSelector("button[data-testid='checkout-submit-email']");
  private static final By firstNameInputBy = By.name("firstName");
  private static final By lastNameInputBy = By.name("lastName");
  private static final By countryCodeDropdownBy = By.id("PHONE_CODE_FIELD");
  private static final By phoneInputBy = By.name("phoneNumber.nationalNumber");
  private static final By loginButtonBy = By.cssSelector("button[data-testid='submit-password']");
  private static final By goBackButtonBy = By.cssSelector("a[data-test='PAGE_TITLE_BACK");
  private static final By closeSidebarButtonBy =
          By.cssSelector("button[aria-controls='USER_SPACE_FIRST_PANEL']");
  private static final By sidebarLoggedInContainerBy = By.id("USER_SPACE_FIRST_PANEL");
  private static final By registerButtonBy = By.cssSelector("section button[type='submit']");

  private static final By requestCreatePasswordBy =
          By.cssSelector("[data-test='user-space-request-create-password-step']");
  private static final By createPasswordPageBy =
          By.cssSelector("[data-testid='create-password-page']");
  private static final By accountCreationSectionBy =
          By.cssSelector("section div.pageContent");
  private static final By invalidPwdLabelBy = By.cssSelector(".inputLabel + span");
  private static final By forgotPasswordBy =
          By.cssSelector("button[data-testid='reset-password-link']");
  private static final By resetPasswordMsgBy =
          By.cssSelector("div[data-testid='reset-password-page']");

  private static final int TIMEOUT_TO_BUTTON_ENABLED = 20;
  private static final int TIMEOUT_TO_CHANGE_SCREEN = 20;

  public SidebarNotLoggedIn(WebElement container) {
    super(container);
  }

  /**
   * Enter the user email in the email input.
   * @param email the user email
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn enterEmail(String email) {
    TestReporter.addInfoToReport("Enter email: " + email);
    container.findElement(emailInputBy).sendKeys(email);
    return this;
  }

  /**
   * Enter the user password in the password input.
   * @param password the user password
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn enterPassword(String password) {
    TestReporter.addInfoToReport("Enter password: " + password);
    container.findElement(passwordInputBy).sendKeys(password);
    return this;
  }

  /**
   * Continue to password screen.
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn continueToPasswordScreen() {
    TestReporter.addInfoToReport("Continue to password screen");
    logger.debug("Wait until continue to password screen button is clickable");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_BUTTON_ENABLED);
    wait.until(ExpectedConditions.elementToBeClickable(continueToPasswordScreenButtonBy));
    logger.debug("Click continue to password screen button");
    container.findElement(continueToPasswordScreenButtonBy).click();
    logger.debug("Wait until password input is visible");
    WebDriverWait waitNextScreen =
            new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_CHANGE_SCREEN);
    waitNextScreen.until(ExpectedConditions.visibilityOfElementLocated(passwordInputBy));
    if (isAccountCreationSectionDisplayed()) {
      throwNotLoadedException("Password screen was not displayed");
    }
    return this;
  }

  /**
   * Clicks on the log in button and logs in successfully.
   * @return The sidebar when the user is logged in.
   */
  public SidebarLoggedIn clickLoginButtonSuccessful() {
    TestReporter.addInfoToReport("Click on log in button and log in successfully");
    logger.debug("Wait until login button is clickable");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_BUTTON_ENABLED);
    wait.until(ExpectedConditions.elementToBeClickable(loginButtonBy));
    logger.debug("Click login button");
    container.findElement(loginButtonBy).click();
    logger.debug("Wait until not logged in sidebar is closed");
    wait.until(ExpectedConditions.invisibilityOf(container));
    logger.debug("Not logged in sidebar is closed");
    logger.debug("Wait until logged in sidebar is displayed");
    WebElement newSidebar =
            wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarLoggedInContainerBy));
    logger.debug("Logged in sidebar is displayed");
    SidebarLoggedIn sidebarLoggedIn = new SidebarLoggedIn(newSidebar);
    sidebarLoggedIn.get();
    return sidebarLoggedIn;
  }

  /**f
   * Clicks on the log in button and the log in fails (wrong password for example).
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn clickLoginButtonUnsuccessful() {
    TestReporter.addInfoToReport("Click on log in button and doesn't log in");
    logger.debug("Wait until login button is clickable");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_BUTTON_ENABLED);
    wait.until(ExpectedConditions.elementToBeClickable(loginButtonBy));
    logger.debug("Click login button");
    container.findElement(loginButtonBy).click();
    return this;
  }

  /**
   * Performs the full login process.
   * @param email the user email
   * @param password the user password
   */
  public SidebarLoggedIn login(String email, String password) {
    logger.debug("Log in with email {} and password {}", email, password);
    enterEmail(email);
    continueToPasswordScreen();
    enterPassword(password);
    return clickLoginButtonSuccessful();
  }

  /**
   * Continue to request password screen.
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn continueToCreatePasswordRequestScreen() {
    TestReporter.addInfoToReport("Continue to request password screen");
    logger.debug("Wait until continue to password screen button is clickable");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_BUTTON_ENABLED);
    wait.until(ExpectedConditions.elementToBeClickable(continueToPasswordScreenButtonBy));
    logger.debug("Click continue to password screen button");
    container.findElement(continueToPasswordScreenButtonBy).click();
    logger.debug("Wait until create password  request message is visible");
    WebDriverWait waitNextScreen =
            new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_CHANGE_SCREEN);
    waitNextScreen.until(ExpectedConditions.visibilityOfElementLocated(requestCreatePasswordBy));
    return this;
  }

  /**
   * Checks if the Create Password message is displayed.
   * Only shown if the user was created through booking flow.
   * @return True if the Create Passoword message is visible. False otherwise.
   */
  public boolean isCreatePasswordMessageDisplayed() {
    logger.debug("Validating if the Create Password message is displayed");
    try {
      return container.findElement(createPasswordPageBy).isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("The Create Password message was not found", e);
      return false;
    }
  }

  /**
   * Checks if the Create Account section is displayed.
   * Only shown if the user doesn't have an account.
   * @return True if the Create Account section is displayed. False otherwise.
   */
  public boolean isAccountCreationSectionDisplayed() {
    logger.debug("Validating if the Account Creation section is displayed");
    try {
      return container.findElement(accountCreationSectionBy).isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("The Create Account section was not found", e);
      return false;
    }
  }

  /**
   * Checks if the invalid password message is displayed.
   * Only shown if password is wrong.
   * @return True if the the invalid password message is displayed. False otherwise.
   */
  public boolean isInvalidPasswordDisplayed() {
    logger.debug("Check if the invalid password message is displayed");
    try {
      WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_BUTTON_ENABLED);
      return wait.until(ExpectedConditions.visibilityOfElementLocated(invalidPwdLabelBy))
              .isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("The invalid password message was not found", e);
      return false;
    }
  }

  /**
   * Checks if the reset password message is displayed.
   * Only shown if forgot password button is clicked.
   * @return True if the the reset password message is displayed. False otherwise.
   */
  public boolean isResetPasswordMsgDisplayed() {
    logger.debug("Check if the reset password message is displayed");
    try {
      WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_BUTTON_ENABLED);
      return wait.until(ExpectedConditions.visibilityOfElementLocated(resetPasswordMsgBy))
              .isDisplayed();
    } catch (TimeoutException e) {
      logger.debug("The reset password message was not found", e);
      return false;
    }
  }

  /**
   * Enters the user first name.
   * @param firstName The first name of the user.
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn enterFirstName(String firstName) {
    TestReporter.addInfoToReport("Enter first name: " + firstName);
    WebElement firstNameInput =  container.findElement(firstNameInputBy);
    logger.debug("Clear first name input");
    firstNameInput.clear();
    logger.debug("Type first name: {}", firstName);
    firstNameInput.sendKeys(firstName);
    return this;
  }

  /**
   * Enters the user last name.
   * @param lastName The last name of the user.
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn enterLastName(String lastName) {
    TestReporter.addInfoToReport("Enter last name: " + lastName);
    WebElement firstNameInput =  container.findElement(lastNameInputBy);
    logger.debug("Clear last name input");
    firstNameInput.clear();
    logger.debug("Type last name: {}", lastName);
    firstNameInput.sendKeys(lastName);
    return this;
  }

  /**
   * Select the country code using the Alpha-2 Country code.
   * For more info: https://www.iban.com/country-codes
   * @param countryCode The country code.
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn selectCountryCode(@NotNull CountryCode countryCode) {
    TestReporter.addInfoToReport("Select country code: " + countryCode);
    Select countryCodeDropdown = new Select(container.findElement(countryCodeDropdownBy));
    logger.debug("Select alpha-2: {}", countryCode.getAlpha2());
    countryCodeDropdown.selectByValue(countryCode.getAlpha2());
    return this;
  }

  /**
   * Enters the user phone number.
   * @param phoneNumber The phone number of the user.
   * @return The sidebar when the user is not logged in.
   */
  public SidebarNotLoggedIn enterPhoneNumber(String phoneNumber) {
    TestReporter.addInfoToReport("Enter phone number: " + phoneNumber);
    WebElement phoneInput =  container.findElement(phoneInputBy);
    logger.debug("Clear phone number input");
    phoneInput.clear();
    logger.debug("Type phone number: {}", phoneNumber);
    phoneInput.sendKeys(phoneNumber);
    return this;
  }

  /**
   * Click to register the account.
   * @return The sidebar when the user is logged in.
   */
  public SidebarLoggedIn registerAccount() {
    TestReporter.addInfoToReport("Click on button register the account");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_BUTTON_ENABLED);
    wait.until(ExpectedConditions.elementToBeClickable(registerButtonBy));
    logger.debug("Click register button");
    container.findElement(registerButtonBy).click();
    logger.debug("Wait until not logged in sidebar is closed");
    wait.until(ExpectedConditions.invisibilityOf(container));
    logger.debug("Not logged in sidebar is closed");
    logger.debug("Wait until logged in sidebar is displayed");
    WebElement newSidebar =
            wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarLoggedInContainerBy));
    logger.debug("Logged in sidebar is displayed");
    SidebarLoggedIn sidebarLoggedIn = new SidebarLoggedIn(newSidebar);
    sidebarLoggedIn.get();
    return sidebarLoggedIn;
  }

  @Override
  protected void isLoaded() throws Error {
    try {
      // verify that each element is displayed before continuing
      container.findElement(emailInputBy);
      logger.debug("Email input button is displayed");
      container.findElement(continueToPasswordScreenButtonBy);
      logger.debug("Continue to password screen button is displayed");
      container.findElement(closeSidebarButtonBy);
      logger.debug("Close sidebar button is displayed");
    } catch (Exception e) {
      throwNotLoadedException("The sidebar not logged in component was not loaded correctly", e);
    }

    logger.debug("SidebarNotLoggedIn component was loaded correctly");
  }
}
