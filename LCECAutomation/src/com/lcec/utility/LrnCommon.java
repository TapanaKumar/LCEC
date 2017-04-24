package com.lcec.utility;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

public class LrnCommon extends BrowserMethods {
	private String userIDlocater="User";
	//user password locater 
	private String passwordLocater="Password";
	//user login button  locater
	private String loginLocater="SignOn";
	//user after log in locater for validation 
	private String verifyLocater="//a[contains(text(),'Logout')]";
	public  void getLoginLCEC(String username,String password) throws IOException
	{
		try
		{
			//UserManagerLocater	userManagerLocater =loadObject(new UserManagerLocater());
			driver.findElement(By.id(userIDlocater)).clear();
			driver.findElement(By.id(userIDlocater)).sendKeys(username);
			//userManagerLocater.getUserName().sendKeys(username);
			logger.log(LogStatus.INFO, "send user name in to user name text box");
			LogerData.info("user name is :"+username);
			driver.findElement(By.id(passwordLocater)).clear();
			driver.findElement(By.id(passwordLocater)).sendKeys(password);
			//userManagerLocater.getPassword().sendKeys(password);
			logger.log(LogStatus.INFO, "send password to password text box");
			LogerData.info("password name is :"+password);
			driver.findElement(By.name(loginLocater)).click();
			logger.log(LogStatus.INFO, "log in browser successfully");
			
			//userManagerLocater.getSignOnButtion().click();
			//WebElement locaterValidation=driver.findElement(By.xpath(verifyLocater));
			//Assert.assertTrue(locaterValidation.isDisplayed());
			//Assert.assertTrue(userManagerLocater.getLoginUserMessage().isDisplayed());
		
			//logger.log(LogStatus.INFO, "click logout button");
			logger.log(LogStatus.PASS, "Locater user name is displaying");
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
			logger.log(LogStatus.INFO, "-----------log out browser--------");
		}
		
		public void verifayTextAdmin(String Message) throws IOException
		{
			try
			{
			WebElement locater=driver.findElement(By.xpath("//a[text()='Admin Tools']"));
			Assert.assertTrue(locater.isDisplayed());
			LogerData.passWithCompare("Actual User Message is",null,"Admin tool button is not displaying even admin permission  given ",Message );
			}catch(Exception e)
			{
				getErrorMessage(Message,"adminTools");
			}
		}
}
