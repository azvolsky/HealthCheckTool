package org.healthCheckToolpkg;

/**
 *Author: Andrew Zvolskiy
 *Version: 2.0
 *Date: 8/19/2012
 *Last Modified Date: 8/19/2012
 *
 * This class provide ability to invoke ucmdb jmx method via http requests.
 * Also was added ability to ignore absence of certificate on client host in case when used https connection.
 * Class variables:
 * 	private String host			-		target host ip or dns name where located ucmdb server;
 *	private	String port			-		ucmdb port(by def 8080);
 *	private	String user			-		ucmdb jmx user name(by def sysadmin);
 *	private	String pass			-		ucmdb jmx user password;
 *	private String service		-		ucmdb jmx service name;
 *	private String method		-		ucmdb jmx method name in current service;
 *	private Object params[] 	-		list of method parameters occurs in the same way as it shows in jmx UI;
 *	private	String response		-		result of invoked jmx method in html view, can be used to check result of invoked method;
 *	private	String protocol		-		protocol. Can be only HTTP or HTTPS;
 *	private	String httpMethod	-		HTTP method. Should be "POST";
 *	private	String authMethod	-		method of web server auth. Should be "Basic";
 *	private	boolean doOutput	-		Need to clarify it purpose .Should be "true";
 *	private	logger log			-		Enable logger if not null, else exception message throws to standard out put.
 *	private static class DefaultTrustManager	-	implementation of interface X509TrustManager, used in httpsUrlConnect()
 *													to avoid problems with absence of certificate on client host
 * Class interface:
 * 	public void invoke()		-		invoke jmx method;
 * 	public String getResponse() -		get result of invoked method.
 **/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import sun.misc.BASE64Encoder;

public class jmxHttpInvoke implements invoker {
    private 	String 				host;
    private 	int 				port;
    private 	String 				user;
    private 	String 				pass;
    private 	String 				params;
    private		String				response;
    private		String				protocol			=	"HTTP";//HTTP/HTTPS
    private		String				httpMethod			=	"POST";
    private		String				authMethod			=	"Basic";
    private		boolean				doOutput			=	true;
    private		URL					url;
    private		boolean				httpsWithoutCert	=	true;
    private		logger				log;

    //############################CONSTRUCTORS#################################################################
    public jmxHttpInvoke(String host,int port,String user,String pass) {
        setJmxInvokeCredentials(host,port,user,pass);
    }
    public jmxHttpInvoke(String host,int port,String user,String pass,logger log) {
        setJmxInvokeCredentials(host,port,user,pass);
        setLog(log);
    }
    //#######################################INTERFACE########################################################
    /*
      *	Invoke prepared URL
      */
    public void invoke() {
        if(url!=null & params!=null){
            try {
                if(protocol.equalsIgnoreCase("HTTP")) {
                    HttpURLConnection connection = httpUrlConnect();
                    writeData(connection);
                    this.response = readData(connection);
                    connection.disconnect();
                }
                else if(protocol.equalsIgnoreCase("HTTPS")) {
                    try{
                        HttpsURLConnection connection;
                        if(httpsWithoutCert) {
                            connection = httpsUrlConnectWithoutCert();
                        }
                        else {
                            connection = httpsUrlConnect();
                        }
                        writeData(connection);
                        this.response = readData(connection);
                        connection.disconnect();
                    }
                    catch(NoSuchAlgorithmException nsae) {
                        logging(nsae);
                    }
                    catch(KeyManagementException kme) {
                        logging(kme);
                    }
                }
            }
            catch(UnsupportedEncodingException uee) {
                logging(uee);
            }
            catch(ProtocolException pe) {
                logging(pe);
            }
            catch(IOException ioe) {
                logging(ioe);
            }
            catch(Exception e) {
                logging(e);
            }
        }
    }
    /**
     *
     * @param appForm	- format: /jmx-console/HtmlAdaptor
     * @throws MalformedURLException
     */
    public void createUrl(String appForm)throws MalformedURLException {
        this.url = new URL (protocol + "://"+host+":"+port+appForm);
    }
    /**
     *
     * @param paramList - format: callerService=UCMDB%3Aservice%3DFramework+Services&customerID=1&operationString=bla
     */
    public void createParams(String paramList){
        this.params = paramList;
    }

    /*
      * Return result
      *
      * @return response of server in html.
      */
    public String getResponse() {
        return response;
    }
    /*
      * Set JMX credentials
      *
      * @param  host host name or ip
      * @param  port server port
      * @param  user	user name
      * @param  pass	user password
      */
    public void setJmxInvokeCredentials(String host,int port,String user,String pass) {
        setHost(host);
        setPort(port);
        setUser(user);
        setPass(pass);
    }
    //#################################################INTERNAL REALIZATION########################################
    private HttpURLConnection httpUrlConnect() throws IOException,ProtocolException {
        String credentials = user+":"+pass;
        String encoding = (new BASE64Encoder()).encode(credentials.getBytes());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Authorization", authMethod + " " + encoding);
        connection.setDoOutput(doOutput);
        connection.setRequestMethod(httpMethod);

        return connection;
    }
    private HttpsURLConnection httpsUrlConnect() throws IOException,ProtocolException,NoSuchAlgorithmException,KeyManagementException {
        String credentials = user+":"+pass;
        String encoding = (new BASE64Encoder()).encode(credentials.getBytes());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authMethod + " " + encoding);
        connection.setDoOutput(doOutput);
        connection.setRequestMethod(httpMethod);
        return connection;
    }
    private HttpsURLConnection httpsUrlConnectWithoutCert() throws IOException,ProtocolException,NoSuchAlgorithmException,KeyManagementException {
        jmxHttpConnectWithoutCert httpConnectWithoutCert = new jmxHttpConnectWithoutCert();
        httpConnectWithoutCert.createHostnameVerifier();
        String credentials = user+":"+pass;
        String encoding = (new BASE64Encoder()).encode(credentials.getBytes());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(httpConnectWithoutCert.getHostnameVerifier());
        connection.setRequestProperty("Authorization", authMethod + " " + encoding);
        connection.setDoOutput(doOutput);
        connection.setRequestMethod(httpMethod);
        return connection;
    }

    private void writeData(HttpURLConnection connection)throws IOException {
        String data = URLEncoder.encode(params, "UTF-8");
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(data);
        wr.flush();
    }
    private void writeData(HttpsURLConnection connection)throws IOException {
        String data = URLEncoder.encode(params, "UTF-8");
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(data);
        wr.flush();
    }
    private String readData(HttpURLConnection connection) throws IOException {
        StringBuffer response = new StringBuffer();
        InputStream content = (InputStream)connection.getInputStream();
        BufferedReader in   = new BufferedReader (new InputStreamReader (content));
        while (in.readLine() != null) {
            response.append(in.readLine());
//			response.append("\n");	// to show formated html
        }

        return new String(response);
    }
    private String readData(HttpsURLConnection connection) throws IOException {
        StringBuffer response = new StringBuffer();
        InputStream content = (InputStream)connection.getInputStream();
        BufferedReader in   = new BufferedReader (new InputStreamReader (content));
        while (in.readLine() != null) {
            response.append(in.readLine());
//			response.append("\n");	// to show formated html
        }

        return new String(response);
    }
    private void logging(Exception e){
        if(this.log != null) {
            log.log(this.getClass(), e);
        }
        else{
            e.printStackTrace();
        }
    }
    //####################################GETTERS AND SETTERS#################################################
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public String getHttpMethod() {
        return httpMethod;
    }
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
    public String getAuthMethod() {
        return authMethod;
    }
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }
    public boolean isDoOutput() {
        return doOutput;
    }
    public void setDoOutput(boolean doOutput) {
        this.doOutput = doOutput;
    }
    public logger getLog() {
        return log;
    }
    public void setLog(logger log) {
        this.log = log;
    }
    public boolean isHttpsWithoutCert() {
        return httpsWithoutCert;
    }
    public void setHttpsWithoutCert(boolean httpsWithoutCert) {
        this.httpsWithoutCert = httpsWithoutCert;
    }

}