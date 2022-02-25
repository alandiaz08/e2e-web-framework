package pageobjects.components;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import pageobjects.base.AbstractComponent;

public class SearchResultItem extends AbstractComponent {

  // Selectors
  private static final By pictureBy = By.tagName("img");
  private static final By insiderPictureTag = By.cssSelector("[data-test='insider-medal']");
  private static final By timeslotListBy = By.cssSelector("li > a");
  private static final By imageContainerBy = By.cssSelector("div > div");
  private static final By restaurantContainerBy = By.cssSelector("div > div + div");

  private static final String NUMBER_OF_TIMESLOTS = "There are {} timeslots";
  private static final String EMPTY_TIMESLOTS = "The list of timeslots is empty";
  private static final String INDEX_GREATER_NUMBER_OF_TIMESLOTS =
          "The index {} is greater than the number of timeslots {}";
  private static final By restaurantNameBy = By.tagName("a");
  private static final By cuisineTagBy = By.cssSelector("[data-test='search-restaurant-tags-DEFAULT']");


  /**
   * Initializes the selectors specific to the search result item on the search results page.
   * @param container the container of the Search Result Item
   */
  public SearchResultItem(WebElement container) {
    super(container);

    // initializes selectors to be used by the super class
  }

  /**
   * Checks if the restaurant has a picture.
   * @return True if a picture is visible. False otherwise.
   */
  public boolean hasPicture() {
    logger.debug("Has picture tag");
    try {
      return container.findElement(pictureBy).isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("Picture not found", e);
      return false;
    }
  }

  /**
   * Checks if the restaurant has an insider picture tag (small IN over the restaurant picture).
   * @return True if the restaurant has an insider tag. False otherwise.
   */
  public boolean hasInsiderPictureTag() {
    logger.debug("Has insider picture tag");
    try {
      return container.findElement(insiderPictureTag).isDisplayed();
    } catch (NoSuchElementException e) {
      logger.debug("Insider picture tag not found", e);
      return false;
    }
  }

  /**
   * Gets the cuisine tag.
   * @return the cuisine tag.
   */
  public String getCuisineTag() {
    logger.debug("Get the cuisine tag");
    return container.findElement(cuisineTagBy).getText();
  }


  /**
   * Gets the hour displayed in the timeslot.
   * @param index The index of the timeslot.
   * @return The hour of the timeslot.
   */
  public String getTimeSlotHour(int index) {
    logger.debug("Get the hour on the timeslot index {}", index);

    List<WebElement> listOfTimeSlots = container.findElements(timeslotListBy);
    logger.debug(NUMBER_OF_TIMESLOTS, listOfTimeSlots.size());

    if (listIfEmptyOrSmallerThanIndex(listOfTimeSlots, index)) {
      throw new IllegalArgumentException();
    } else {
      String timeslotText = listOfTimeSlots.get(index).getText();

      // we need to clean the text, remove the offer from the whole text
      if (hasTimeSlotOffer(index)) {
        timeslotText = timeslotText.replace(getTimeSlotOffer(index), "");
        timeslotText = timeslotText.trim();
      }

      return timeslotText;
    }
  }

  /**
   * Gets the size of timeSlots list.
   * @return the list of timeSlots.
   */
  public int getNumberOfTimeslots() {
    logger.debug("Get the size of timeSlots list");
    try {
      return container.findElements(timeslotListBy).size();
    } catch (NoSuchElementException e) {
      logger.error("The list of timeslots was not found", e);
      throw e;
    }
  }

  private static boolean listIfEmptyOrSmallerThanIndex(@NotNull List<WebElement> listOfTimeSlots,
                                                       int index) {
    if (listOfTimeSlots.isEmpty()) {
      logger.error(EMPTY_TIMESLOTS);
      return true;
    } else if (listOfTimeSlots.size() <= index) {
      logger.error(INDEX_GREATER_NUMBER_OF_TIMESLOTS,
              index,
              listOfTimeSlots.size());
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets the offer displayed in the timeslot.
   * @param index The index of the timeslot.
   * @return The offer of the timeslot.
   */
  public String getTimeSlotOffer(int index) {
    logger.debug("Get the offer on the timeslot index {}", index);

    List<WebElement> listOfTimeSlots = container.findElements(timeslotListBy);
    logger.debug(NUMBER_OF_TIMESLOTS, listOfTimeSlots.size());

    if (listIfEmptyOrSmallerThanIndex(listOfTimeSlots, index)) {
      throw new IllegalArgumentException();
    } else {
      if (hasTimeSlotOffer(index)) {
        return listOfTimeSlots.get(index).findElement(By.tagName("span")).getText();
      } else {
        return "";
      }
    }
  }

  /**
   * Checks if the timeslot has an offer displayed.
   * @param index The index of the timeslot.
   * @return True if the timeslot has an offer. False otherwise.
   */
  public boolean hasTimeSlotOffer(int index) {
    logger.debug("Has timeslot offer {}", index);

    List<WebElement> listOfTimeSlots = container.findElements(timeslotListBy);
    logger.debug(NUMBER_OF_TIMESLOTS, listOfTimeSlots.size());

    if (listIfEmptyOrSmallerThanIndex(listOfTimeSlots, index)) {
      throw new IllegalArgumentException();
    } else {
      try {
        return listOfTimeSlots.get(index).findElement(By.tagName("span")).isDisplayed();
      } catch (NoSuchElementException e) {
        logger.debug("No offer in the timeslot", e);
        return false;
      }
    }
  }

  @Override
  protected void isLoaded() throws Error {
    // To be implemented

    try {
      // verify that each element is displayed before continuing
      container.findElement(restaurantNameBy);
      logger.debug("The restaurant name is displayed");
      container.findElement(imageContainerBy);
      logger.debug("The restaurant image is displayed");
      container.findElement(restaurantContainerBy);
      logger.debug("The restaurant data is displayed");
    } catch (Exception e) {
      throwNotLoadedException("The search result item was not loaded correctly", e);
    }

    logger.debug("SearchResultItem component was loaded correctly");
  }
}
