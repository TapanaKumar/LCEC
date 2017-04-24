package com.lcec.automationScript.UserManger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
//import com.lcec.objectCollection.UserManagerLocater;
import com.lcec.pages.userManger.CreateUserUtili;
import com.lcec.utility.LogerData;
//import com.lcec.utility.LogerData;
import com.lcec.utility.LrnCommon;
import com.lcec.utility.PropertyHandler;
import com.relevantcodes.extentreports.LogStatus;

import atu.testng.reports.ATUReports;

public class CreatUserScript extends CreateUserUtili{
	//load loger file
	private static Logger Log = Logger.getLogger(CreatUserScript.class.getName());
	//add userManagerLocater 
	//private UserManagerLocater userManagerLocater=null;
	//in username data comes from properties file
	private String username=PropertyHandler.getProperty("USERNAME");
	//password data comes from properties file
	private String password=PropertyHandler.getProperty("PASSWORD");
	//create LRNUtili object
	//LRNUtili lRNUtili=new LRNUtili();
	//create utilimethod object.
	//private SearchManagerUtili utiliMethod=new SearchManagerUtili();
	//private CreateUserUtili createManagerUtili=new CreateUserUtili();
	
	//create weblist store
	private List<WebElement> element = null;
	//private String createNewButton="default_button";
	private LrnCommon lrnCommon=new LrnCommon();
	
	@Test(testName="LCE-1223")
	public void loginCEC_NGTest() throws IOException
	{
		try{
			
			//Search user test pass message to extentReports
			logger=report.startTest("Search User Test");
			LogerData.startTestCase("loginCEC_NGTest");		
			//call method of setAuthorInfoForReports and pass parameter tester name
			setAuthorInfoForReports("Tapana Kumar Badhai");
			ATUReports.setTestCaseReqCoverage("loginLCEC_NGTest");
			//Description of create user and search user test case.
			ATUReports.currentRunDescription = "When user log in with nagative test data ,error message should display.";
			
			
			Log.info("---------------------start test case of loginCEC_NGTest----------------");
			logger=report.startTest("login lcec useing nagative test case");
			lrnCommon.getLoginLCEC(username,"you");
			LogerData.info("login to lcec page");
			String ExceptedErrorMessage=driver.findElement(By.xpath("//font[@face='verdana']/b")).getText();
			LogerData.info("ExceptedErrorMessage: "+ExceptedErrorMessage);
			String ActualErrorMessage="Invalid username or password. Please try again. If the problem persists, contact your help desk.";
			LogerData.info("ActualErrorMessage: "+ActualErrorMessage);
			try
			{
				Assert.assertEquals(ExceptedErrorMessage, ActualErrorMessage);
				LogerData.passWithCompare("Compare Excepted error message and actual error message", null, ExceptedErrorMessage, ActualErrorMessage);
			}catch(Exception e)
			{
				getErrorMessage("login with wrong password is not pass","loginCEC_NGTest");				
			}
			LogerData.endTestCase("complete loginCEC_NGTest");
		}catch(Exception e)
		{
			getErrorMessage("login with wrong password is not pass","loginCEC_NGTest");
			//-----------------------------password method call--------------------------------------------------------
			getLoginLCEC(username,password);
		}
	}

	@Test(testName="LCE-1224",dependsOnMethods = { "loginCEC_NGTest" })
	public void loginlCEC_Test() throws IOException
	{
		driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
		//wait some seconds
		logger=report.startTest("Creat User Script Test");
		LogerData.startTestCase("loginCEC_Test");
		setAuthorInfoForReports("Tapana Kumar Badhai");
		ATUReports.setTestCaseReqCoverage("login lCEC Test");
		//Description of create user and search user test case.
		ATUReports.currentRunDescription = "When user log in with posative  test data user should log in.";
		try
		{
			//-----------------------------password method call--------------------------------------------------------
			getLoginLCEC(username,password);
			LogerData.info("Start log in in lcec.");
			//Assert.assertTrue(userManagerLocater.getLoginUserMessage().isDisplayed());
			WebElement exceptedLocater=driver.findElement(By.xpath("//a[contains(text(),'Logout')]"));
			LogerData.info("After log in verifay log in user is displaying or not");
			Assert.assertTrue(exceptedLocater.isDisplayed());
			LogerData.pass("Locater user name is present");
			LogerData.passWithCompare("Log in user test case","User name & Password",null, null);
		}
		catch(Exception e)
		{
			
			LogerData.warn("faill loginCEC");
			String errorMessage=driver.findElement(By.xpath("//font[@face='verdana']/b")).getText();
	
			LogerData.info("errorMessage message of log in should be"+errorMessage);
			LogerData.fail(e.getMessage());
			LogerData.failWithCompare("Log in is now working","User name & Password", null, null);
		}

	}

	/*@Descraption:Create a new user in the system. Validate cancel/back  buttion.
	 * 
	 * Verify button is displaying or not.
	 * */

	
	
	@Test(testName="LCE-1225",dependsOnMethods = { "loginlCEC_Test" })
	public void backButtionTest() throws IOException, InvalidFormatException
	{

		try
		{
			logger=report.startTest("Cancel & Back Button Test");
			
			
			setAuthorInfoForReports("Tapana Kumar Badhai");
			ATUReports.setTestCaseReqCoverage("Back Buttion Test");
			//Description of create user and search user test case.
			ATUReports.currentRunDescription = "When user click back button or cancel button user should come back admin tools page.";
			getCreateNewUser();
			//get data from cancel button.
			String exceptedCancelButtonText=driver.findElement(By.xpath("//span[text()='Cancel/Back']")).getText();
			//String exceptedCancelButtonText=userManagerLocater.getCancelButton().getText();
			//get data from create button text.
			String exceptedCreateButtonText=driver.findElement(By.id("default_button")).getText();
			//String exceptedCreateButtonText=userManagerLocater.getCreateNewUserButtion().getText();
			LogerData.info("exceptedButtonText:"+exceptedCancelButtonText);
			try
			{
				Assert.assertEquals("Cancel/Back", exceptedCancelButtonText);
				LogerData.passWithCompare("verifay button is displaying ", "Button name","Cancel/Back", exceptedCancelButtonText);
			}catch(Exception cancel)
			{

				getErrorMessage("Buttion of cancel/Back buttion is not present","Cancel/Back");
			}

			try
			{
				Assert.assertEquals("Create New User", exceptedCreateButtonText);
				LogerData.info("Buttion of createButton is display");

			}catch(Exception cancel)

			{

				getErrorMessage("Buttion of cancel/Back buttion is not present","Cancel/Back");
			}

			driver.findElement(By.xpath("//span[text()='Cancel/Back']")).click();
			LogerData.pass("ButtonTest is pass");
			//excelSheetHandel.setExcelStringData(1,4,4,"PASS");
		}catch(Exception e)
		{
			getErrorMessage("cancelBackButtionTest is faill","cancelBackButtionTest");
			getLogOutLCEC();
			//-----------------------------password method call--------------------------------------------------------
			getLoginLCEC(username,password);
			//excelSheetHandel.setErrorMessage(0,4,4,"Fail");	
		}
	}

	/*@Descraption:
	 * Create a new user in the system. 
	 * 1)Verify  button present or not "cancel/back and & create new button "
	 * 2)Verify user should be create new account.
	 * 
	 * 
	 * */
	@Test(testName="LCE-1226-",dataProviderClass=com.lcec.excelReader.ExcelTestDataProvider.class, dataProvider="ExcelDataProvider",dependsOnMethods = { "backButtionTest" })
	public void createNewUserTest(String UserID,String NewPassword,String FirstName,String LastName,String Language,String ReenterPassword,
			String loginName,String Password,String firstName,String lastName,String language,String verifayPassword) throws IOException, InvalidFormatException
	{
		try
		{
			logger=report.startTest("Create NewUser Test");
			LogerData.startTestCase("Create NewUser Test");
			setAuthorInfoForReports("Tapana Kumar Badhai");
			ATUReports.setTestCaseReqCoverage("Create New User Test");
			//Description of create user and search user test case.
			ATUReports.currentRunDescription = "Admin should able to create new user in LCEC";
			getCreateNewUser();
			LogerData.info("Create user method.");
			getRequriedField( UserID,NewPassword,FirstName,LastName,Language,ReenterPassword,
					loginName, Password, firstName, lastName, language, verifayPassword);
			LogerData.pass("Create NewUser Test");
			//excelSheetHandel.setExcelStringData(1,5,4,"PASS");
		}catch(Exception e)
		{
			getLogOutLCEC();
			//-----------------------------password method call--------------------------------------------------------
			getLoginLCEC(username,password);
			//excelSheetHandel.setExcelStringData(1,5,4,"PASS");
		}
		LogerData.endTestCase("Create NewUser Test");
	}

	/*@Descraption:Create a new user in the system. Validate field:
	 *1)user id,new password,first name,re-enter password ,language,
	 *  
	 * */

	@Test(testName="LCE-1227",dependsOnMethods = { "createNewUserTest" })
	public void requiredFieldTest() throws IOException
	{
		try
		{
			logger=report.startTest("Required Field Test");
			setAuthorInfoForReports("Tapana Kumar Badhai");
			ATUReports.setTestCaseReqCoverage("Required Field Test");
			//Description of create user and search user test case.
			ATUReports.currentRunDescription = "Admin create user ,if admin not eddit requried field ,error message should be display.";
			//call create new user method
			LogerData.startTestCase("Required Field Test");
			getCreateNewUser();
			LogerData.info("call method new user");
			driver.findElement(By.id("default_button")).click();
			LogerData.info("click createNew user button");
			
			LogerData.info("click createNew user button");
			//verify all required field error.------------------------------------------------
			element=driver.findElements(By.xpath("//li/font[@color='red']"));
			Log.info("size of element"+element.size());
			for(int l=1;l<=element.size();l++)
			{
				try
				{
					String errorMessage=driver.findElement(By.xpath("//li["+l+"]/font[@color='red']")).getText();

					LogerData.info("errorMessage is:"+errorMessage);

					if(errorMessage.equalsIgnoreCase("The field 'Password' is required."))
					{
						try
						{
							Assert.assertEquals(errorMessage,"The field 'Password' is required.");

							LogerData.pass("The field 'Password' is required. text is present");

						}catch(Exception e)
						{
							LogerData.fail("The field 'Password' is required. that text message is not displaying");
						}
					}
					else if(errorMessage.equalsIgnoreCase("The field 'First Name' is required."))
					{

						try
						{
							Assert.assertEquals(errorMessage,"The field 'First Name' is required.");

							LogerData.pass("The field 'First Name' is required.. text is present");

						
						}catch(Exception e)
						{
							LogerData.fail("The field 'First Name' is required. is not displaying");

						}

					}
					else if(errorMessage.equalsIgnoreCase("The field 'Last Name' is required."))
					{
						try
						{
							Assert.assertEquals(errorMessage,"The field 'Last Name' is required.");

							LogerData.pass("The field 'Last Name' is required.. text is present");

						}catch(Exception e)
						{
							LogerData.fail("The field 'Last Name' is required. is not displaying");
						}
					}
					else if(errorMessage.equalsIgnoreCase("The field 'User ID' is required."))
					{
						try
						{
							Assert.assertEquals(errorMessage,"The field 'User ID' is required.");

							LogerData.pass("The field 'User ID' is required. text is present");

						}catch(Exception e)
						{
							LogerData.fail("The field 'User ID' is required. is not displaying");

						}
					}

					else if(errorMessage.equalsIgnoreCase("The field 'Language' is required."))
					{
						try
						{
							Assert.assertEquals(errorMessage,"The field 'Language' is required.");

							LogerData.pass("The field 'Language' is required.. text is present");

						}catch(Exception e)
						{
							LogerData.fail("The field 'Language' is required.. is not displaying");
						}
					}

					else if(errorMessage.equalsIgnoreCase("Your new password is the same as the old one."))
					{
						try
						{
							Assert.assertEquals(errorMessage,"Your new password is the same as the old one.");

							LogerData.pass("Your new password is the same as the old one.. text is present");

						}catch(Exception e)
						{
							LogerData.fail("Your new password is the same as the old one. is not displaying");
						}
					}
				}catch(Exception e)
				{
					getErrorMessage("requiredFieldTest","requiredFieldTest");
				}

			}

			//click cancel button
			driver.findElement(By.xpath("//span[text()='Cancel/Back']")).click();
			//userManagerLocater.getCancelButton().click();
			LogerData.info("click cancel button");
			LogerData.pass("requiredFieldTest is pass");
			LogerData.error("Required Field Test");
		}
		catch(Exception e)
		{
			getErrorMessage("exception requiredFieldTest ","requiredFieldTest");

			getLogOutLCEC();
			//-----------------------------password method call--------------------------------------------------------
			getLoginLCEC(username,password);
		}
	}


}
