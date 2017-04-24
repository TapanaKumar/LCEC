package com.lcec.excelReader;

import java.io.File;
import java.lang.reflect.Method;

import jxl.*;

import org.apache.log4j.Logger;

import org.testng.annotations.*;

import com.lcec.utility.BrowserMethods;

public class ExcelTestDataProvider extends BrowserMethods
{
	private static Logger Log = Logger.getLogger(ExcelTestDataProvider.class.getName());
	
	
	@DataProvider(name = "ExcelDataProvider")
	public static Object[][] getDatafromExcel(Method testMethod) throws Exception
	{
		String xlFileName = null;
		String sheetName = null;
		String tableName = null;
		String dataSheetPath = null;
	    xlFileName = "TestData.xls";
		sheetName = testMethod.getDeclaringClass().getSimpleName();
	    tableName = testMethod.getName();
	   
	    dataSheetPath="testdata\\TestData.xls";
	    

		Object[][] retObjArr=getTableArray(dataSheetPath, sheetName, tableName);
	    return(retObjArr);
	}
	
	/*
	@DataProvider(name = "ExcelDataProviderLogin")
	public static Object[][] getDatafromExcel() throws Exception
	{		
		String xlFileName = "TestData.xls";
		String sheetName = "Login_Credentials";
		String tableName = "Credentials";
		String dataSheetPath = null;
		
		dataSheetPath = "testdata\\TestData.xls";
		
	    Object[][] retObjArr=getTableArray(dataSheetPath, sheetName, tableName);
	    
	    return(retObjArr);
	}*/
	
	
	/*@DataProvider
	public static Object[][] getRestData() throws Exception
	{		
		String xlFileName = "TestData.xls";
		String sheetName = "SearchManagerData";
		String tableName = "getRestPasswordTest";
		String dataSheetPath = null;
		
		dataSheetPath = "testdata\\TestData.xls";
		
	    Object[][] retObjArr=getTableArray(dataSheetPath, sheetName, tableName);
	    
	    return(retObjArr);
	}
	
	@DataProvider
	public static Object[][] getUserCreatData() throws Exception
	{		
		String xlFileName = "TestData.xls";
		String sheetName = "SearchManagerData";
		String tableName = "getRequriedField";
		String dataSheetPath = null;
		
		dataSheetPath = "testdata\\TestData.xls";
		
	    Object[][] retObjArr=getTableArray(dataSheetPath, sheetName, tableName);
	    
	    return(retObjArr);
	}
	
	@DataProvider
	public static Object[][] getSearchManagerDataList() throws Exception
	{		
		String xlFileName = "TestData.xls";
		String sheetName = "SearchManagerData";
		String tableName = "SearchManagerDataList";
		String dataSheetPath = null;
		
		dataSheetPath = "testdata\\TestData.xls";
		
	    Object[][] retObjArr=getTableArray(dataSheetPath, sheetName, tableName);
	    
	    return(retObjArr);
	}
	
	@DataProvider
	public static Object[][] getGrantAdmistratorData() throws Exception
	{		
		String xlFileName = "TestData.xls";
		String sheetName = "GrantAdmistratorData";
		String tableName = "grantAdministratorPermissionsTest";
		String dataSheetPath = null;
		
		dataSheetPath = "testdata\\TestData.xls";
		
	    Object[][] retObjArr=getTableArray(dataSheetPath, sheetName, tableName);
	    
	    return(retObjArr);
	}*/
	
	public static String[][] getTableArray(String xlFilePath, String sheetName, String tableName) throws Exception{
		String[][] tabArray=null;

		Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
		Sheet sheet = workbook.getSheet(sheetName); 
		int startRow,startCol, endRow, endCol,ci,cj;
		Cell tableStart=sheet.findCell(tableName);
		startRow=tableStart.getRow();
		startCol=tableStart.getColumn();
		
		
		Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);                

		endRow=tableEnd.getRow();
		endCol=tableEnd.getColumn();
		
		System.out.println("startRow="+startRow+", endRow="+endRow+", " +
				"startCol="+startCol+", endCol="+endCol);
		tabArray=new String[endRow-startRow-1][endCol-startCol-1];
		ci=0;

		for (int i=startRow+1;i<endRow;i++,ci++)
		{
			cj=0;
			for (int j=startCol+1;j<endCol;j++,cj++)
			{
				tabArray[ci][cj]=sheet.getCell(j,i).getContents();
			}
		}

		return(tabArray);
	}
	
}
