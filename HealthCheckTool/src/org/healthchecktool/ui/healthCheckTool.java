package org.healthchecktool.ui;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.healthchecktool.ui.mainform.mainForm;
import org.healthchecktool.util.config.config;

public class healthCheckTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String propertiesPath;
		if(args.length<1){
			propertiesPath = "D:\\github\\workspace\\projects\\gitRepository\\HealthCheckTool\\HealthCheckTool\\config.properties";
		}
		else{
			propertiesPath = args[1];	
		}
		config conf = config.getInstance();
		conf.setPropertiesFile(propertiesPath);
		try {
			conf.readPropertiesFromFile();
		}
		catch(FileNotFoundException fnfe) {
			
		}
		catch(IOException ioe) {
			
		}
		finally {
			
		}
		mainForm mf = new mainForm(conf);
		mf.showFarme();

	}

}
