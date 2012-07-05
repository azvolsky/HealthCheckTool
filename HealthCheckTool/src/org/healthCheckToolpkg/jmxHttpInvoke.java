package org.healthCheckToolpkg;

import sun.misc.BASE64Encoder;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA.
 * Author: Andrew Zvolskiy; Alex Syvorotka
 * Version: 2.1
 * Date: 19/06/2012
 * Last Modified Date: 01/07/2012
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 *
 * This class provides ability to invoke ucmdb jmx method via http requests.
 * In v 2.0 was added ability to ignore absence of certificate on client host in case when used https connection.
 *
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
 *	private	boolean doOutput	-		Need to clarify it purpose. Should be "true";
 *	private	logger log			-		Enable logger if not null, else exception message throws to standard out put.
 *	private static class DefaultTrustManager	-	implementation of interface X509TrustManager, used in httpsUrlConnect()
 *													to avoid problems with absence of certificate on client host
 * Class interface:
 * 	public void invoke()		-		invoke jmx method;
 * 	public String getResponse() -		get result of invoked method.
 **/

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import sun.misc.BASE64Encoder;
//import test.ucmdb.util.logger.logger;

public class jmxHttpInvoke {
    private 	String 		host;
    private 	int 		port;
    private 	String 		user;
    private 	String 		pass;
    private 	String 		service;
    private 	String 		method;
    private 	Object 		params[];
    private		String		response;
    private		String		protocol		=	"HTTP";//HTTP/HTTPS
    private		String		httpMethod		=	"POST";
    private		String		authMethod		=	"Basic";
    private		boolean		doOutput		=	true;
    private		logger		log;
    //
    private static class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    //############################CONSTRUCTORS#################################################################
    public jmxHttpInvoke() {
    }
    public jmxHttpInvoke(String host,int port,String user,String pass) {
        setJmxInvokeCredentials(host,port,user,pass);
    }
    public jmxHttpInvoke(String service,String method,Object params[],String host,int port,String user,String pass) {
        setJmxInvokeCredentials(host,port,user,pass);
        setJmxInvokeMethod(service,method,params);
    }
    public jmxHttpInvoke(String service,String method,Object params[],String host,int port,String user,String pass,logger log) {
        setJmxInvokeCredentials(host,port,user,pass);
        setJmxInvokeMethod(service,method,params);
        setLog(log);
    }
    //#######################################METHODS########################################################
    /*
      *	Invoke prepared URL
      */
    public void invoke() {

        try {
            String data = createData();
            if(protocol.equalsIgnoreCase("HTTP")) {
                HttpURLConnection con = httpUrlConnect();
                writeData(con, data);
                this.response = readData(con);  //what does this return here?
            }
            else if(protocol.equalsIgnoreCase("HTTPS")) {
                try{
                    HttpsURLConnection con = httpsUrlConnect();
                    writeData(con, data);
                    this.response = readData(con);
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
    /*
      * Return result
      *
      * @return response of server in html.
      */
    public String getResponse() {
        return response;
    }
    /*
      * Set JMX method. In case if method does not have any parameters 'null' without params
      *
      * @param  service  jmx service name
      * @param  method 	jmx method name
      * @param  params[]	list of method parameters
      */
    public void setJmxInvokeMethod(String service,String method,Object params[]) {
        setService(service);
        setMethod(method);
        setParams(params);
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
    //#############################################INTERNAL REALIZATION########################################
    private String createData() throws UnsupportedEncodingException {
        String data = new String();
        data += URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("invokeOpByName", "UTF-8");
        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(this.service, "UTF-8");
        data += "&" + URLEncoder.encode("methodName", "UTF-8") + "=" + URLEncoder.encode(this.method, "UTF-8");
        if(params != null) {
            for(int i=0;i<params.length;i++) {
                data += "&";
                if(!params[i].equals("")) {
                    data += "arg" + i + "=" + URLEncoder.encode(params[i].toString(), "UTF-8");
                }
                else {
                    break;
                }
            }
        }
        return data;
    }
    private HttpURLConnection httpUrlConnect() throws IOException,ProtocolException {
        URL url = new URL (protocol + "://"+host+":"+port+"/jmx-console/HtmlAdaptor");
        String credentials = user+":"+pass;
        String encoding = (new BASE64Encoder()).encode(credentials.getBytes());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Authorization", authMethod + " " + encoding);
        connection.setDoOutput(doOutput);
        connection.setRequestMethod(httpMethod);
        return connection;
    }
    private HttpsURLConnection httpsUrlConnect() throws IOException,ProtocolException,NoSuchAlgorithmException,KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);
        URL url = new URL (protocol + "://"+host+":"+port+"/jmx-console/HtmlAdaptor");
        String credentials = user+":"+pass;
        String encoding = (new BASE64Encoder()).encode(credentials.getBytes());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        connection.setRequestProperty("Authorization", authMethod + " " + encoding);
        connection.setDoOutput(doOutput);
        connection.setRequestMethod(httpMethod);
        return connection;
    }
    private void writeData(HttpURLConnection connection,String data)throws IOException {
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
//			response.append("\n");
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
    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public Object[] getParams() {
        return params;
    }
    public void setParams(Object[] params) {
        this.params = params;
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

}