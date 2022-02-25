package pageobjects.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pageobjects.base.AbstractComponent;

public class Footer extends AbstractComponent {

  // Selectors
  private static final By aboutPageBy = By.cssSelector("[data-test='tf_web_footer_aboutUs']");
  private static final By loyaltyPageBy =
          By.cssSelector("[data-test='tf_web_footer_loyaltyProgram']");
  private static final By contactPageBy = By.cssSelector("[data-test='tf_web_footer_contact']");
  private static final By cguPageBy = By.cssSelector("[data-test='tf_web_footer_CGU']");
  private static final By areYouRestaurantPageBy =
          By.cssSelector("[data-test='tf_web_footer_restaurant']");
  private static final By cookiePolicyPageBy =
          By.cssSelector("[data-test='tf_web_footer_cookiePolicy']");
  private static final By cookieConsentPageBy =
          By.cssSelector("[data-test='tf_web_footer_evidon']");
  private static final By faqPageBy = By.cssSelector("[data-test='tf_web_footer_faq']");
  private static final By careersPageBy = By.cssSelector("[data-test='tf_web_footer_weRecruit']");
  private static final By michelinPageBy = By.cssSelector("[data-test='tf_web_footer_michelin']");


  public Footer(WebElement container) {
    super(container);
  }

  @Override
  protected void isLoaded() throws Error {
    try {
      // verify that each element is displayed before continuing
      container.findElement(aboutPageBy);
      logger.debug("The about page link is displayed");
      container.findElement(loyaltyPageBy);
      logger.debug("The loyalty page link is displayed");
      container.findElement(contactPageBy);
      logger.debug("The contact page link is displayed");
      container.findElement(cguPageBy);
      logger.debug("The CGU page link is displayed");
      container.findElement(areYouRestaurantPageBy);
      logger.debug("The are you a restaurant page link is displayed");
      container.findElement(cookiePolicyPageBy);
      logger.debug("The cookie policy page link is displayed");
      container.findElement(cookieConsentPageBy);
      logger.debug("The cookie consent page link is displayed");
      container.findElement(faqPageBy);
      logger.debug("The FAQ page link is displayed");
      container.findElement(careersPageBy);
      logger.debug("The careers page link is displayed");
      container.findElement(michelinPageBy);
      logger.debug("The michelin page link is displayed");
    } catch (Exception e) {
      throwNotLoadedException("The footer component was not loaded correctly", e);
    }

    logger.debug("Footer component was loaded correctly");
  }
}
