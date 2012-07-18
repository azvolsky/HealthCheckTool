package org.healthchecktool.util.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.healthchecktool.util.files.filerw;

public class config {
	private static config instance = null;
	private Properties config;
	private filerw file;
	
	protected config() {
		config = new Properties();
		instance = this;
	}
	public void setPropertiesFile(String path) {
		file = new filerw(path);
	}
	public void readPropertiesFromFile() throws FileNotFoundException,IOException {
		config.load(file.getReader());
	}
	public static config getInstance() {
		if(instance == null) {
			return new config();
		}
		else {
			return instance;
		}
	}
	public String getValueByKey(String key) {
		return config.getProperty(key);
	}
}
