package com.lcec.automationScript.UserManger;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.lcec.pages.userManger.CreateUserUtili;
import com.lcec.pages.userManger.SearchManagerUtili;
import com.lcec.utility.LogerData;
import com.lcec.utility.LrnCommon;
import com.lcec.utility.PropertyHandler;
import atu.testng.reports.ATUReports;
/*@Descraption:in this class 3 test i have write.1)SearchUserScript:2)allPermisionTest,3)getRestPasswordTest
 * 1)SearchUserScript is doing search user and verify. 
 * 2)allPermisionTest :in this class verify permission test.
 * 3)getRestPasswordTest:in this method resetPassword test.
 * 
 * */
public class SearchUserScript extends SearchManagerUtili
{

	//load loger file
	private static Logger Log = Logger.getLogger(SearchUserScript.class.getName());
	//add userManagerLocater 
	//in username data comes from properties file
	private String username=PropertyHandler.getProperty("USERNAME");
	//password data comes from properties file
	private String password=PropertyHandler.getProperty("PASSWORD");
	//create utilimethod object.
	//private SearchManagerUtili searchManagerUtili=new SearchManagerUtili();
	private CreateUserUtili createManagerUtili=new CreateUserUtili();
	private String CancelButtonLocater="//span[text()='Cancel/Back']";
	private String SaveButton="//input[@value='Save']";

	private List<WebElement> element = null;
	private LrnCommon lrnCommon=new LrnCommon();
	String PermmisionMessageData=null;


	@Test(testName="LCE-1228",dataProviderClass=com.lcec.excelReader.ExcelTestDataProvider.class, dataProvider="ExcelDataProvider")
	public void searchUserTest(String searchUserKey,String UserID) throws IOException
	{ 	//Search user test pass message to extentReports
		logger=report.startTest("Search User Test");
		//call method of start test case.
		LogerData.startTestCase("SearchUserTest");		
		try
		{
			//call method of setAuthorInfoForReports and pass parameter tester name
			setAuthorInfoForReports("Tapana Kumar Badhai");
			//pass test case name 
			ATUReports.setTestCaseReqCoverage("searchUserTest");
			//Description of create user and search user test case.
			ATUReports.currentRunDescription = "Create User And Search User Test Case Run Report";
	
			//wait for page load 60 seconds.
			driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
			LogerData.info("wait for page load 60 seconds");
			
			
			//-----------------------------password method call--------------------------------------------------------
			//lrnCommon.getLoginLCEC(username, password);
			//LogerData.info("login to lcec success");
			try
			{
				//call the method get create user..
				createManagerUtili.getCreateNewUser();
				LogerData.info("user create in LCEC");
				//click cancel button.
				driver.findElement(By.xpath(CancelButtonLocater)).click();
				LogerData.info("User click cancel button");
				//click search tool button.
				LogerData.info("User send search key data is "+searchUserKey);
				LogerData.info("User send searching user id is "+UserID);
				LogerData.info("-------search user start------");
				getUser(searchUserKey,UserID);
				LogerData.info("select drop menu and select userID .");
			}catch(Exception e)
			{
				LogerData.warn(e.getMessage());
				getErrorMessage("Exception on searching user","search user");
			}
			LogerData.pass("Search User Test Method is working");
		}
		catch(Exception e)
		{
			getErrorMessage("SearchUserTest is fail","searchUserTest");
			LogerData.warn(e.getMessage());
			lrnCommon.getLogOutLCEC();
			//-----------------------------password method call--------------------------------------------------------
			lrnCommon.getLoginLCEC(username,password);
		}
		LogerData.endTestCase("Search User Test Case");
	}

	/*@Descraption:After Search User >add user Grant Administrator Permissions and validate  all permission
	 * 
	 * */
	@Test(testName="LCE-1229",dataProviderClass=com.lcec.excelReader.ExcelTestDataProvider.class, dataProvider="ExcelDataProvider",dependsOnMethods = { "searchUserTest" })
	public void allPermissionTest(String exceptedPermissionMessage) throws InvalidFormatException, IOException
	{
		try
		{ 
			setAuthorInfoForReports("Tapana Kumar Badhai");
			//select  Grant Administrator Permissions
			logger=report.startTest("Permision Text Message Test");
			LogerData.startTestCase("All Permission Test case start");	
			LogerData.info("grantAdministratorPermissionsTest is starting.");
			WebElement userPermissionLocator =driver.findElement(By.xpath("//select[@name='action']"));
			getDroupDown(userPermissionLocator,"Grant Administrator Permissions");
			LogerData.info("select drop down menu.select Grant Administrator Permissions ");
			getWaitForElementPresent("//span[contains(text(),'Permission Groups')]",100);
			//call list of Permissions method
			getListOfPermisation();
			LogerData.info("list of permisation method ");
			//user click save button.
			driver.findElement(By.xpath(SaveButton)).click();
			LogerData.info("User click save button of permisation page.");
			getWaitForElementPresent("//span[text()='Administrator Tool Permissions']",100);
			LogerData.info("List Of Permisation AfterSave");
			element=driver.findElements(By.xpath("//div[@id='permViewPane']/table/tbody/tr/td[1]"));
			LogerData.info("After save list of message is "+element.size());
			for(int n=1;n<=element.size();n++)
			{
				//------------------------------------locater of permissions text.------------------------------------------
				String PermmisionMessage=driver.findElement(By.xpath("//div[@id='permViewPane']/table/tbody/tr["+n+"]/td[1]")).getText();
				try
				{
					PermmisionMessageData=driver.findElement(By.xpath("//div[@id='permViewPane']/table/tbody/tr["+n+"]/td[2]")).getText();
				}catch(Exception e)
				{
					LogerData.info("----------------------------------");
				}
				//--------------------------spilt data-----------------------------
				String string = exceptedPermissionMessage;
				String[] parts = string.split(",");
				Log.info("length of split :"+string.split(",").length);
				for(int k=0;k<string.split(",").length;k++)
				{
					try{
						String realMessage= parts[k];
						LogerData.info("realMessage:"+realMessage);
						if(PermmisionMessage.equalsIgnoreCase(realMessage))
						{
							LogerData.info("message is PermmisionMessage:"+PermmisionMessage);
							LogerData.info("excepted Message: "+realMessage);
							LogerData.passWithCompare("permission message is same ",null,realMessage,PermmisionMessage);
							break;
						}else if(PermmisionMessageData.equalsIgnoreCase(realMessage))
						{
							LogerData.info("message is PermmisionMessage:"+PermmisionMessageData);
							LogerData.info("excepted Message: "+realMessage);
							LogerData.passWithCompare("permission message is same ",null,realMessage,PermmisionMessageData);
							break;
						}
						else
						{
							LogerData.info("---------------------------------------------");
						}

					}catch(Exception e)
					{
						LogerData.failWithCompare(e.getMessage(),null,PermmisionMessage, PermmisionMessageData);
						LogerData.fail("PermmisionMessage:"+PermmisionMessage);
						LogerData.fail("PermmisionMessageData:"+PermmisionMessageData);
					}
				}
			}
			LogerData.pass("All Permission Test case is pass");
		}
		catch(Exception e)
		{
			getErrorMessage("All Permission Test case is fail","allPermissionTest");

			LogerData.fail("All Permission Test");
			LogerData.fail(e.getMessage());
			lrnCommon.getLogOutLCEC();
			//-----------------------------password method call--------------------------------------------------------
			lrnCommon.getLoginLCEC(username,password);
		}
		LogerData.endTestCase("All Permission Test");
	}
	@Test(testName="LCE-1230",dataProviderClass=com.lcec.excelReader.ExcelTestDataProvider.class, dataProvider="ExcelDataProvider",dependsOnMethods = { "allPermissionTest" })
	public void getRestPasswordTest(String droupDownManu,String UserID,String Editpassword) throws IOException
	{
		try
		{
			logger=report.startTest("Rest Password Test");
			LogerData.startTestCase("Rest Password Test");
			setAuthorInfoForReports("Tapana Kumar Badhai");
			ATUReports.setTestCaseReqCoverage("Rest Password Test");
			//Description of create user and search user test case.
			ATUReports.currentRunDescription = "Rest password with new password";
			//Search user test pass message to extentReports
			logger=report.startTest("Rest Password Test");

			LogerData.info("droupDownManu:"+droupDownManu);

			LogerData.info("User user id is :"+UserID);

			LogerData.info("User password is :"+Editpassword);

			getRestPassword(droupDownManu, UserID, Editpassword);

			LogerData.endTestCase("Rest Password Test");
			LogerData.pass("Rest Password Test");
		}catch(Exception e)
		{
			getErrorMessage("Rest Password Test case  is fail","getRestPasswordTest");

			LogerData.fail("All Permission Test");
			LogerData.error(e.getMessage());
			lrnCommon.getLogOutLCEC();
			//-----------------------------password method call--------------------------------------------------------
			lrnCommon.getLoginLCEC(username,password);
		}
	}


}
