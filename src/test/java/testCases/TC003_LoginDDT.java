package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;


public class TC003_LoginDDT extends BaseClass{

	@Test(dataProvider="LoginData", dataProviderClass=DataProviders.class, groups="Datadriven") //here since the dataprovider is not in the same class and is in different class so we specified 2nd parameter and specified the className Dataproviders.class and then import it from utilities package
	public void verify_loginDDT(String email,String pwd, String exp) throws InterruptedException   { //here these parameters are coming from the Data Provider array[][] i.e email, password, valid/invalid in excel sheet
		
		logger.info("**** Starting TC_003_LoginDDT ****");
		try {
		HomePage hp = new HomePage(driver);
		hp.clickMyAccount();
		hp.clickLogin();
		
		//Login Page
		LoginPage lp = new LoginPage(driver);
		lp.setEmail(email);
		lp.setPassword(pwd);
		lp.clickLogin();
		
		//My Account page
		MyAccountPage macc = new MyAccountPage(driver);
		boolean targetPage = macc.isMyAccountPageExists();
		
		
		/*Data is valid - login success - test pass - logout
		Data is valid - login failed - test fail

		Data is invalid - login success - test fail - logout
		Data is invalid - login failed - test pass */

		if(exp.equalsIgnoreCase("Valid")) {
			if(targetPage==true) {
				macc.clickLogout();
				Assert.assertTrue(true); //test pass
			}else {
				Assert.assertTrue(false); //test fail
			}
		}else if(exp.equalsIgnoreCase("Invalid")) {
			if(targetPage==true) {
				macc.clickLogout();
				Assert.assertTrue(false);//test fail
			}else {
				Assert.assertTrue(true); // test pass
			}
		}
		}catch(Exception e) {
			Assert.fail();
		}
		Thread.sleep(2000); //here each cycle ends as looping is took care by DataProvider.java class, so added Thread.sleep(2000) to see each execution
		logger.info("**** Finished TC_003_LoginDDT ****");
	}
}
