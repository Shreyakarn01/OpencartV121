package testCases;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass{
	
	@Test(groups= {"Regression","Master"})
	public void verify_account_registration() {
		logger.info("**** Starting TC001_AccountRegistrationTest ****");
		try { //putting whole code in try-catch block to log the exceptions also
		HomePage hp = new HomePage(driver);
		hp.clickMyAccount();
		logger.info("Clicked on MyAccount Link");
		hp.clickRegister();
		logger.info("Clicked on Register Link");
		
		AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
		logger.info("Providing customer details...");
		regpage.setFirstName(randomString().toUpperCase());
		regpage.setLastName(randomString().toUpperCase());
		regpage.setEmail(randomString()+"@gmail.com");
		regpage.setTelephone(randomNumber());
		
		String password = randomAlphaNumeric();
		regpage.setPassword(password);
		regpage.setConfirmPassword(password);
		regpage.setPrivacyPolicy();
		regpage.clickContinue();
		
		
		logger.info("Validating expected message...");
		String message = regpage.getConfirmationMsg();
		
		if(message.equals("Your Account Has Been Created!")) {
			Assert.assertTrue(true);
		}else {
			//We put the error and debug message in if-else as the catch block was not getting executed as there were no exception occuring and assertEquals was getting failed which is a hard assertion due to which the lines of codes after it won't execute so catch was not executed and in xml specify level as debug as info and error will be covered automatically
			logger.error("Test failed..");
			logger.debug("Debug logs.."); //also have to specify Log Level as Debug in log4j2.xml file in resources folder, if we specify off level in xml file then the logs we write will also not get logged
			Assert.assertTrue(false);
		}
		
//		Assert.assertEquals(message,"Your Account Has Been Created!!!");
		}catch(Exception e) {
			Assert.fail(); //fails the test due to exception
		}
		logger.info("**** Finished TC001_AccountRegistrationTest ****");
	}	
}
