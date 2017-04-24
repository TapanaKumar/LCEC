
package com.lcec.utility;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import jxl.Cell;
import jxl.Workbook;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;



public class WebAppCommon 
{
	public static ExtentReports report;
	public static ExtentTest logger;
	
	public static WebDriver driver=null;
	private static Logger Log = Logger.getLogger(WebAppCommon.class.getName());
	//public static WebDriver driver = null;
	protected static int timeoutSeconds = 30;
	//public static String BROWSER_NAME = "";
	//public static String BROWSER_VERSION = "";
	//public static String PLATFORM = "";
	private static WebDriverWait wait = null;
	//public static SoftAssert softAssert = new SoftAssert();
	//protected String logs = appPath()+ "//src//log4j.xml";
	private static String chromeDriver=PropertyHandler.getProperty("ChromeDriver");
	private static String greckoDrive=PropertyHandler.getProperty("FireFoxDriver");
	private static String IEDriver=PropertyHandler.getProperty("IEDriver");

	public static void openBrowser(String browser) 
	{
		try {
			if (browser.equalsIgnoreCase("Firefox")) 
			{
				System.setProperty("webdriver.gecko.driver", greckoDrive);
				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("chrome")) 
			{
				System.setProperty("webdriver.chrome.driver",chromeDriver);
				driver = new ChromeDriver();
				driver.manage().window().maximize();
			} else if (browser.equalsIgnoreCase("IE")) 
			{
				System.setProperty("webdriver.ie.driver",IEDriver);
				driver = new InternetExplorerDriver();
			}

		} catch (WebDriverException e) 
		{

		}
	}

	
	
	public void waitPageComplete()
	{
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	}

	public boolean waitForWindow(String title)
	{
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(70, TimeUnit.SECONDS) //How long should WebDriver wait for new window
				.pollingEvery(5, TimeUnit.SECONDS)  //How often should it check if a searched window is present
				.ignoring(NoSuchWindowException.class);
		wait.until(new Open_PopUp(title)); //Here 'title' is an actual title of the window, we are trying to find and switch to.
		return true;
	}

	private class Open_PopUp implements ExpectedCondition<String> 
	{
		private String windowTitle;	
		public Open_PopUp(String windowTitle)
		{
			this.windowTitle = windowTitle;
		}
		public String apply(WebDriver driver) 
		{
			for(String windowHandle: driver.getWindowHandles())
			{
				driver.switchTo().window(windowHandle);
				if (driver.getTitle().equalsIgnoreCase(windowTitle))
					return driver.getWindowHandle();
			}
			return null;
		}
	}
	public static void getWaitForElementPresent(String elementXpath,int time)
	{ 	
		WebDriverWait wt = new WebDriverWait(driver,time);
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXpath)));
	}
	public  static void getWaitForNamePresent(String Name,int time)
	{	
		WebDriverWait wt = new WebDriverWait(driver, time);
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.name(Name)));
	}
	public  static void getWaitForIdPresent(String id,int time)
	{		
		WebDriverWait wt = new WebDriverWait(driver,time);
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
	}
	public  static void getDroupDown(WebElement element,String value)
	{
		Select select=new Select(element);
		select.selectByVisibleText(value);
	}


	public  static void getScroolToText(String Scrooltext)
	{
		HashMap scrollObject = new HashMap();
		RemoteWebElement element = (RemoteWebElement)driver.findElement(By.className("android.widget.ListView"));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String webElementId = ((RemoteWebElement) element).getId();
		scrollObject.put("text", Scrooltext);
		scrollObject.put("element", webElementId);
		js.executeScript("mobile: scrollTo", scrollObject);
	}

	public  static void getScroolToText(String Xapth,String Scrooltext)
	{
		HashMap scrollObject = new HashMap();
		RemoteWebElement element = (RemoteWebElement)driver.findElement(By.className(Xapth));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String webElementId = ((RemoteWebElement) element).getId();
		scrollObject.put("text", Scrooltext);
		scrollObject.put("element", webElementId);
		js.executeScript("mobile: scrollTo", scrollObject);
	}


	public  static void getMouseMoveMent(WebElement MoveToLocater)
	{
		Actions actions = new Actions(driver);
		WebElement subMenu = MoveToLocater;
		actions.moveToElement(subMenu).build().perform();;
	}

	public String getText(WebElement textPrint)
	{
		String textOFLocater=textPrint.getText();
		return textOFLocater;
	}
	public String getColor(WebElement textPrint,String color)
	{
		String textOFLocater=textPrint.getCssValue(color);
		return textOFLocater;
	}
	public String getText_UseAttribute(WebElement textPrint,String AttributeName)
	{
		String textOFLocater=textPrint.getAttribute(AttributeName);
		return textOFLocater;
	}


	public String gettakeScreenShot(String imageName) throws IOException
	{
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String imagepath="C:\\Users\\Tapana.Kumar\\Desktop\\VC_Test_Automation\\screenshots\\"+imageName+".png";
		//The below method will save the screen shot in d drive with name "screenshot.png"
		FileUtils.copyFile(scrFile, new File(imagepath));
		return imagepath;
	}
	public  void getErrorMessage(String errorMessage,String imageName) throws IOException
	{
		logger.log(LogStatus.FAIL, errorMessage);
		String screenshot_path=gettakeScreenShot(imageName);
		String image=logger.addScreenCapture(screenshot_path);
		logger.log(LogStatus.FAIL, "Title verification", image);
		ATUReports.add(errorMessage, LogAs.FAILED, new CaptureScreen(ScreenshotOf.DESKTOP));
	}

	//openURL
	public static void OpenURL(String URL) throws Exception 
	{
		driver.get(URL);
	}

	
	
	
	
	/*protected static WebDriver getFFDriver() 
	{

		File pathToBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
		FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
		FirefoxProfile firefoxProfile = new FirefoxProfile();  




FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
FirefoxProfile firefoxProfile = new FirefoxProfile();       
WebDriver driver = new FirefoxDriver(ffBinary,firefoxProfile);



	//	FirefoxProfile firefoxProfile = new FirefoxProfile();
		// Disable auto update
		firefoxProfile.setPreference("app.update.enabled", false); 
		// Disable the default browser check certificate
		firefoxProfile.setPreference("browser.shell.checkDefaultBrowser", false); 
		firefoxProfile.setAcceptUntrustedCertificates(true); // Accept certificates
		firefoxProfile.setPreference("browser.tabs.autoHide", true); // Hide tabs
		firefoxProfile.setPreference("browser.tabs.warnOnClose", false); // Disable warning on tab open
		firefoxProfile.setPreference("browser.tabs.warnOnOpen", false); // Disable warning on tab close

		firefoxProfile.setPreference("browser.rights.3.shown", true); // Disable know your right options

		driver = new FirefoxDriver(ffBinary,firefoxProfile);
		driver.manage().window().maximize();
	//	driver.get(URL);		
		driver.manage().timeouts()
		.implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);

		return driver;
	}

	protected static WebDriver getChromeDriver() {
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir")
				+ "/resource/drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		//driver.get(URL);		

		return driver;
	}*/

	public static WebDriverWait getWait() 
	{
		if (wait == null) {
			wait = new WebDriverWait(driver, timeoutSeconds);
		}
		return wait;
	}

	public static void setWait(int timeoutSecs) {
		timeoutSeconds = timeoutSecs;
		wait = new WebDriverWait(driver, timeoutSeconds);
	}

	public static void setImplicitWait(int timeoutSeconds) {
		if (driver != null)
			driver.manage().timeouts()
			.implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
	}

	public static void closeDriver() throws Exception {
		try {
			if (driver != null)
				driver.quit();

			driver = null;
			wait = null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
	}


	public void closeWindow() throws Exception{
		try {
			driver.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void mouseOverByXpath(String xpath) throws InterruptedException
	{
		try
		{
			//Thread.sleep(1000);
			WebElement mainMenu=driver.findElement(By.xpath(xpath));
			//Thread.sleep(1000);
			// WebElement vc=driver.findElement(By.xpath("//a[@title='Sync manager']"));

			Actions actions = new Actions(driver);
			actions.moveToElement(mainMenu).build().perform();
			//Thread.sleep(2000);
			//Boolean t=driver.findElement(By.id("syncManager")).isDisplayed();

			//Assert.assertTrue(driver.findElement(By.id("syncManager")).isDisplayed(), "Sync manager is not present");



		}          catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*public static void launchBrowser(String browser) throws Exception
	{
		//DOMConfigurator.configure(logs);
		Log.info("# # # # # # # # # # # # # # # # # # # # # # # # # # # ");
		try{
			if (browser.equalsIgnoreCase("firefox"))
			{
				getFFDriver();
				Log.info("Executing on Firefox");
				Log.info("Exceuting on FireFox");
				System.out.println(" Executing on FireFox");
				driver = new FirefoxDriver();
				driver.get(URL);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
			}
			else if (browser.equalsIgnoreCase("chrome"))
			{
				getChromeDriver();
				Log.info("Executing on Chrome");
				Log.info("Executing on Chrome");
				System.out.println(" Executing on CHROME");
				System.out.println("Executing on IE");
				System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
				driver = new ChromeDriver();
				driver.get(URL);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
			}
			else if (browser.equalsIgnoreCase("ie"))
			{
				getIEDriver();
				Log.info("Executing on Internet Explorer");
				Log.info("Executing on Internet Explorer");
				System.out.println("Executing on IE");
				System.setProperty("webdriver.ie.driver", "D:\\IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				driver.get(URL);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
			}
			else
			{
				throw new IllegalArgumentException("The Browser Type is Undefined");
			} }
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}*/

	//read table data


	/*public String[][] readExcelData(String xlFilePath, String sheetName,String tableName1, String tableend1) throws Exception {
		String[][] tabArray1 = null;
		try{

		Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
		Sheet sheet = workbook.getSheet(sheetName);
		int startRow, startCol, endRow, endCol, ci, cj;
		Cell tableStart1 = sheet.findCell(tableName1);
		startRow = tableStart1.getRow();
		startCol = tableStart1.getColumn();

		Cell tableEnd = sheet.findCell(tableend1);
		endRow = tableEnd.getRow();
		endCol = tableEnd.getColumn();
		System.out.println("startRow=" + startRow + ", endRow=" + endRow + ", "
				+ "startCol=" + startCol + ", endCol=" + endCol);
		tabArray1 = new String[endRow - startRow - 1][endCol - startCol - 1];
		ci = 0;


		for (int i = startRow + 1; i < endRow; i++, ci++) {
			cj = 0;
			for (int j = startCol + 1; j < endCol; j++, cj++) {
				tabArray1[ci][cj] = sheet.getCell(j, i).getContents();
			}
		}

	} catch(Exception e){
		e.printStackTrace();
		throw e;
	}
		return (tabArray1);
	}


	//read table data
	public String[][] readExcelData12(String xlFilePath, String sheetName) throws Exception {
		String[][] tabArray1 = null;

		Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
		Sheet sheet = workbook.getSheet(sheetName);
		int rows = sheet.getRows();
		System.out.println(rows);
		int cols =sheet.getColumns();
		int ci =0;
		for(int row=0; row<sheet.getRows(); row++)
		{
			System.out.println(row);
			int cj=0;
			for(int col=0;col<sheet.getColumns();col++){
				tabArray1[ci][cj] = sheet.getCell(col, row).getContents();	
			}
			System.out.print(sheet.getCell(0,0).getContents());
			System.out.print(":::");
			System.out.println(sheet.getCell(1,1).getContents());
		}
		return tabArray1;
	}*/




	//clickIdentifierLinkText


	public static void clickIdentifierLinkText(String strHTMLID) throws Exception
	{
		strHTMLID = strHTMLID.trim();
		try
		{
			if (waitForElementPresentByLinkText(strHTMLID))
			{
				WebElement e1 = driver.findElement(By.linkText(strHTMLID));
				Actions builder1 = new Actions(driver);
				builder1.moveToElement(e1).click(e1);
				builder1.perform();
				Thread.sleep(1000);
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


	//clickIdentifierLinkText
	public void clickIdentifierPartialLinkText(String strHTMLID) throws Exception
	{
		strHTMLID = strHTMLID.trim();
		try
		{
			if (waitForElementPresentByPartialLinkText(strHTMLID))
			{
				WebElement e1 = driver.findElement(By.partialLinkText(strHTMLID));
				Actions builder1 = new Actions(driver);
				builder1.moveToElement(e1).click(e1);
				builder1.perform();
				Thread.sleep(1000);
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

	public void clickIdentifierCssSelector(String strHTMLID) throws Exception
	{
		strHTMLID = strHTMLID.trim();
		try
		{
			if (waitForElementPresentByCssSelector(strHTMLID))
			{
				WebElement e1 = driver.findElement(By.cssSelector(strHTMLID));
				Actions builder1 = new Actions(driver);
				builder1.moveToElement(e1).click(e1);
				builder1.perform();
				Thread.sleep(1000);
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

	//clickIdentifier xpath
	public static void clickIdentifierXpath(String element,String xpath) throws Exception
	{
		xpath = xpath.trim();
		try
		{
			if (waitForElementPresentByXpath(element,xpath))
			{

				Thread.sleep(2000);
				driver.findElement(By.xpath(xpath)).click();
				//	WebElement link = driver.findElement(By.xpath(xpath));
				/*Actions builder1 = new Actions(driver);
						builder1.click();
						builder1.perform();*/
				Thread.sleep(1000);	
			}
			else
			{
				Reporter.log("Could not click on " + xpath + " because it was not found.");
				failTestcase("Could not click on " + xpath + " because it was not found.");
			}
		}
		catch (Exception e) 
		{

			e.printStackTrace();
			throw e;
		}
	}



	public void explicitWait_CSS(String cssValue)
	{
		try
		{WebDriverWait wait = new WebDriverWait(driver, 80);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssValue)));
		}

		catch(Exception e){
			Assert.fail(cssValue + " Not Found ");
		}
	}

	//waitForElementPresentByPartialLinkText
	public boolean waitForElementPresentByPartialLinkText(String element) throws Exception
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(element)));
			if(strElement.isDisplayed() == false)
			{
				failTestcase("Waited for the TIMEOUT period but element " + strElement + " not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strElement.isDisplayed();
	}

	//clickIdentifierLinkText
	public static void clickIdentifierByID(String element,String strHTMLID) throws Exception
	{
		strHTMLID = strHTMLID.trim();
		try
		{
			if (waitForElementPresentByID(element,strHTMLID))
			{

				WebElement e1 = driver.findElement(By.id(strHTMLID));
				highLightElement(driver,e1);
				e1.click();
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

	//wait for element to visible
	public static boolean waitForElementPresentByID(String element,String id) throws Exception
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
			highLightElement(driver,strElement);
		}
		catch(Exception e){
			Assert.fail(element + " Not Found ");
		}

		return strElement.isDisplayed();
	}

	//waitForElementPresentByLinkText
	public static boolean waitForElementPresentByLinkText(String element) throws Exception
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element)));

		}
		catch(Exception e){
			Assert.fail(element + " Not Found ");
		}

		return strElement.isDisplayed();
	}





	/*	Name  - readPropertyFile*/
	public static Properties readPropertyFile(String pFileName) 
	{   
		FileInputStream fileSource = null;
		Properties propertyLoad = null;
		try 
		{
			pFileName = appPath() + pFileName; 
			System.out.println("�bsolute path of WorkSpace---"+appPath());
			System.out.println("Absolute path of file---"+pFileName);
			propertyLoad = new Properties();
			propertyLoad.load(new FileInputStream(pFileName));
			propertyLoad.load(new FileInputStream(pFileName));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(fileSource!=null)
			{
				try 
				{
					fileSource.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}	
		}
		return propertyLoad;	
	}

	//appPath
	public static String appPath() 
	{
		String strAppPath = null;
		try
		{
			java.io.File currentDir = new java.io.File("");
			strAppPath = currentDir.getAbsolutePath();
		}
		catch (Exception e)
		{
			strAppPath = "-1";
			e.printStackTrace();
		}
		return strAppPath;
	}


	//waitForElementPresentByxpath
	public static boolean waitForElementPresentByXpath(String element, String xpath1) throws Exception
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath1)));
			Log.info(strElement +  " is present");
			highLightElement(driver, strElement);
			Assert.assertTrue(strElement.isDisplayed(),element + "is not prsenet");
		}

		catch(Exception e){
			Assert.fail(element + " Not Found ");
		}

		return strElement.isDisplayed();
	}

	public boolean waitForElementPresentByCssSelector(String element) throws Exception
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element)));
			if(strElement.isDisplayed() == false)
			{
				failTestcase("Waited for the TIMEOUT period but element " + strElement + " not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strElement.isDisplayed();
	}


	public static void typeTextById(String element,String strHTMLID, String strString) throws Exception
	{
		strHTMLID = strHTMLID.trim();
		strString = strString.trim();
		try 
		{
			if (waitForElementPresentByID(element,strHTMLID))
			{
				driver.findElement(By.id(strHTMLID)).clear();
				driver.findElement(By.id(strHTMLID)).sendKeys(strString);
				highLightElement(driver,driver.findElement(By.id(strHTMLID)));
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

	public void typeTextByXpath(String element,String strHTMLID, String strString) throws Exception
	{
		strHTMLID = strHTMLID.trim();
		strString = strString.trim();
		try 
		{
			if (waitForElementPresentByXpath(element,strHTMLID))
			{
				driver.findElement(By.xpath(strHTMLID)).clear();
				driver.findElement(By.xpath(strHTMLID)).sendKeys(strString);
				highLightElement(driver,driver.findElement(By.id(strHTMLID)));
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

	//selectDropdownValue
	public void selectDropdownValueVisibleText(String element ,String strHTMLID, String strValue) throws Exception
	{
		strHTMLID = strHTMLID.trim();
		strValue = strValue.trim();
		try
		{
			if (waitForElementPresentByID(element,strHTMLID))
			{
				Select drpdown = new Select(driver.findElement (By.id(strHTMLID)));
				drpdown.selectByVisibleText(strValue);
			}
			else
			{
				Reporter.log("Could not click on " + strHTMLID + " because it was not found.");
				failTestcase("Could not click on " + strHTMLID + " because it was not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	//failTestcase
	public static void failTestcase(String e)
	{
		Reporter.log(" Failed because " + e);
		Assert.fail("--------- FAILED ----------");
	}


	//assert2Strings
	public void assert2Strings(String strString1, String strString2)
	{
		Assert.assertEquals(strString1,strString2);        	
	}

	//assert title bar
	public void assertTitleBar(String strString1, String strString2)
	{
		Assert.assertEquals(strString1,strString2);        	
	}


	//getValuebyid
	public String getValueByID(String element,String strHTMLID)
	{
		String strValue = null;
		try
		{
			if (waitForElementPresentByID(element,strHTMLID))
			{
				strValue = driver.findElement(By.id(strHTMLID)).getAttribute("value");
			}
			else
			{
				Reporter.log("Could not click on " + strHTMLID + " because it was not found.");
				failTestcase("Could not click on " + strHTMLID + " because it was not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strValue;

	}
	//getValuebyid
	public String getValueByXpath(String element,String strHTMLID)
	{
		String strValue = null;
		try
		{
			if (waitForElementPresentByXpath(element,strHTMLID))
			{
				strValue = driver.findElement(By.xpath(strHTMLID)).getAttribute("value");
			}
			else
			{
				Reporter.log("Could not click on " + strHTMLID + " because it was not found.");
				failTestcase("Could not click on " + strHTMLID + " because it was not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strValue;

	}

	//getTitlebar
	public String getTitleBar()
	{
		String strValue = null;
		try
		{
			strValue = driver.getTitle();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strValue;

	}

	public void switchToFrame(String frameName)
	{
		try
		{
			driver.switchTo().frame(frameName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public void switchToParentWindow(String frameName) throws Exception
	{
		try
		{
			driver.switchTo().window(frameName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}

	}

	public boolean isElementPresent(By by, String elementName) {
		try {
			driver.findElement(by);
			System.out.println(elementName + ": Present");
			return true;

		} catch(Exception e){
			Assert.fail(elementName + " Not Found ");
			return false;
		}
	}



	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true; 
		} catch(Exception e){
			Assert.fail(by +" Element not  found");
			//  Assert.fail(by +" No Data found in User Completion Report");
			return false;
		}

	}

	public void refreshWebPage()
	{
		try
		{
			driver.navigate().refresh();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	//wait for element to visible
	public boolean waitForElementToVisible(String element) throws Exception
	{
		WebElement strElement = null;
		try
		{
			strElement= new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(element)));
			if(strElement.isDisplayed() == false)
			{
				failTestcase("Waited for the TIMEOUT period but element " + strElement + " not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			strElement= new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.name(element)));
			if(strElement.isDisplayed() == false)
			{
				failTestcase("Waited for the TIMEOUT period but element " + strElement + " not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			strElement= new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(element)));
			if(strElement.isDisplayed() == false)
			{
				failTestcase("Waited for the TIMEOUT period but element " + strElement + " not found.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return strElement.isDisplayed();
	}

	//capture screenshots
	public static void CaptureScreenshot(String fileNameStart) throws Exception
	{
		String path = null;
		try {

			File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			Calendar currentDate = Calendar.getInstance();
			java.io.File currentDir = new java.io.File("");		        
			String workingDirectory = System.getProperty("user.dir");
			//get today's date without Time stamp
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date today = dateFormat.parse(dateFormat.format(new Date()));
			String todaysDate = dateFormat.format(today).replace("/", "_");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
			//Get Full Class name 
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			//Get Method name 
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			//Get Class short name 
			int firstChar = fullClassName.lastIndexOf('.') +1; 
			String className = fullClassName.substring(firstChar);
			String dateN = formatter.format(currentDate.getTime()).replace("/","_");
			String dateNow = dateN.replace(":","_");
			System.out.println("printing datenow---------"+dateNow);
			String strAppPath = currentDir.getAbsolutePath()+ "/testresult/screenshots"+"_"+methodName+"_"+todaysDate;

			String fileName = fileNameStart+"."+className+"."+methodName+"."+dateNow+ ".png"; 
			String snapShotDirectory = strAppPath;
			System.out.println("Im here-------------"+snapShotDirectory);
			File f = new File(snapShotDirectory);
			//check for snapshot having folder with today date if not then create one
			if(f.exists()){
				System.out.println("SnapshotDirectory Already Exists");
			}
			else{
				f.mkdir();
			}		        
			path = f.getAbsolutePath() + "/" + fileName ;
			System.out.println("Path------------------"+path);
			FileUtils.copyFile(source, new File(path));
			//	Reporter.log("Path of the Snapshot : " + path);
			/*//		Reporter.log("<a href='" + screenshotFile.getAbsolutePath() + "'>screenshot</a>")
			Reporter.log("<a href='" + path + "' >  path  </a>");
			Reporter.log("<a href=" + path + "> </a>"); 
			Reporter.log("<a href='"+path+".png'> </a>");*/

		}
		catch(IOException e) {
			path = "Failed to capture screenshot: " + e.getMessage();
		}
	}

	public static void highLightElement(WebDriver driver, WebElement element)
	{
		JavascriptExecutor js=(JavascriptExecutor)driver; 

		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

		try 
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) {

			System.out.println(e.getMessage());
		} 

		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white')", element); 

	}
	
	

}