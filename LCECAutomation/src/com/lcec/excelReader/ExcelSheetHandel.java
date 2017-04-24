package com.lcec.excelReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.lcec.utility.PropertyHandler;

/*@Authore:Tapana Kumar Badhai
 *@Descraption:her we handle excel sheet file.this file support to geting data from excel sheet and send data to the 
 *excel sheet.  
 * 
 * 
 * */

public class ExcelSheetHandel 
{
	//load logger file
	private static Logger Log = Logger.getLogger(ExcelSheetHandel.class.getName());
	//copy data to copy input excel  sheet to output excel sheet.
	public void copyData(){
		try {
			//load file of excel sheet.
			FileInputStream file = new FileInputStream(new File(PropertyHandler.getProperty("InPutDataFile")));
			//Get the workbook instance for XLS file 
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			//Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			//Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				//For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					switch(cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:

						break;
					case Cell.CELL_TYPE_NUMERIC:

						break;
					case Cell.CELL_TYPE_STRING:

						break;
					}
				}
			}
			file.close();
			FileOutputStream out =new FileOutputStream(new File(PropertyHandler.getProperty("OutPutDataFile")));
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			Log.info("exception in reading  data from list");
		} catch (IOException e) {
			Log.info("exception in reading  data from list");
		}
	}
	//set the data in excel sheet
	public void setExcelStringData(int sheetNo, int rowNum , int columeNum , String data,String filePath) throws InvalidFormatException, IOException{
		//Property   file add
		//out put data set
		FileInputStream fis = new FileInputStream(PropertyHandler.getProperty(filePath));
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		Sheet sh = workbook.getSheetAt(sheetNo);
		Row r = sh.getRow(rowNum);
		Cell c = r.createCell(columeNum);
		c.setCellType(Cell.CELL_TYPE_STRING);
		c.setCellValue(data);
		//set the value in out put data(OutPutDataFile)
		FileOutputStream fos = new FileOutputStream(PropertyHandler.getProperty(filePath));
		workbook.write(fos);	 
	}
	public void setErrorMessage(int sheetNo, int rowNum , int columeNum , String data) throws IOException{
		// Create Workbook and Worksheet
		FileInputStream fis = new FileInputStream(PropertyHandler.getProperty("OutPutDataFile")); 
		HSSFWorkbook my_workbook = new HSSFWorkbook(fis);
		HSSFSheet my_sheet = my_workbook.getSheetAt(sheetNo);
		// Get access to HSSFCellStyl
		HSSFCellStyle my_style = my_workbook.createCellStyle();
		// We will now specify a background cell color */
		my_style.setFillPattern(HSSFCellStyle.FINE_DOTS );
		my_style.setFillForegroundColor(new HSSFColor.BLUE_GREY().getIndex());
		my_style.setFillBackgroundColor(new HSSFColor.PINK().getIndex());

		//Create a row in the sheet */
		Row row = my_sheet.getRow(rowNum);
		//Create a cell
		Cell cell = row.createCell(columeNum);
		cell.setCellValue(data);
		//Attach the style to the cell
		cell.setCellStyle(my_style);
		//Write changes to the workbook
		FileOutputStream out = new FileOutputStream(new File(PropertyHandler.getProperty("OutPutDataFile")));
		my_workbook.write(out);
	}
}
