package org.healthchecktool.util.logger;

public interface logger {
	public void log(Class exceptionClass,Exception exception);
	public void closeFile();
}
