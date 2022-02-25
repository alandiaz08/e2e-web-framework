package pageobjects.pages;

import environment.EnvironmentConfig;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import pageobjects.base.AbstractPage;
import pageobjects.components.Footer;
import pageobjects.components.HeaderNoSearch;
import pageobjects.components.SearchComponent;
import utils.TestReporter;

/**
 * WebNextGen Home Page. Example: https://www.thefork.com
 */
public class HomePage extends AbstractPage {

  // Selectors
  private static final By headerBy = By.tagName("header");
  private static final By footerBy = By.tagName("footer");
  private static final By tagLineBy = By.cssSelector("div[data-test='homepage-tagline'] > h1");
  private static final By searchContainerBy =
          By.cssSelector("div[data-test='search-component']");

  // Components
  private HeaderNoSearch header;
  private Footer footer;
  private SearchComponent searchComponent;

  private URL homePageUrl;
  private boolean comingFromAnotherPage;

  /**
   * Constructor of the HomePage class.
   */
  public HomePage() {
    super();
    logger.debug("Initializing Home Page");
    try {
      homePageUrl = new URL(EnvironmentConfig.getTheForkUrl());
    } catch (MalformedURLException e) {
      logger.error("The URL format '{}' is not correct.", EnvironmentConfig.getTheForkUrl(), e);
      throwNotLoadedException("The home page could not be opened", e);
    }
    logger.debug("Set home page url to: '{}'", homePageUrl);
  }

  /**
   * Constructor of the HomePage class when passing the home page url as argument.
   */
  public HomePage(String url) {
    super();
    logger.debug("Initializing Home Page");
    try {
      homePageUrl = new URL(url);
    } catch (MalformedURLException e) {
      logger.error("The URL format '{}' is not correct.", url, e);
      throwNotLoadedException("The home page could not be opened", e);
    }
    logger.debug("Set home page url to: '{}'", homePageUrl);
  }

  /**
   * Constructor of the HomePage when coming from another page.
   *
   * @param comingFromAnotherPage Boolean
   */
  public HomePage(boolean comingFromAnotherPage) {
    super();
    logger.debug("Initializing HomePage when coming from another page");
    this.comingFromAnotherPage = comingFromAnotherPage;

  }

  public HeaderNoSearch header() {
    return header;
  }

  public Footer footer() {
    return footer;
  }

  /**
   * Launch a search with the what and where arguments.
   *
   * @param what  The keyword to search
   * @param where The location to search
   * @return The Search Page with the results.
   */
  public SearchPage search(String what, String where) {
    return searchComponent.search(what, where);
  }

  /**
   * Enters a keyword in the search what field.
   *
   * @param what The keyword to enter in the search what field.
   * @return The same Home Page.
   */
  public HomePage enterWhat(String what) {
    TestReporter.addInfoToReport("Enter what: " + what);
    searchComponent.enterWhat(what);
    return this;
  }

  /**
   * Enters a location in the search where field.
   *
   * @param where The location to enter in the search where field.
   * @return The same Home Page.
   */
  public HomePage enterWhere(String where) {
    TestReporter.addInfoToReport("Enter where: " + where);
    searchComponent.enterWhere(where);
    return this;
  }

  /**
   * Clicks on the search button.
   *
   * @return The Search Page with the search results.
   */
  public SearchPage launchSearch() {
    TestReporter.addInfoToReport("Launch search");
    return searchComponent.launchSearch();
  }

  /**
   * Selects the All Restaurants option in the search what field.
   *
   * @return The same Home Page.
   */
  public HomePage selectSearchAllRestaurants() {
    TestReporter.addInfoToReport("Select All Restaurants in the search what field");
    searchComponent.selectAllRestaurants();
    return this;
  }

  /**
   * Selects the Near Me option in the search where field.
   *
   * @return The same Home Page.
   */
  public HomePage selectSearchNearMe() {
    TestReporter.addInfoToReport("Select Near Me in the search where field");
    searchComponent.selectNearMe();
    return this;
  }

  /**
   * Selects a option from the what field autocomplete.
   *
   * @param text The index of the option to be selected.
   * @return The same Home Page.
   */
  public HomePage selectFromAutocomplete(String text) {
    searchComponent.selectFromAutocomplete(text);
    return this;
  }

  /**
   * Logs in with an email and password.
   *
   * @param email    The user email.
   * @param password The user password.
   * @return The same Home Page but with the user logged in.
   */
  public HomePage login(String email, String password) {
    header().openSidebarNotLoggedIn().login(email, password).closeSidebar();
    return this;
  }


  /**
   * Check autocomplete result contains the text in param.
   *
   * @param text The index of the option to be selected.
   * @return if text exists in autocomplete result.
   */
  public boolean isTextPresentInAutoCompleteResult(String text) {
    TestReporter.addInfoToReport("Check if autocomplete result contains text " + text);
    return searchComponent.autocompleteContains(text);
  }

  /**
   * load() is called when HomePage.get() is called. Opens the home page defined by the
   * configuration selected (environment, domain) or passed as -DwngURL argument.
   */
  @Override
  protected void load() {
    if (!comingFromAnotherPage) {
      TestReporter.addInfoToReport("Opening Home Page: " + homePageUrl);
      driver.get(homePageUrl.toString());
      driver.navigate().refresh();
    } else {
      TestReporter.addInfoToReport("Opening Home Page when coming from another page");
    }

  }

  /**
   * isLoaded() is called when HomePage.get() is called. Defines when the page has finished
   * loading. It must verify that the components of this page have also finished loading before
   * continuing.
   */
  @Override
  protected void isLoaded() throws Error {
    try {
      // verify that each element is displayed before continuing
      driver.findElement(tagLineBy);
      logger.debug("Tag line is displayed");
      driver.findElement(headerBy);
      logger.debug("Header container is displayed");
      driver.findElement(footerBy);
      logger.debug("Footer container is displayed");
      driver.findElement(searchContainerBy);
      logger.debug("Search container is displayed");
    } catch (Exception e) {
      throwNotLoadedException("The Home page was not loaded correctly", e);
    }

    // initialize and verify that each internal component is loaded
    header = new HeaderNoSearch(driver.findElement(headerBy));
    header.get();
    footer = new Footer(driver.findElement(footerBy));
    footer.get();
    searchComponent = new SearchComponent(driver.findElement(searchContainerBy));
    searchComponent.get();
  }
}
