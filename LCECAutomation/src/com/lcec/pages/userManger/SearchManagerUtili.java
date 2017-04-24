package com.lcec.pages.userManger;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.lcec.excelReader.ExcelSheetHandel;
//import com.lcec.objectCollection.UserManagerLocater;
import com.lcec.utility.BrowserMethods;
import com.lcec.utility.LogerData;
import com.lcec.utility.LrnCommon;
import com.relevantcodes.extentreports.LogStatus;

public class SearchManagerUtili extends BrowserMethods
{
	
	private List<WebElement> element = null;
	private ExcelSheetHandel excelSheetHandel=new ExcelSheetHandel();
	private static Logger Log = Logger.getLogger(CreateUserUtili.class.getName());
	CreateUserUtili createManagerUtili=new CreateUserUtili();
	LrnCommon  lrnCommon=new LrnCommon();
	//------------------------------------------------search user using user id------------------------------
	/*@Descraption :search user using user id
	 * 
	 * 
	 * */
	public void getUser(String droupDownManu,String UserID) throws IOException
	{
		try
		{
			//Click search link lCEC.
			driver.findElement(By.xpath("//a[text()='Search Tool']")).click();
			LogerData.info("Click search link lCEC");
			//store web element of drop down.
			WebElement droupDownElement=driver.findElement(By.xpath("//select[@name='column']"));
			LogerData.info("store web element of drop down");
			//call method of drop down of class LRNUtili
			getDroupDown(droupDownElement,droupDownManu);
			//get search box edit box and send search user id
			driver.findElement(By.xpath("//input[@name='search']")).sendKeys(UserID);
			LogerData.info("get search box edit box and send search user id");
			//click search button
			driver.findElement(By.xpath("//input[@class='button']")).click();
			LogerData.info("click search button");
			element=driver.findElements(By.xpath("//tr/td[1][@class='tabledList']/a"));
			for(int j=1;j<=element.size();j++)
			{
				try
				{
					//store list of locater in actual user id
					WebElement actualUserIDLocater=driver.findElement(By.xpath("//tr["+j+"]/td[1][@class='tabledList']/a"));
					LogerData.info("store list of locater in actual user id");
					String actualUserID=actualUserIDLocater.getText();
					if(UserID.equalsIgnoreCase(actualUserID))
					{
						//select that user 
						actualUserIDLocater.click();
						try
						{
							LogerData.passWithCompare("Droup Down list","User id select",actualUserID,UserID );
							Assert.assertTrue(driver.findElement(By.xpath("//td[contains(text(),'"+actualUserID+"')]")).isDisplayed());
							logger.log(LogStatus.PASS, "Search user is Displaying  and Select Search User.");
						}catch(Exception e)
						{
							getErrorMessage("search user not displaying  ","searchUser");
							LogerData.failWithCompare("Droup Down list","User id select",actualUserID,UserID );
						}
						break;	
					}
					else
					{
						//search user is not present.
						logger.log(LogStatus.WARNING, "search user is not present");
						LogerData.warn("search user is not present");
					}
				}
				catch(Exception e)
				{
					
					LogerData.warn("exception in searching user");
				}
			}

		}
		catch(Exception e)
		{

			getErrorMessage("error in search user !","searchuser");
		}

	}

	//------------------------------------------list of permission-----------------------------------
	public void getListOfPermisation() throws InvalidFormatException, IOException
	{
		try
		{
			LogerData.info("start method getListOfPermisation");
			element=driver.findElements(By.xpath("//td[@id='permissionGroups']/table/tbody/tr/td[1]"));
			LogerData.info("element size"+"element.size()");
			//------------loop for verify  permission 
			LogerData.info("------------loop for verify  permission ");
			for(int m=1;m<=element.size();m++)
			{
				//String PermmisionGroupMessage=driver.findElement(By.xpath("//td[@id='permissionGroups']/table/tbody/tr["+m+"]/td[1]")).getText();
				//Log.info("PermmisionGroupMessage :"+PermmisionGroupMessage);
				//select one of permissions group
				WebElement checkButtonLocater=driver.findElement(By.xpath("//td[@id='permissionGroups']/table/tbody/tr["+m+"]/td/input"));
				if(checkButtonLocater.isSelected())
				{
					LogerData.info("All Rady Selected");
				}else
				{
					checkButtonLocater.click();
					LogerData.info("select permissions icon");
				}
			}

			
			
			//Permissions message
			LogerData.info("Geting Permissions message loop start ");
			element=driver.findElements(By.xpath("//div[@id='permissions']/table/tbody/tr/td[1]"));
			for(int n=1;n<=element.size();n++)
			{
				LogerData.info(element.size()+"No of message are displaying " );
				//excelSheetHandel.setExcelStringData(3,m,1,PermmisionGroupMessage,"InPutDataFile");
				String PermmisionMessage=driver.findElement(By.xpath("//div[@id='permissions']/table/tbody/tr["+n+"]/td[1]")).getText();				
				LogerData.info("Permissions message is------- :"+PermmisionMessage);
				driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);	
				//excelSheetHandel.setExcelStringData(4,9+n,1,PermmisionMessage,"InPutTestData");
				try
				{
					String PermmisionMessagSube=driver.findElement(By.xpath("//div[@id='permissions']/table/tbody/tr["+n+"]/td[2]")).getText();
					LogerData.info("PermmisionMessagSube message is------- :"+PermmisionMessagSube);
				//	excelSheetHandel.setExcelStringData(4,9+n,1,PermmisionMessagSube,"InPutTestData");

				}catch(Exception e)
				{
					LogerData.info("Locater not display exception");
				}
				//excelSheetHandel.setExcelStringData(3,n,2,PermmisionMessage,"InPutDataFile");
			}

		}
		catch(Exception e)
		{
			getErrorMessage("error in permmission message print","getListOfPermisation");
		}

	}
	public void getRestPassword(String droupDownManu,String UserID,String Editpassword) throws IOException
	{
		//locater of reset password dropDown manu.
		String dropDownLocater="//select[@name='action']";
		String cancelButton="//button[span[text()='Cancel/Back']]";
		String SearchTools="//a[text()='Search Tool']";
		String UserSelect="//select[@name='column']";
		String searchInput="//input[@name='search']";
		//edit password locater
		String EditpasswordLocater="//input[@name='EDIT_Password']";
		//submit password button.
		String SubmitPasswordButtonLocater="default_button";
		//locater after log in displaying=
		String logAfterDisplay="//font[text()='Please use the form below to create a new password.']";

		try
		{

			//call method of admin tools
			createManagerUtili.getCreateNewUser();
			LogerData.info("call method of admin tools");
			//click cancel button 
			driver.findElement(By.xpath(cancelButton)).click();
			LogerData.info("User click cancel button");
			//click search tools button.
			//driver.findElement(By.xpath(SearchTools)).click();
			getUser(droupDownManu,UserID);
			//Drop down locater
			WebElement locaterDroupdown=driver.findElement(By.xpath(dropDownLocater));

			getDroupDown(locaterDroupdown,"Reset Password");
			LogerData.info("User select rest password ");
			driver.findElement(By.xpath(EditpasswordLocater)).sendKeys(Editpassword);
			LogerData.info("Send value of eddit password edit box");
			driver.findElement(By.id(SubmitPasswordButtonLocater)).click();
			LogerData.info("click password create buttion");
			//call logout page.
			createManagerUtili.getLogOutLCEC();
			LogerData.info("log out lcec");
			//call login method.
			createManagerUtili.getLoginLCEC(UserID, Editpassword);
			LogerData.info("log in lcec");
			//Verify complete
			String ActualUserMessage=driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).getText();
			LogerData.info("Actual User Message is "+ActualUserMessage);
			//lrnCommon.verifayTextAdmin("Admin tool button is not displaying even admin permission  given ");
			try
			{
				
				Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Admin Tools']")).isDisplayed());
				LogerData.pass("reset password is working");
			}catch(Exception e)
			{
				LogerData.fail("reset password is not working");
			}
		}catch(Exception e)
		{
			LogerData.warn("Exception on RestPassword");
			LogerData.warn(e.getMessage());
		}
	}
};
