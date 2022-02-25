package pageobjects.pages;

import driver.DriverBase;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import pageobjects.base.AbstractPage;
import pageobjects.components.Footer;
import pageobjects.components.SearchResultList;
import utils.TestReporter;

/**
 * WebNextGen Search page. Example: https://www.thefork.com/search/?cityId=415144
 */
public class SearchPage extends AbstractPage {

  // Selectors
  private static final By numberOfResultsBy = By.cssSelector(".container > div > div > p");
  private static final By headerContainerBy = By.tagName("header");
  private static final By footerContainerBy = By.tagName("footer");
  private static final By mapContainerBy = By.id("map");
  private static final By resultListContainerBy =
          By.cssSelector("div[data-test='result-list-restaurants']");
  private static final By listOfRestaurantsEmptyMessageBy = By.cssSelector(".withMap h1");
  private static final By dhpBy = By.cssSelector("[data-test='dhp-selector']");
  private static final By paginationContainerBy =
          By.cssSelector("nav[data-test='pagination-page-list']");
  private static final By closeDhpRegionBy =
          By.cssSelector("[data-tracking-region='DHP and filters'] > div > div > div");
  private static final By sortByButtonBy = By.cssSelector("#root fieldset legend");
  private static final By sortByOptionsBy = By.cssSelector("#root fieldset legend+div > div");
  private static final By bestRestaurantsInCityLabelBy = By.cssSelector("div > h1");
  private static final By specialOffersButtonBy =
          By.cssSelector("[data-test='quick-filter-special-offer']");
  private static final By marketingBanner =
          By.cssSelector("header[data-test='search-marketing-banner-header']");
  private static final By numberOfRestaurants = By.cssSelector("[data-test='result-count']");

  // Components
  private Footer footer;
  private SearchResultList searchResultList;


  private String searchPageUrl;
  private static final String SEARCH_PAGE_NOT_LOADED = "The Search page was not loaded correctly";
  private static final Pattern myRegex = Pattern.compile("\u202f");

  /**
   * Constructor of the SearchPage class when coming from another page.
   */
  public SearchPage() {
    super();
    logger.debug("Initializing Search Page");
  }

  /**
   * Constructor of the SearchPage class when opening the page directly from the URL.
   */
  public SearchPage(String searchArguments) {
    super();
    logger.debug("Initializing Search Page with argument '{}", searchArguments);
    driver = DriverBase.getDriver();
    logger.debug("Full Search Page url '{}", searchPageUrl);
  }

  public Footer footer() {
    return footer;
  }




  /**
   * Verifies if the search googleMap is displayed.
   * @return True if the googleMap is displayed. False otherwise.
   */
  public boolean isGoogleMapDisplayed() {
    try {
      return driver.findElement(mapContainerBy).isDisplayed();
    } catch (NotFoundException e) {
      logger.debug("The googleMap was not found", e);
      return false;
    }
  }


  /**
   * Verifies if the list of restaurants is empty message is displayed.
   * @return True if the list of restaurants is empty message is displayed. False otherwise.
   */
  public boolean isListOfRestaurantsEmpty() {
    try {
      return driver.findElement(listOfRestaurantsEmptyMessageBy).isDisplayed();
    } catch (NotFoundException e) {
      logger.debug("The list of restaurants was not found", e);
      return false;
    }
  }

  /**
   * Verifies if the label "The Best Restaurants in CITY" is displayed.
   * @return True if label "The Best Restaurants in CITY" is displayed. False otherwise.
   */
  public boolean isBestRestaurantsInCityLabelDisplayed() {
    try {
      return driver.findElement(bestRestaurantsInCityLabelBy).isDisplayed();
    } catch (NotFoundException e) {
      logger.debug("The best restaurants in city label was not found", e);
      return false;
    }
  }

  public SearchPage closeDhp() {
    driver.findElement(closeDhpRegionBy).click();
    return this;
  }

  /**
   * Enable the special offers only filter.
   * @return The Search Page after the special offers filter was enabled.
   */
  public SearchPage enableSpecialOffers() {
    TestReporter.addInfoToReport("Enable special offers only");
    WebElement specialOffersButton = driver.findElement(specialOffersButtonBy);

    // if the button contains only one span, it's the white button with special offers disabled
    if (specialOffersButton.findElements(By.tagName("span")).size() == 1) {
      logger.debug("Click the white special offers button");
      specialOffersButton.click();
      SearchPage searchPage = new SearchPage();
      searchPage.get();
      return searchPage;
    } else {
      logger.debug("The special offers button is already enabled");
      return this;
    }
  }

  /**
   * Disable the special offers only filter.
   * @return The Search Page after the special offers filter was disabled.
   */
  public SearchPage disableSpecialOffers() {
    TestReporter.addInfoToReport("Disable special offers only");
    WebElement specialOffersButton = driver.findElement(specialOffersButtonBy);

    // if the button does not contain only one span, it's the black button with special offers
    // enabled
    if (specialOffersButton.findElements(By.tagName("span")).size() != 1) {
      logger.debug("Click the black special offers button");
      specialOffersButton.click();
      SearchPage searchPage = new SearchPage();
      searchPage.get();
      return searchPage;
    } else {
      logger.debug("The special offers button is already disabled");
      return this;
    }
  }



  /**
   * Checks if the marketing banner is displayed.
   * @return true if the marketing banner is displayed. False otherwise
   */
  public boolean isMarketingBannerDisplayed() {
    logger.debug("Is the marketing banner displayed");
    return driver.findElement(marketingBanner).isDisplayed();
  }

  /**
   * Get number of restaurants.
   * @return a integer with the number of restaurants
   */
  public int getNumberOfRestaurants() {
    logger.info("Get number of restaurants");
    String numberOfRestaurant = driver.findElement(numberOfRestaurants).getText();
    String [] numberOfRest = numberOfRestaurant.split(" ");
    String number = numberOfRest[0];
    logger.debug("Getting {} the number of restaurants in string format", number);
    number = myRegex.matcher(number).replaceAll("");
    logger.debug("Preparing {} to convert to integer",number);
    return Integer.parseInt(number);
  }

  /**
   * isLoaded() is called when SearchPage.get() is called. Defines when the page has finished
   * loading. It must verify that the components of this page have also finished loading before
   * continuing.
   */
  @Override
  protected void isLoaded() throws Error {
    // First load the DHP menu and close it if it's open
    try {
      driver.findElement(dhpBy);
      logger.debug("Dhp container is visible");
    } catch (Exception e) {
      throwNotLoadedException(SEARCH_PAGE_NOT_LOADED, e);
    }

    // Continue loading the rest of the page
    try {
      // verify that each element is displayed before continuing
      driver.findElement(numberOfResultsBy);
      logger.debug("Number of results is visible");
      driver.findElement(headerContainerBy);
      logger.debug("Header container is visible");
      driver.findElement(footerContainerBy);
      logger.debug("Footer container is visible");
      driver.findElement(mapContainerBy);
      logger.debug("GoogleMap container is visible");
      driver.findElement(sortByButtonBy);
      logger.debug("Sort by button is displayed");
    } catch (Exception e) {
      throwNotLoadedException(SEARCH_PAGE_NOT_LOADED, e);
    }

    try {
      // special case for the list of results, if there are no search results the list does not
      // appear
      driver.findElement(resultListContainerBy);
      logger.debug("Search list result is displayed");

      // only initializes the search result list if it is visible
      searchResultList = new SearchResultList(driver.findElement(resultListContainerBy));
      searchResultList.get();

    } catch (Exception noSearchList) {
      // if the search restaurant list was not found, try to find the empty restaurant list message
      try {
        logger.debug("Search list result is not displayed, checking for empty list message",
                noSearchList);
        driver.findElement(listOfRestaurantsEmptyMessageBy).isDisplayed();
        logger.debug("Empty restaurant list message is displayed");
        searchResultList = new SearchResultList();
        logger.debug("Initializing the SearchResultList with zero element");
      } catch (Exception e) {
        throwNotLoadedException(SEARCH_PAGE_NOT_LOADED, e);
      }
    }
  }
}
