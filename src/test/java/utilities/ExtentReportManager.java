 package utilities;
 
 import java.awt.Desktop;
 import java.io.File;
 import java.io.IOException;
 //import java.net.URL;
 import java.net.URL;

 //Extent report 5.x...//version

 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.List;

// import org.apache.commons.mail.DefaultAuthenticator;
// import org.apache.commons.mail.ImageHtmlEmail;
// import org.apache.commons.mail.resolver.DataSourceUrlResolver;
 import org.testng.ITestContext;
 import org.testng.ITestListener;
 import org.testng.ITestResult;

 import com.aventstack.extentreports.ExtentReports;
 import com.aventstack.extentreports.ExtentTest;
 import com.aventstack.extentreports.Status;
 import com.aventstack.extentreports.reporter.ExtentSparkReporter;
 import com.aventstack.extentreports.reporter.configuration.Theme;

 import testBase.BaseClass;

public class ExtentReportManager implements ITestListener{
	
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;

	public void onStart(ITestContext testContext) { //testContext is which "test cases"(in testCases package) got executed is captured
		
		/*SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt=new Date();
		String currentdatetimestamp=df.format(dt);
		*/
		
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		repName = "Test-Report-" + timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

		sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Title of report
		sparkReporter.config().setReportName("opencart Functional Testing"); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);
		
		//populating common info in the report
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name")); //this will return the tester name who is currently running the tests by using System.getProperty("user.name"). This comes from environment variable
		extent.setSystemInfo("Environment", "QA");
		
		String os = testContext.getCurrentXmlTest().getParameter("os"); // getCurrentXmlTest() method returns the xml file of the executed test //getting the xml file of the test executed and from that xml file getting the value of the "os" parameter used
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser"); //same logic as of os parameter
		extent.setSystemInfo("Browser", browser);
		
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups(); //captures the group names specified in the <include> tag in the xml file
		if(!includedGroups.isEmpty()) {
		extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}

	public void onTestSuccess(ITestResult result) {
	
		test = extent.createTest(result.getTestClass().getName()); //getting the class name of the executed test case and creating a new entry in report
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
		test.log(Status.PASS,result.getName()+" got successfully executed"); //result.getName() returns the class name
		
	}

	public void onTestFailure(ITestResult result) { //this method will automatically get triggered if any test case fails
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		
		test.log(Status.FAIL,result.getName()+" got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
		
		try { //attaching screenshot for failed test cases
			String imgPath = new BaseClass().captureScreen(result.getName()); //captures ss and adds to the screenshot folder and return the targetFilePath
			test.addScreenCaptureFromPath(imgPath); //using targetFilePath that screenshot is added to the reports
			
		} catch (IOException e1) {// if in case ss i not properly taken and it is trying to add to the reports in that case it throws FileNotFoundException
			e1.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+" got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	public void onFinish(ITestContext testContext) {
		
		extent.flush(); //this method will consolidate all info from the report and finally it will generate
		//this extent.flus() is enough for this method but some more additional statements are written
		
		
		//the below piece of code is added to automatically open the reports as soon as the test cases are executed eliminating the need to manually go to the folder and open the reports file
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			Desktop.getDesktop().browse(extentReport.toURI());  //opens the report on the browser automatically
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		//this piece of code is used for automatically sending the report to team-mate after complete test case execution
		/*  try {
			  URL url = new  URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+repName);
		  
		  // Create the email message 
		  ImageHtmlEmail email = new ImageHtmlEmail();
		  email.setDataSourceResolver(new DataSourceUrlResolver(url));
		  email.setHostName("smtp.googlemail.com"); 
		  email.setSmtpPort(465);
		  email.setAuthenticator(new DefaultAuthenticator("pavanoltraining@gmail.com","password")); 
		  email.setSSLOnConnect(true);
		  email.setFrom("pavanoltraining@gmail.com"); //Sender
		  email.setSubject("Test Results");
		  email.setMsg("Please find Attached Report....");
		  email.addTo("pavankumar.busyqa@gmail.com"); //Receiver 
		  email.attach(url, "extent report", "please check report..."); 
		  email.send(); // send the email 
		  }
		  catch(Exception e) 
		  { 
			  e.printStackTrace(); 
			  }
		 */ 
	}

}
