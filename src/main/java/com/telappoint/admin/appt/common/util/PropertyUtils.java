package com.telappoint.admin.appt.common.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * 
 * Functionality Uploaded
 * 
 * @author Balaji 
 *
 */

public class PropertyUtils {
	private static Map<String, Properties> propsMap = new HashMap<String, Properties>();
	
	private static final Object lock = new Object();

	/**
	 * @param fileName
	 * @return An Inputstream object
	 * @throws Exception
	 *             if specified file not found
	 * @desc Creates an InputStream Object from a given file name.
	 */
	public static InputStream getResourceAsStream(String fileName) throws IOException {
		InputStream propsIn = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (propsIn == null) {
			propsIn = FileUtils.class.getResourceAsStream(fileName);
		}
		if (propsIn == null) {
			propsIn = ClassLoader.getSystemResourceAsStream(fileName);
		}
		
		if(propsIn == null) {
			propsIn = new FileInputStream(fileName);
		}

		return propsIn;
	}
	
	/**
	 * @desc Refreshes a Properties Object from a given file name by assigning it to null.
	 * @param fileName
	 * @return A Properties object
	 * @throws Exception
	 */
	public static Properties clearProperties(String fileName) throws Exception {
		Properties properties = new Properties();
		try(InputStream inputStream=getResourceAsStream(fileName)) {
			if(inputStream == null) {
				throw new IOException();
			}
			properties.load(inputStream);
			synchronized (lock) {
				propsMap.put(fileName, null);
			}
			
		} catch (IOException e) {
			throw new Exception(fileName+" file is not found");
		} 
		return properties;
	}

	/**
	 * @param fileName
	 * @return A Properties object
	 * @throws java.lang.Exception
	 * @desc Creates a Properties Object from a given file name.
	 */
	public static Properties getProperties(String fileName) throws IOException {
		Properties properties = propsMap.get(fileName);
		if (properties != null) {
			return properties;
		}
		try (InputStream inputStream=getResourceAsStream(fileName)){
			if(inputStream == null) {
				throw new IOException();
			}
			
			properties = new Properties();
			properties.load(inputStream);
			synchronized (lock) {
				propsMap.put(fileName, properties);
			}
			System.out.println(inputStream == null);
		} catch (IOException e) {
			throw new IOException(fileName + " file is not found");
		} 

		return properties;
	}

	public static String getValueFromProperties(String key, String fileName) throws IOException {
		Properties properties = getProperties(fileName);
		return (properties.get(key) !=null)?(String)properties.get(key):null;
	}
}
