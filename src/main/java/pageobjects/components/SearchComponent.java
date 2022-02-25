package pageobjects.components;

import driver.DriverBase;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.util.Strings;
import pageobjects.base.AbstractComponent;
import pageobjects.pages.SearchPage;
import utils.TestReporter;

public class SearchComponent extends AbstractComponent {
  private static final By whatInputBy = By.id("whatinput");
  private static final By whereInputBy = By.id("whereinput");
  private static final By searchButtonBy = By.cssSelector("button[type='submit']");
  private static final By clearWhereButtonBy =
          By.cssSelector("label[for='whereinput']+span button");
  private static final By labelWhereBy = By.cssSelector("label[for='whereinput']");
  private static final By labelWhatBy = By.cssSelector("label[for='whatinput']");
  private static final By clearWhatButtonBy = By.cssSelector("label[for='whatinput']+span button");
  private static final By autocompleteBy =
          By.cssSelector("ul[data-test='search-autocomplete-results']");
  private static final By autocompleteFirstLinkBy = By.cssSelector("ul > a, ul > button");

  private static final String VALUE_ATTRIBUTE = "value";
  private static final String AUTOCOMPLETE_VISIBLE = "Autocomplete is visible";

  private static final int TIMEOUT_FOR_AUTOCOMPLETE = 10;
  private static final int TIMEOUT_CLEAR_BUTTON = 2;
  private static final int TIMEOUT_SEARCH_BUTTON_CLICKABLE = 10;

  public SearchComponent(WebElement container) {
    super(container);
  }

  /**
   * Enter the keywords to be searched.
   * @param what the user email
   */
  public void enterWhat(String what) {
    logger.debug("Enter what: {}", what);
    WebElement whatInput = container.findElement(whatInputBy);

    String whatInputContent = whatInput.getAttribute(VALUE_ATTRIBUTE);
    if (!Strings.isNullOrEmpty(whatInputContent)) {
      logger.debug("The what field contains the text: {}", whatInputContent);
      clearWhatField();
    }

    whatInput.sendKeys(what);
    logger.debug("Entered '{}' in what field", what);
    container.findElement(labelWhatBy).click();
  }

  /**
   * Enter the location to be searched.
   * @param where the user email
   */
  public void enterWhere(String where) {
    logger.debug("Enter where: {}", where);
    WebElement whereInput = container.findElement(whereInputBy);

    String whereInputContent = whereInput.getAttribute(VALUE_ATTRIBUTE);
    if (!Strings.isNullOrEmpty(whereInputContent)) {
      logger.debug("The where field contains the text: {}", whereInputContent);
      clearWhereField();
    }

    // click outside to clear the focus
    container.findElement(labelWhereBy).click();
    whereInput.sendKeys(where);
    logger.debug("Entered '{}' in where field", where);

    logger.debug("Wait for autocomplete to appear");
    WebDriverWait waitAutocomplete =
            new WebDriverWait(DriverBase.getDriver(), TIMEOUT_FOR_AUTOCOMPLETE);
    waitAutocomplete.until(ExpectedConditions.visibilityOfElementLocated(autocompleteBy));
    logger.debug(AUTOCOMPLETE_VISIBLE);
  }

  /**
   * Clicks on the search button.
   * @return The search page with the search result.
   */
  public SearchPage launchSearch() {
    logger.debug("Click search button");
    waitUntilSearchButtonIsClickable();
    container.findElement(searchButtonBy).click();
    logger.debug("Clicked on search button");
    SearchPage searchPage = new SearchPage();
    searchPage.get();
    return searchPage;
  }

  /**
   * Selects the autocomplete option defined by the text in the argument.
   * @param text The text of the autocomplete option to be selected.
   */
  public void selectFromAutocomplete(String text) {
    logger.debug("Select '{}' from autocomplete", text);
    clickAutocompleteOption(text);
    waitUntilSearchButtonIsClickable();
  }

  private static void clickAutocompleteOption(String text) {
    WebDriverWait waitAutocomplete =
            new WebDriverWait(DriverBase.getDriver(), TIMEOUT_FOR_AUTOCOMPLETE);
    waitAutocomplete.until(ExpectedConditions.visibilityOfElementLocated(autocompleteBy));
    logger.debug(AUTOCOMPLETE_VISIBLE);

    By selectorLink = By.cssSelector(
            "a[aria-label='" + text + "'], "
                    +
                    "button[aria-label='" + text + "']"
    );

    WebElement autocompleteResult = waitAutocomplete.until(
            ExpectedConditions.presenceOfElementLocated(selectorLink));

    String autocompleteLinkText = autocompleteResult.getText();
    TestReporter.addInfoToReport("Click on autocomplete option with text '"
            + autocompleteLinkText + "'");

    autocompleteResult.click();
    logger.debug("Clicked on the autocomplete link with text: {}", autocompleteLinkText);
  }

  /**
   * Check if the autocomplete result contains the text.
   * @param text The index of the option to be selected.
   * @return if the text exists in the autocomplete result.
   */
  public boolean autocompleteContains(String text) {
    WebDriverWait waitAutocomplete =
            new WebDriverWait(DriverBase.getDriver(), TIMEOUT_FOR_AUTOCOMPLETE);
    waitAutocomplete.until(ExpectedConditions.visibilityOfElementLocated(autocompleteBy));
    logger.debug(AUTOCOMPLETE_VISIBLE);

    By selectorLink = By.cssSelector(
            "a[aria-label='" + text + "'], "
                    +
                    "button[aria-label='" + text + "']"
    );
    logger.debug("Autocomplete result selector: {}", selectorLink);

    try {
      return waitAutocomplete.until(
              ExpectedConditions.presenceOfElementLocated(selectorLink)).isDisplayed();
    } catch (NoSuchElementException e) {
      logger.error("The option to be selected was not found", e);
      return false;
    }
  }

  /**
   * Selects the near me search option.
   */
  public void selectNearMe() {
    logger.debug("Select Near Me");
    WebElement whereInput = container.findElement(whereInputBy);

    if (!Strings.isNullOrEmpty(whereInput.getAttribute(VALUE_ATTRIBUTE))) {
      clearWhereField();
    }

    // click outside to clear the focus
    container.findElement(labelWhereBy).click();
    whereInput.click();

    WebDriverWait waitAutocomplete =
            new WebDriverWait(DriverBase.getDriver(), TIMEOUT_FOR_AUTOCOMPLETE);
    WebElement autocomplete = waitAutocomplete
            .until(ExpectedConditions.visibilityOfElementLocated(autocompleteBy));
    logger.debug(AUTOCOMPLETE_VISIBLE);
    autocomplete.findElement(autocompleteFirstLinkBy).click();
    logger.debug("Selected Near Me");

    waitUntilSearchButtonIsClickable();
  }

  /**
   * Selects the all restaurants search option.
   */
  public void selectAllRestaurants() {
    logger.debug("Select Near Me");
    WebElement whatInput = container.findElement(whatInputBy);

    if (!Strings.isNullOrEmpty(whatInput.getAttribute(VALUE_ATTRIBUTE))) {
      clearWhereField();
    }
    whatInput.click();

    WebDriverWait waitAutocomplete =
            new WebDriverWait(DriverBase.getDriver(), TIMEOUT_FOR_AUTOCOMPLETE);
    WebElement autocomplete = waitAutocomplete
            .until(ExpectedConditions.visibilityOfElementLocated(autocompleteBy));
    logger.debug(AUTOCOMPLETE_VISIBLE);
    autocomplete.findElement(autocompleteFirstLinkBy).click();
    logger.debug("Selected all restaurants");
    waitAutocomplete.until(ExpectedConditions.invisibilityOfElementLocated(autocompleteBy));
    logger.debug("Autocomplete is closed");

    waitUntilSearchButtonIsClickable();
  }

  /**
   * Clears the what field.
   */
  private void clearWhatField() {
    logger.debug("Clear what field");
    container.findElement(whatInputBy).click();
    container.findElement(clearWhatButtonBy).click();
    logger.debug("Clicked clear button in what field");
    logger.debug("Wait until clear button is not visible");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_CLEAR_BUTTON);
    wait.until(ExpectedConditions.invisibilityOfElementLocated(clearWhatButtonBy));
    logger.debug("Clear button is not visible");
  }

  /**
   * Clears the where field.
   */
  private void clearWhereField() {
    logger.debug("Clear where input field");
    container.findElement(whereInputBy).click();
    container.findElement(clearWhereButtonBy).click();
    logger.debug("Clicked clear button in where field");
    logger.debug("Wait until clear button is not visible");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_CLEAR_BUTTON);
    wait.until(ExpectedConditions.invisibilityOfElementLocated(clearWhereButtonBy));
    logger.debug("Clear button is not visible");
  }

  private static void waitUntilSearchButtonIsClickable() {
    logger.debug("Wait until the search button is clickable");
    WebDriverWait wait = new WebDriverWait(DriverBase.getDriver(), TIMEOUT_SEARCH_BUTTON_CLICKABLE);
    wait.until(ExpectedConditions.elementToBeClickable(searchButtonBy));
    logger.debug("The search button is clickable");
  }

  /**
   * Performs a search using the what and where arguments.
   * @param what keyword to be searched.
   * @param where location to be searched.
   * @return The search page with the search result.
   */
  public SearchPage search(String what, String where) {
    TestReporter.addInfoToReport("Search what: '" + what + "', where: '" + where + "'");
    enterWhat(what);
    selectFromAutocomplete(what);
    enterWhere(where);
    selectFromAutocomplete(where);
    return launchSearch();
  }

  @Override
  protected void isLoaded() throws Error {
    try {
      // verify that each element is displayed before continuing
      container.findElement(whatInputBy);
      logger.debug("What input is displayed");
      container.findElement(whereInputBy);
      logger.debug("Where input is displayed");
      container.findElement(searchButtonBy);
      logger.debug("Search button is displayed");
      container.findElement(labelWhereBy);
      logger.debug("Where label is displayed");
      container.findElement(labelWhatBy);
      logger.debug("What label is displayed");
    } catch (Exception e) {
      throwNotLoadedException("The search component was not loaded correctly", e);
    }

    logger.debug("SearchComponent was loaded correctly");
  }
}