package org.healthCheckToolpkg;

/**
 * Created with IntelliJ IDEA.
 * User: azvolskiy
 * Date: 7/10/12
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
import java.net.MalformedURLException;

/**
 * http://amws-aries.od5.lohika.com:8080/jmx-console/com.mercury.topaz.cmdb.server.jmx.OperationFromStringForm?callerService=UCMDB%3Aservice%3DFramework+Services&customerID=1&operationString=bla
 */

public class jmxHttpInvokerImpl implements jmxHttpInvoker {
    private 	String 			urlParams				= "callerService=UCMDB%3Aservice%3DFramework+Services&customerID={1}&operationString={2}";
    private		String			urlPath 				= "jmx-console/com.mercury.topaz.cmdb.server.jmx.OperationFromStringForm";
    private 	credential	 	cred;
    private 	String			paramCustomerId 		= "1";
    private 	String			paramoperationString;
    private 	jmxHttpInvoke 	jhi;

    public jmxHttpInvokerImpl(credential cred) {
        this.cred = cred;
    }
    public void setParams() {
        urlParams.replace("{1}", paramCustomerId);
        urlParams.replace("{2}", paramoperationString);
    }
    public void invoke() throws MalformedURLException{
        jhi = new jmxHttpInvoke(cred.getHost(),cred.getPort(),cred.getUser(),cred.getPass());
        jhi.setProtocol(cred.getProtocol());
        jhi.createParams(urlParams);
        jhi.createUrl(urlPath);
        jhi.invoke();

    }
    public String getResult() {
        return jhi.getResponse();
    }
    public void close() {
        jhi.closeConnection();
    }
    public void invoke(logger log) throws MalformedURLException{
        jhi = new jmxHttpInvoke(cred.getHost(),cred.getPort(),cred.getUser(),cred.getPass(),log);
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