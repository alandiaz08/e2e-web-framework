package pageobjects.components;

import driver.DriverBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.base.AbstractComponent;
import pageobjects.pages.HomePage;
import utils.TestReporter;

public class SidebarLoggedIn extends AbstractComponent {

  // Selectors
  private static final By closeSidebarButtonBy =
          By.cssSelector("button[aria-controls='USER_SPACE_FIRST_PANEL']");
  private static final By myPersonalInformationButtonBy =
          By.cssSelector("button[aria-controls='user-space-user-information']");
  private static final By myReservationsButtonBy =
          By.cssSelector("button[aria-controls='user-space-user-bookings']");
  private static final By myFavoritesButtonBy =
          By.cssSelector("button[aria-controls='user-space-user-favorites']");
  private static final By myReviewsButtonBy =
          By.cssSelector("button[aria-controls='user-space-user-reviews']");
  private static final By myLoyaltySpaceButtonBy =
          By.cssSelector("button[aria-controls='user-space-fidelity-space']");
  private static final By logoutButtonBy =
          By.cssSelector("button[data-test='LOGOUT_BTN']");
  private static final By usernameBy = By.tagName("h1");
  private static final By totalYumsBy =
          By.cssSelector("li[data-test='USER_PROFILE_TOTAL_YUMS'] > span");

  private static final int TIMEOUT_TO_CLOSE_SIDEBAR = 5;

  public SidebarLoggedIn(WebElement container) {
    super(container);
  }

  /**
   * Gets the username.
   * @return The username.
   */
  public String getUsername() {
    logger.debug("Get username");
    return container.findElement(usernameBy).getText();
  }

  /**
   * Gets the yums of the user.
   * @return The number of yums of the user.
   */
  public String getYums() {
    logger.debug("Get yums");
    return container.findElement(totalYumsBy).getText();
  }

  /**
   * User log out.
   */
  public HomePage logOut() {
    TestReporter.addInfoToReport("Log out");
    logger.debug("Click log out");
    container.findElement(logoutButtonBy).click();
    logger.debug("Wait until sidebar is not displayed");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_CLOSE_SIDEBAR);
    wait.until(ExpectedConditions.invisibilityOf(container));
    logger.debug("Delete all cookies");
    DriverBase.getDriver().manage().deleteAllCookies();
    logger.debug("Sidebar is not displayed");
    HomePage homePage = new HomePage(true);
    homePage.get();
    return homePage;
  }

  /**
   * Close the sidebar.
   */
  public void closeSidebar() {
    TestReporter.addInfoToReport("Close sidebar");
    container.findElement(closeSidebarButtonBy).click();
    logger.debug("Wait until sidebar is not displayed");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_CLOSE_SIDEBAR);
    wait.until(ExpectedConditions.invisibilityOf(container));
    logger.debug("Sidebar is not displayed");
  }

  @Override
  protected void isLoaded() throws Error {
    try {
      // verify that each element is displayed before continuing
      container.findElement(closeSidebarButtonBy);
      logger.debug("Close sidebar button is displayed");
      container.findElement(myPersonalInformationButtonBy);
      logger.debug("My personal information button is displayed");
      container.findElement(myReservationsButtonBy);
      logger.debug("My reservations button is displayed");
      container.findElement(myFavoritesButtonBy);
      logger.debug("My Favorites button is displayed");
      container.findElement(myReviewsButtonBy);
      logger.debug("My reviews button is displayed");
      container.findElement(myLoyaltySpaceButtonBy);
      logger.debug("My loyalty space button is displayed");
      container.findElement(logoutButtonBy);
      logger.debug("Logout button is displayed");
      container.findElement(usernameBy);
      logger.debug("Username is displayed");
      container.findElement(totalYumsBy);
      logger.debug("Total yums is displayed");
    } catch (Exception e) {
      throwNotLoadedException("The sidebar logged in component was not loaded correctly", e);
    }

    logger.debug("SidebarLogged component was loaded correctly");
  }
}
