package com.lcec.utility;

import org.apache.log4j.Logger;

import com.relevantcodes.extentreports.LogStatus;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;



public class LogerData extends WebAppCommon {

	// Initialize Log4j logs 
	private static Logger Log = Logger.getLogger(LogerData.class.getName());// 
	//This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite 
	public static void startTestCase(String sTestCaseName)
	{    
		Log.info("***********************************************************************"); 
		Log.info("$$$$$$$$$$$$$$$$$$$$$		"+sTestCaseName+ "		$$$$$$$$$$$$$$"); 
		Log.info("************************************************************************"); 
		logger.log(LogStatus.INFO, "***********************************************************************");
		logger.log(LogStatus.INFO, "$$$$$$$$$$$$$$$$$$$$$		"+sTestCaseName+ "		$$$$$$$$$$$$$$");
		logger.log(LogStatus.INFO, "***********************************************************************");
	
	}
	//This is to print log for the ending of the test case 
	public static void endTestCase(String sTestCaseName)
	{ 
		Log.info("XXXXXXXXXXXXXXXXXXXXXXX    "+"-E---N---D-"+"   XXXXXXXXXX"); 
		Log.info("X"); 
		Log.info("X"); 
		logger.log(LogStatus.INFO, "XXXXXXXXXXXXXXXXXXXXXXX    "+"-E---N---D-"+"   XXXXXXXXXX");
		logger.log(LogStatus.INFO, "X");
		logger.log(LogStatus.INFO, "X");
	}
	// Need to create these methods, so that they can be called  
	public static void info(String message) 
	{ 
		Log.info(message);
		logger.log(LogStatus.INFO, message);
		ATUReports.add(message,LogAs.INFO,null);
	}
	public static void warn(String message) 
	{
		Log.warn(message);
		logger.log(LogStatus.WARNING, message);
		ATUReports.add("INfo Step", LogAs.WARNING, new CaptureScreen(ScreenshotOf.DESKTOP));
	}
	public static void error(String message) 
	{ 
		Log.error(message);
		logger.log(LogStatus.ERROR, message);
	}
	public static void fatal(String message) 
	{ 
		Log.fatal(message);
		logger.log(LogStatus.FATAL, message);
	}
	public static void debug(String message) 
	{ 
		Log.debug(message);
	} 

	public static void fail(String message)
	{
		logger.log(LogStatus.FAIL, message);
		ATUReports.add(message, LogAs.FAILED, new CaptureScreen(ScreenshotOf.DESKTOP));
	}
	
	public static void failWithCompare(String message,String input,String exceptedValue,String actualValue)
	{
		ATUReports.add(message,input,exceptedValue, actualValue, LogAs.FAILED,  new CaptureScreen(ScreenshotOf.DESKTOP));
		fail(message);
		
	}
	
	public static void pass(String message)
	{
		logger.log(LogStatus.PASS, message);
		ATUReports.add(message,LogAs.PASSED,null);
	}
	public static void passWithCompare(String message,String input,String exceptedValue,String actualValue)
	{
		ATUReports.add(message,input,exceptedValue, actualValue, LogAs.PASSED,null);
		pass(message);
	}
}
