package com.lcec.utility;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import javax.imageio.ImageIO;
import javax.naming.AuthenticationException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

//import javax.imageio.ImageIO;


import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import atu.testng.reports.utils.Utils;
//import atu.testrecorder.ATUTestRecorder;
//import atu.testrecorder.exceptions.ATUTestRecorderException;
import net.sf.json.JSONException;

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
	MethodListener.class })
public class BrowserMethods extends WebAppCommon
{
	{
		System.setProperty("atu.reporter.config","C:\\Users\\Tapana.Kumar\\Desktop\\VC_Test_Automation\\src\\atu.properties");
	}
	//public static Properties configProperties = readPropertyFile("//resource//config//FrameworkConfig.properties");
	//public static Properties vcProperties = readPropertyFile("//resource//config//VCobjects.properties");
	public static String chromeDriverPath;
	private static Logger Log = Logger.getLogger(BrowserMethods.class.getName());
	//in before class open chrome browser
	private String BrowserName=PropertyHandler.getProperty("BrowserName");
	//private ATUTestRecorder recorder=null;
	public  void setAuthorInfoForReports(String AuthoreName) 
	{
		ATUReports.setAuthorInfo(AuthoreName, Utils.getCurrentTime(),"1.0");  
	}

	private void setIndexPageDescription()
	{
		ATUReports.indexPageDescription = "LRN LCEC AUTOMATION REPORT <br/> <b>LCEC CreateUser And Search User Script</b>";
	}

	////	@SuppressWarnings("unchecked")
	//	public <T> T loadObject(T t)
	//	{
	//		return (T) PageFactory.initElements(driver, t.getClass());
	//	}
	//	public void getTextVerify(String actual,String expected)
	//	{
	//		Assert.assertEquals(actual, expected);
	//	}




	@BeforeTest
	public void createHtmltView()  
	{


		ATUReports.setWebDriver(driver);
		ATUReports.indexPageDescription = "LRN LCEC Test Report";
		setIndexPageDescription();
		report=new ExtentReports(PropertyHandler.getProperty("AutomationHTMLView"));

		//----------------------------------------record ATUTestRecord---------------------------
		//recorder = new ATUTestRecorder("C:\\Users\\Tapana.Kumar\\Desktop\\VC_Test_Automation\\reportLayer\\lrnHTMLReport","SearchUser&CreateUser",false);
		//ATUReports.add("<a href=\"#\">C:\\Users\\Tapana.Kumar\\Desktop\\VC_Test_Automation\\reportLayer\\lrnHTMLReport\\LrnAutomation.html</a>", true);
		//recorder.start(); 
		openBrowser(BrowserName);
		driver.get(PropertyHandler.getProperty("url"));

	}
	@BeforeClass
	public void openBrowser()
	{
		//openBrowser(BrowserName);
		//driver.get(PropertyHandler.getProperty("url"));
		//create extentReportHTML
	}
	@BeforeMethod
	public void loadLocater()
	{

		DOMConfigurator.configure(PropertyHandler.getProperty("Loger"));
	}
	@AfterClass
	public void closeBrowser()
	{
		//driver.quit();
		
	}
	@AfterTest
	public void loadAllTestLogor() 
	{
		driver.quit();
		report.endTest(logger);
		report.flush();
		//recorder.stop();
		
	}
	//@AfterMethod
	//public void validateTest() 
	//{
	//	report.endTest(logger);
	//	report.flush();
	//}



	/*public BrowserMethods()
	{
		if(report==null)
		{
			report=new ExtentReports("C:\\Report\\VirtualCatalystAutomation.html");
			String pathApp=configProperties.getProperty("pathApp");
			chromeDriverPath=configProperties.getProperty("chromeDriverPath");
		}

	}*/




	/*@AfterClass

	public void closeBrowser()
	{
		try {
			closeDriver();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@AfterMethod
	public void postTestCaseExecution(ITestResult result ,Method method) throws AuthenticationException, JSONException
	{
		Test test = method.getAnnotation(Test.class);
		String testCaseID = test.testName();
		String status = Integer.toString(result.getStatus());
		//Execution.put(testCaseID, status);
		JiraRestUtility.updateTestStatusInJira(result, method);	
		
		System.out.println(testCaseID);
		System.out.println(status);
		
		/*try{
			if(result.getStatus()==ITestResult.FAILURE)
			{
				String c=result.getName();
				String screenshot_path=captureScreenshot3(driver,c);
				String image= logger.addScreenCapture(screenshot_path);
				logger.log(LogStatus.FAIL, image);
				//logger.log(LogStatus.FAIL, "Title verification", image);
			}

			report.endTest(logger);
			//report.endTest(OnlineSetUP.logger);
			report.flush();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
	}


/*	@AfterMethod
	public void tearDown(ITestResult result)
	{
		try{
			if(result.getStatus()==ITestResult.FAILURE)
			{
				String c=result.getName();

				String screenshot_path=captureScreenshot3(driver,c);
				String image= logger.addScreenCapture(screenshot_path);
				logger.log(LogStatus.FAIL, "Title verification", image);
			}
			//report.endTest(logger);
			//report.flush();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/


	/*@AfterTest
	public void endClass1() throws Exception
	{
		System.setProperty("webdriver.chrome.driver",chromeDriverPath);
		WebDriver driver=new ChromeDriver();
		//	driver.get("http://google.com");
		driver.get("C:\\Report\\VirtualCatalystAutomation.html");
		driver.manage().window().maximize();

	}
	 */


	public  String captureScreenshot3(WebDriver driver,String screenShotname) throws AWTException 
	{
		String desr="C:\\Users\\Tapana.Kumar\\Desktop\\VC_Test_Automation\\screenshots\\"+screenShotname+".jpeg";	
		File destination= new File(desr);
		try {
			System.out.println("DRIVER IS-----------   " +driver);
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			// FileUtils.copyFile(scrFile, destination);
			Robot robot = new Robot();
			BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(screenShot, "JPG", new File(desr));
			System.out.println("screenshot Taken");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return desr;

	}

	// @BeforeClass
	/*public void launchApp() throws Exception{
		String pathApp=wdriver.configProperties.getProperty("pathApp");
			String chromeDriverPath=wdriver.configProperties.getProperty("chromeDriverPath");
		   File file=new File(pathApp);
		System.setProperty("webdriver.chrome.driver",chromeDriverPath);
	          ChromeOptions options = new ChromeOptions();
	          options.setBinary(file);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		// capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		Thread.sleep(6000);
		//  driver = new ChromeDriver(capabilities);
		System.out.println("DRIVER IS-----------   " +driver);

	}*/


	public static void getAllrecords(String elementID) 
	{
		Select oSelect = new Select(driver.findElement(By.id(elementID)));
		int size= oSelect.getOptions().size();
		oSelect.selectByIndex(size-1);
	}


	public static boolean isElementEnabled(String elementName,String id) {
		WebElement isElementEnabled = null;
		try {
			isElementEnabled=driver.findElement(By.id(id));
			System.out.println(elementName + ": Present");


		} catch(Exception e){
			Assert.fail(elementName + " Not Found ");

		}
		return  isElementEnabled.isEnabled();

	}

	public static int getTotalValuesInDropdown(String elementID)
	{

		Select oSelect = new Select(driver.findElement(By.id(elementID)));

		List <WebElement> elementCount = oSelect.getOptions();

		int iSize = elementCount.size();

		for(int x=0; x<iSize;x++)
		{
			highLightElement(driver, elementCount.get(x));

		}
		return iSize;
	}


	public static void getDefaultPage(String elementID)
	{
		Select oSelect = new Select(driver.findElement(By.id(elementID)));
		int size= oSelect.getOptions().size();
		oSelect.selectByVisibleText("10");
	}
	public static void clearTextFileds(String element,String strHTMLID) throws Exception
	{
		try 
		{
			if (waitForElementPresentByID(element,strHTMLID))
			{
				driver.findElement(By.id(strHTMLID)).clear();

			}
			else
			{
				Reporter.log("Could not click on " + strHTMLID + " because it was not found.");
				failTestcase("Could not click on " + strHTMLID + " because it was not found.");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	public static String getTextByXpath(String element, String xpath)
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

		}
		catch(Exception e){
			e.printStackTrace();
			Assert.fail(element+ " not found");
		}
		//	highLightElement(driver,strElement);
		return strElement.getText();
	}

	public static String getTextByID(String element, String id)
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));

		}
		catch(Exception e){
			e.printStackTrace();
			Assert.fail(element+ " not found");
		}
		//	highLightElement(driver,strElement);
		return strElement.getText();
	}
}





