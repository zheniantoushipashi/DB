package Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class readProperty {
	private static Properties properties;

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	Properties  getProperties() throws IOException{
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("db.properties");
		properties = new Properties();
		properties.load(in);
		return  properties;
	}
	
	public   String  getDbFilename() throws IOException{
		return "" + this.getProperties().get("DbFilename");
	}

}
