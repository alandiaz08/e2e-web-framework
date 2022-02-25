package pageobjects.components;

import driver.DriverBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.base.AbstractComponent;
import utils.TestReporter;

public class HeaderNoSearch extends AbstractComponent {

  // Selectors
  protected static final By logoBy = By.cssSelector("[data-test='brand-logo']");
  private static final By loginButtonBy = By.cssSelector("button[data-test='user-space']");
  private static final By sidebarContainerBy = By.id("USER_SPACE_FIRST_PANEL");

  private static final int TIMEOUT_TO_OPEN_SIDEBAR = 10;

  public HeaderNoSearch(WebElement container) {
    super(container);
  }

  /**
   * Open the sidebar when the user is not logged in.
   * @return Instance of SidebarUserNotLoggedIn.
   */
  public SidebarNotLoggedIn openSidebarNotLoggedIn() {
    TestReporter.addInfoToReport("Open sidebar when not logged in");
    container.findElement(loginButtonBy).click();
    logger.debug("Wait until sidebar container is displayed");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_OPEN_SIDEBAR);
    WebElement sidebarContainer =
            wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarContainerBy));
    logger.debug("Sidebar container is displayed");
    SidebarNotLoggedIn sidebarNotLoggedIn = new SidebarNotLoggedIn(sidebarContainer);
    sidebarNotLoggedIn.get();
    return sidebarNotLoggedIn;
  }

  /**
   * Open the sidebar when the user is logged in.
   * @return Instance of SidebarUserLoggedIn
   */
  public SidebarLoggedIn openSidebarLoggedIn() {
    TestReporter.addInfoToReport("Open sidebar when logged in");
    container.findElement(loginButtonBy).click();
    logger.debug("Wait until sidebar container is displayed");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_TO_OPEN_SIDEBAR);
    WebElement sidebarContainer =
            wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarContainerBy));
    logger.debug("Sidebar container is displayed");
    SidebarLoggedIn sidebarLoggedIn = new SidebarLoggedIn(sidebarContainer);
    sidebarLoggedIn.get();
    return sidebarLoggedIn;
  }


  @Override
  protected void isLoaded() throws Error {
    try {
      // verify that each element is displayed before continuing
      container.findElement(logoBy);
      logger.debug("Logo is displayed");
      container.findElement(loginButtonBy);
      logger.debug("Login button is displayed");
    } catch (Exception e) {
      throwNotLoadedException("The header no search component was not loaded correctly", e);
    }

    logger.debug("HeaderNoSearch component was loaded correctly");
  }
}
