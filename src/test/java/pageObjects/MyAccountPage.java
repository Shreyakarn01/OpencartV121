package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage{

	public MyAccountPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(xpath="//*[@id=\"content\"]/h2[1]") WebElement msgHeading;
	@FindBy(linkText="Logout") WebElement btnLogout;
	
	//We don't do validations in object classes, validations are done in test classes, we will write only action methods
	
	public boolean isMyAccountPageExists() {
		try {
			return msgHeading.isDisplayed();
		}catch(Exception e) {
			//If msgHeading is not displayed then it will throw exception that has to be catched
			return false; //handling exception
		}
	}
	
	public void clickLogout() {
		btnLogout.click();
	}
}
