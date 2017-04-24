package com.lcec.utility;

import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author:Tapana
 * @version 1.0
 * @created 11/04/2017 
 * @Description:her we handle lrn.properties file.
 */
public class PropertyHandler 
{
	private static String fileName = "lrn.properties";
	private static Properties properties;
	public PropertyHandler(){
	}

	public void finalize() throws Throwable {
	}
	/**
	 *
	 * @param key
	 */
	public static String getProperty(String key){
		try{
			InputStream is = PropertyHandler.class.getResourceAsStream("/"+fileName);
			if(is==null)
			{
				throw new FileNotFoundException("path mentioned is wrong");
			}
			properties=new Properties();
			properties.load(is);
		}catch(FileNotFoundException fnfe){
			// log.error("File Not Found Error",fnfe);	
		}catch(IOException ie){
			// log.error("IO Error",ie);
		}
		String value=properties.getProperty(key);
		return value;
	}	

}
