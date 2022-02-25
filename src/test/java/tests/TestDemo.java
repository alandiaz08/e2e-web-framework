package tests;

import base.TestBase;
import org.testng.annotations.Test;
import pageobjects.pages.HomePage;

@Test(groups = {"full-regression"})
public class TestDemo extends TestBase {
  /**
   * Login with a customer.
   *
   */
  @Test(
      groups = {"customer"},
      description = "Login a customer",
      enabled = true,
      retryAnalyzer = TestBase.RetryAnalyzer.class
  )
  public void customerLogin() {

    // Arrange
    final String userEmail = "friday_testmail@fork.com";
    final String userPassword = "Test@12345";


    HomePage homePage = new HomePage();
    homePage.get();

    homePage.header()
            .openSidebarNotLoggedIn()
            .login(userEmail, userPassword);

    //Assert
  }
}
