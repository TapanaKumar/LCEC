package com.lcec.pages.userManger;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.lcec.excelReader.ExcelSheetHandel;
import com.lcec.utility.BrowserMethods;
import com.lcec.utility.LogerData;
import com.relevantcodes.extentreports.LogStatus;

/*@Authore;Tapana Kumar Badhai
 * 
 * 
 * */
public class CreateUserUtili extends BrowserMethods
{
	private static Logger Log = Logger.getLogger(CreateUserUtili.class.getName());
	private List<WebElement> element = null;
	private ExcelSheetHandel excelSheetHandel=new ExcelSheetHandel();
	//user id edit locater....
	private String userIDlocater="User";
	//user password locater 
	private String passwordLocater="Password";
	//user login button  locater
	private String loginLocater="SignOn";
	//user after log in locater for validation 
	private String verifyLocater="//a[contains(text(),'Logout')]";
	//-------------------------------login method-----------------------------------------
	public  void getLoginLCEC(String username,String password) throws IOException
	{
		try
		{
			//UserManagerLocater	userManagerLocater =loadObject(new UserManagerLocater());
			driver.findElement(By.id(userIDlocater)).clear();
			driver.findElement(By.id(userIDlocater)).sendKeys(username);
			//userManagerLocater.getUserName().sendKeys(username);
			LogerData.info("send user name in to user name text box");
			LogerData.info("user name is :"+username);
			driver.findElement(By.id(passwordLocater)).clear();
			driver.findElement(By.id(passwordLocater)).sendKeys(password);
			//userManagerLocater.getPassword().sendKeys(password);
			LogerData.info("send password to password text box");
			LogerData.info("password name is :"+password);
			driver.findElement(By.name(loginLocater)).click();
			LogerData.info("log in browser successfully");
			WebElement locaterValidation=driver.findElement(By.xpath(verifyLocater));
			Assert.assertTrue(locaterValidation.isDisplayed());
			LogerData.pass("Locater user name is displaying");
		}
		catch(Exception e)
		{
			getErrorMessage("error in login","getLoginLCEC");
		}
	}

	//--------------------------------------log out method--------------------------------------
	public void getLogOutLCEC()
	{	
		String logoutLocater="//a[contains(text(),'Logout')]";
		//logger.log(LogStatus.INFO, "locater user who is login is displaying");
		//UserManagerLocater	userManagerLocater =loadObject(new UserManagerLocater());
		driver.findElement(By.xpath(logoutLocater)).click();
		//userManagerLocater.getLoginUserMessage().click();
		LogerData.info("logout buttion click");
	}

	//----------------------------------------create user method------------------------------------
	public void getCreateNewUser() throws IOException
	{ 

		String adminToolsLocater="//a[text()='Admin Tools']";
		String managerLocater="//div[contains(text(),'User Manager')]";
		String newUserLink="//a[text()='New User']";
		try
		{
			LogerData.info("------Create new user  Start-------");
			//UserManagerLocater	userManagerLocater =loadObject(new UserManagerLocater());
			Thread.sleep(3000);
			LogerData.info("Admin tools buttion is displaying");
			//admin tool locater to click 
			driver.findElement(By.xpath(adminToolsLocater)).click();
			//userManagerLocater.getAdminTools().click();
			LogerData.info("Click Admin tools buttion");
			Thread.sleep(3000);
			//click manager link
			driver.findElement(By.xpath(managerLocater)).click();
			//userManagerLocater.getUserManagerModule().click();
			LogerData.info("Click Manager Module");
			//------------------click edit user link and add all required field.....................// 
			Thread.sleep(3000);	
			driver.findElement(By.xpath(newUserLink)).click();
			//userManagerLocater.getNewUserLink().click();
			LogerData.info("Click New User Link");
		}catch(Exception e)
		{
			getErrorMessage("error in new user link ","getCreateNewUser");
		}
	}

	//-----------------------------------------------edit field-------------------------------------
	public void edditField(String edditTextBox,String actualEditBox,String SendData,WebElement edditText)
	{
		if(edditTextBox.equalsIgnoreCase(actualEditBox))
		{
			edditText.sendKeys(SendData);
		}
	}
	/*@Descraption:in this method restPassword 
	 * 
	 * 
	 * */
	public void getRestPassword(String username,String CurrentPassword,String NewPassword,String ReenterNewPassword) throws InvalidFormatException, IOException
	{


		try
		{
			//UserManagerLocater	userManagerLocater =loadObject(new UserManagerLocater());
			//click user profile button.
			driver.findElement(By.xpath("//a[text()='My Profile']")).click();
			//userManagerLocater.getMyProfile().click();
			LogerData.info("click user profile button.");
			driver.findElement(By.xpath("//a[text()='My Profile']")).click();
			//click set password button.
			//userManagerLocater.getSetPassword().click();
			LogerData.info("click set password button.");
			//String CurrentPassword=restPasswordBeanList.get(i).getCurrentPassword();
			LogerData.info("CurrentPassword:"+CurrentPassword);
			//String NewPassword=restPasswordBeanList.get(i).getNewPassword();
			LogerData.info("NewPassword:"+NewPassword);
			//String ReenterNewPassword=restPasswordBeanList.get(i).getReenterNewPassword();
			LogerData.info("ReenterNewPassword:"+ReenterNewPassword);
			//--------------------------------send all edit data to reset password.
			driver.findElement(By.id("old_pw")).sendKeys(CurrentPassword);
			//userManagerLocater.getCurrentPassword().sendKeys(CurrentPassword);
			LogerData.info("send current password data to current password edit box");
			driver.findElement(By.id("passwd_1")).sendKeys(NewPassword);
			//userManagerLocater.getNewPassword().sendKeys(NewPassword);
			LogerData.info("send new password data to new password edit  box");
			driver.findElement(By.id("passwd_2")).sendKeys(ReenterNewPassword);
			//userManagerLocater.getReEnetrPassword().sendKeys(ReenterNewPassword);
			LogerData.info("send reenter password to reenter edit box");
			//click change password button
			driver.findElement(By.xpath("//input[@value='Change Password']")).click();
			//userManagerLocater.getChangePasswordButton().click();
			LogerData.info("click change password button");
			//-----------------------verify password change or not.
			getLogOutLCEC();
			LogerData.info("log out lcec");
			LogerData.info("log in new password");
			//excelSheetHandel.setExcelStringData(4,i+1,0,"_"+ReenterNewPassword,"InPutDataFile");
			try
			{

				getLoginLCEC(username, ReenterNewPassword);

			}catch(Exception e)
			{
				LogerData.warn(e.getMessage());
				//	excelSheetHandel.setExcelStringData(4,i+1,3,"resetPasswordFail","OutPutDataFile");
				getErrorMessage("resetPasswordFail","resetPasswordFail");
			}
		}catch(Exception e)
		{
			LogerData.warn(e.getMessage());
			logger.log(LogStatus.ERROR, "exception in reset password loop.");
		}
	}
	//----------------------------------------------------Edit page all field----------------------
	public void getRequriedField(String UserID,String NewPassword,String FirstName,String LastName,String Language,String ReenterPassword,
			String loginName,String Password,String firstName,String lastName,String language,String verifayPassword) throws IOException
	{
		try{

			LogerData.info("all the requried field should eddit");	
			//UserManagerLocater	userManagerLocater =loadObject(new UserManagerLocater());
			LogerData.info("get the data from excel sheet");	
			
			LogerData.info("user id value :"+UserID);
			
			LogerData.info("newPassword value is :"+NewPassword);

			LogerData.info("FirstName value is :"+FirstName);
		
			LogerData.info("LastName value is :"+LastName);
	
			LogerData.info("Language value is :"+Language);
			
			LogerData.info( "ReenterPassword value is :"+ReenterPassword);
		
			getWaitForElementPresent("//tr[td[2]]/descendant::input[1]",30);

			//logger.log(LogStatus.INFO, "wait all count total no of eddit field");

			element =driver.findElements(By.xpath("//tr[td[2]]/descendant::input[1]"));

			//logger.log(LogStatus.INFO, "total no of eddit field in left side");

			for(int locater=2;locater<=element.size();locater++)
			{
				try
				{	
					WebElement edditElement=driver.findElement(By.xpath("//tr["+locater+"][td[2]]/descendant::input[1]"));

					String edditTextBox=edditElement.getAttribute("name");

					//logger.log(LogStatus.INFO, "edditTextBox:"+edditTextBox);

					edditField(edditTextBox,loginName,UserID,edditElement);

					//logger.log(LogStatus.INFO,"send user id data to user id eddit box");

					edditField(edditTextBox,Password,NewPassword,edditElement);

					//logger.log(LogStatus.INFO,"send newpassword data to new password eddit box");

					edditField(edditTextBox,firstName,FirstName,edditElement);

					//logger.log(LogStatus.INFO,"send firstname data to first name eddit message");

					edditField(edditTextBox,lastName,LastName,edditElement);
					LogerData.info("send last name data to last name eddit box");
				}
				catch(Exception e)
				{

				}
			}

			element =driver.findElements(By.xpath("//tr[td[2]]/descendant::input[2]"));

			for(int locater=2;locater<=element.size();locater++)
			{
				try
				{
					WebElement edditElement=driver.findElement(By.xpath("//tr["+locater+"][td[2]]/descendant::input[2]"));
					String edditTextBox=edditElement.getAttribute("name");
					//edditField(edditTextBox,userProfileAccountInfoDataList.get(0).getUserID(),UserID,edditElement);
					//edditField(edditTextBox,userProfileAccountInfoDataList.get(0).getNewPassword(),NewPassword,edditElement);
					//edditField(edditTextBox,userProfileAccountInfoDataList.get(0).getFirstName(),FirstName,edditElement);
					//edditField(edditTextBox,userProfileAccountInfoDataList.get(0).getLastName(),LastName,edditElement);
					//edditField(edditTextBox,userProfileAccountInfoDataList.get(0).getReenterPassword(),ReenterPassword,edditElement);
					edditField(edditTextBox,verifayPassword,ReenterPassword,edditElement);
				}catch(Exception e)
				{

				}
			}
			//droup down language
			WebElement droupDown=driver.findElement(By.xpath("//select[@name='EDIT_Language']"));
			getDroupDown(droupDown,"English");
			driver.findElement(By.id("default_button")).click();
			//userManagerLocater.getCreateNewUserButtion().click();
			WebElement sucessMessage=driver.findElement(By.xpath("//li[text()[normalize-space() = 'User created successfully.']]"));
			getWaitForElementPresent("//li[text()[normalize-space() = 'User created successfully.']]",30);
			Assert.assertTrue(sucessMessage.isDisplayed());
			//Assert.assertTrue(userManagerLocater.getUserSuccessMessage().isDisplayed());
			LogerData.pass("user create test case is pass");
			driver.findElement(By.id("default_button")).click();
			//userManagerLocater.getCreateNewUserButtion().click();
			//logger.log(LogStatus.PASS, "click new user create buttion");
			//excelSheetHandel.setExcelStringData(2,1,1,UserID,"InPutDataFile");
		}
		catch(Exception e)
		{
			getErrorMessage("error in create user in lcec  ","lcecCreateUser");
		}
	}

	
	
}
