package pageobjects.components;

import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import pageobjects.base.AbstractComponent;

public class SearchResultList extends AbstractComponent {

  // Selectors
  private static final By resultItemsBy = By.cssSelector(".card");
  private static final By tagYumsX2By =
          By.cssSelector("span[data-test='search-restaurant-tags-SUPER_YUMS']");

  // Components
  private final ArrayList<SearchResultItem> listOfResults;

  public SearchResultList(WebElement container) {
    super(container);
    listOfResults = new ArrayList<>();
  }

  /**
   * Initializes the component without container.
   */
  public SearchResultList() {
    super(null);
    listOfResults = new ArrayList<>();
  }

  /**
   * Get a SearchResultItem component.
   * @param index the index of the result item
   * @return a SearchResultItem
   */
  public SearchResultItem getResult(int index) {
    logger.debug("Get search result item: {}", index);
    return listOfResults.get(index);
  }

  /**
   * Get the number of search result items in the search result list.
   * @return number of results in the list
   */
  public int getNumberOfResults() {
    logger.debug("Get number of search result items");
    return listOfResults.size();
  }


  /**
   * Checks if the tag yumsX2 is present in the search list.
   *
   * @return True if the tag yumsX2 is present. False otherwise.
   */
  public boolean isYumsX2PresentInSearchResult() {
    logger.debug("Check whether the tag yumsX2 is present in the search list");
    try {
      return container.findElement(tagYumsX2By).isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("The tag yumsX2 is not present in the list", e);
      return false;
    }
  }

  @Override
  protected void isLoaded() throws Error {

    try {
      // verify that each element is displayed before continuing
      container.findElement(resultItemsBy);
    } catch (Exception e) {
      throwNotLoadedException("The search result list was not loaded correctly", e);
    }

    // initializes each result item and add it to the list
    int i = 0;
    for (WebElement resultItemContainer: container.findElements(resultItemsBy)) {
      logger.debug("Adding search result item {} to the search result list", i);
      i++;
      SearchResultItem resultItem = new SearchResultItem(resultItemContainer);
      resultItem.get();
      listOfResults.add(resultItem);
    }

    logger.debug("SearchResultList component was loaded correctly");
  }
}

