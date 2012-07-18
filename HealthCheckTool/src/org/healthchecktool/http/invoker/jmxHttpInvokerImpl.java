package org.healthchecktool.http.invoker;

import java.net.MalformedURLException;
import org.healthchecktool.http.invoke.jmxHttpInvokeImpl;
import org.healthchecktool.util.credentials.credential;
import org.healthchecktool.util.logger.logger;

/**
 * http://amws-aries.od5.lohika.com:8080/jmx-console/com.mercury.topaz.cmdb.server.jmx.OperationFromStringForm?callerService=UCMDB%3Aservice%3DFramework+Services&customerID=1&operationString=bla
 * @param args
 */

public class jmxHttpInvokerImpl implements jmxHttpInvoker {
	private 	String 			urlParams				= "callerService=UCMDB%3Aservice%3DFramework+Services&customerID={1}&operationString={2}";
	private		String			urlPath 				= "/jmx-console/com.mercury.topaz.cmdb.server.jmx.OperationFromStringForm";
	private 	credential	 	cred;
	private 	String			paramCustomerId 		= "1";
	private 	String			paramoperationString;
	private 	jmxHttpInvokeImpl 	jhi;
	
	public jmxHttpInvokerImpl(credential cred) {
		this.cred = cred;
	}
	public void setParams() {
		urlParams.replace("{1}", paramCustomerId);
		urlParams.replace("{2}", paramoperationString);
	}
	public void invoke() throws MalformedURLException{
		jhi = new jmxHttpInvokeImpl(cred.getHost(),cred.getPort(),cred.getUser(),cred.getPass());
		setParams();
		jhi.setProtocol(cred.getProtocol());
		jhi.createParams(urlParams);
		jhi.createUrl(urlPath);
		jhi.invoke();
		
	}
	public String getResult() {
		return jhi.getResponse();
	}
	public void close() {
//		jhi.closeConnection();
	}
	public void invoke(logger log) throws MalformedURLException{
		jhi = new jmxHttpInvokeImpl(cred.getHost(),cred.getPort(),cred.getUser(),cred.getPass(),log);
		jhi.setProtocol(cred.getProtocol());
		jhi.createParams(urlParams);
		jhi.createUrl(urlPath);
		jhi.invoke();
		
	}
	public String getUrlParams() {
		return urlParams;
	}
	public void setUrlParams(String urlParams) {
		this.urlParams = urlParams;
	}
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	public String getParamCustomerId() {
		return paramCustomerId;
	}
	public void setParamCustomerId(String paramCustomerId) {
		this.paramCustomerId = paramCustomerId;
	}
	public String getParamoperationString() {
		return paramoperationString;
	}
	public void setParamoperationString(String paramoperationString) {
		this.paramoperationString = paramoperationString;
	}	
}