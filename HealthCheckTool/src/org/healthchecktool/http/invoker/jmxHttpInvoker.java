package org.healthchecktool.http.invoker;

import java.net.MalformedURLException;

public interface jmxHttpInvoker {
	public void setParams();
	public void invoke() throws MalformedURLException;
	public String getResult();
	public void close();
}
