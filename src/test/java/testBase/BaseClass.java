package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BaseClass {

	public static WebDriver driver; //we made WebDriver static here b'coz in the ExtentReportManager class when test fails i.e. in the onTestFailure() method we are capturing ss using the function captureScreen() method in BaseClass, hence we are creating an object of the BaseClass in that class to call captureScreen() method, so a new driver will get created and this configured driver will not be used and conflict happens, so making it static so that it belongs to all the objects.
	protected Properties prop;
	public Logger logger; //import the package -->  import org.apache.logging.log4j.Logger;
	@BeforeClass(groups={"Sanity","Regression","Master"}) //all the groups must execute setup and teardown so written here, Datadriven group not included for now as it will take more time
	@Parameters({"os","browser"}) //if we are getting parameters from the xml file, then the execution depends on the xml file, so we have to now run code from xml file only, we cannot run from here directly
	public void setUp(String os, String br) throws IOException {
		//Loading config.properties file
		
		FileInputStream file = new FileInputStream("./src//test//resources//config.properties"); //Instead of FileInputStream, you can also use FileReader class in the same manner
		prop = new Properties();
		prop.load(file);
		
		
		
		logger = LogManager.getLogger(this.getClass()); //the class name should be taken dynamically as there may be multiple classes(i.e. test cases), that will require setUp() method and this is done using "this" keyword
		
		
		if(prop.getProperty("execution_env").equalsIgnoreCase("remote")) {
			DesiredCapabilities cap = new DesiredCapabilities();
			
			if(os.equalsIgnoreCase("windows")) {
				cap.setPlatform(Platform.WIN11);
			}else if(os.equalsIgnoreCase("mac")) {
				cap.setPlatform(Platform.MAC);
			}else {
				System.out.println("No matching os");
				return;
			}
			
			
			switch(br.toLowerCase()) {
			case "chrome" : cap.setBrowserName("chrome"); break;
			case "edge" : cap.setBrowserName("MicrosoftEdge"); break;
			default: System.out.println("No matching browser"); return;
			}
			
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
		}
		
		
		
		
		if(prop.getProperty("execution_env").equalsIgnoreCase("local")) {
			switch(br.toLowerCase()) { //this is for local execution and not remote execution
			case "chrome" : 
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				break;
			case "edge" :
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				break;
			case "firefox" :
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				break;
			default : System.out.println("Invalid browser name"); return; //in default we generally do not use break because it comes in the end, and we return which means it closes the program execution
			}
		}
		
		
		
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(prop.getProperty("url")); //reading url from config.properties file
		driver.manage().window().maximize();
	}
	
	@AfterClass(groups= {"Sanity","Regression","Master","Datadriven"})
	public void tearDown() {
		driver.quit();
	}
	
	public String randomString() {
		String generatedstring = RandomStringUtils.randomAlphabetic(6); //all characters will be in lower-case
		return generatedstring;
	}
	public String randomNumber() {
		String generatednumber = RandomStringUtils.randomNumeric(10);
		return generatednumber;
	}
	public String randomAlphaNumeric() {
		String generatedstring = RandomStringUtils.randomAlphabetic(3);
		String generatednumber = RandomStringUtils.randomNumeric(3);
		return generatedstring+"$#"+generatednumber;
	}
	
	public String captureScreen(String tName) throws IOException {
		String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File src = screenshot.getScreenshotAs(OutputType.FILE);
		String targetFilePath = System.getProperty("user.dir")+"\\screenshots\\"+tName+"_"+timestamp+".png";
		File dest = new File(targetFilePath);
		
		FileUtils.copyFile(src,dest); // or, src.renameTo(dest);
		return targetFilePath;
	}
}
