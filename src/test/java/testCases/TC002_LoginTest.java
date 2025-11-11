package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC002_LoginTest extends BaseClass{
	
	@Test(groups={"Sanity","Master"}) //Master means all test cases must run
	public void verify_login() {
		logger.info("**** Starting TC002_LoginTest ****");
		
		
		try {
		//Home page
		HomePage hp = new HomePage(driver);
		hp.clickMyAccount();
		hp.clickLogin();
		
		//Login Page
		LoginPage lp = new LoginPage(driver);
		lp.setEmail(prop.getProperty("email"));
		lp.setPassword(prop.getProperty("password"));
//		lp.setPassword(prop.getProperty("123")); //--> intentionally failing this test case to see if ss is generated in the report file or not
		lp.clickLogin();
		
		//My Account page
		MyAccountPage macc = new MyAccountPage(driver);
		boolean targetPage = macc.isMyAccountPageExists();
		
//		Assert.assertEquals(targetPage, true, "Login failed"); //or,
		Assert.assertTrue(targetPage);
		}catch(Exception e) {
			Assert.fail();
		}
		logger.info("**** Finished TC002_LoginTest ****");
		
	}

}
