package org.ucmdbconnectors;

/**
 * Created with IntelliJ IDEA.
 * Author: Andrew Zvolskiy; Alex Syvorotka
 * Version: 2.1
 * Date: 19/06/2012
 * Last Modified Date: 01/07/2012
 * To change this template use File | Settings | File Templates.
 *
 *	Class serves as wrapper for ucmdb connection api. Used ucmdb-api.jar lib to connect to ucmdb api. Class
 *	contains refference to UcmdbService object, connected to server.
 *	Class variables:
 *		private String 			host			-	server host ip or name;
 *		private int				port			-	server port;
 *		private String 			user			-	user with permissions for api(for 9x integration user);
 *		private String 			pass			-	user password;
 *		private String 			clientContext	-	used as connection param def "ClientContext";
 *		private UcmdbService 	ucmdbService	-	ref to UcmdbService object;
 *		private	logger			log				-	Enable logger if not null, else exception message throws to standard output.
 *	Class interface:
 *		connect()			-	connect to server api and creates UcmdbService object;
 *		getUcmdbService()	-	return ref to UcmdbService object;
 *		closeConnect()		-	assign ref to UcmdbService object null value.
 */

import java.net.MalformedURLException;

//import org.ucmdbconnectors.logger;
import java.net.MalformedURLException;
import com.hp.ucmdb.api.*;

public class ucmdbConnectorApi {
    private 		String 				host;
    private 		int 				port;
    private 		String 				user;
    private 		String 				pass;
    private 	 	String 				clientContext;// def val "ClientContext"
    private 		UcmdbService 		ucmdbService;
    private			logger				log;
    //##################################################constructors##########################################

    public ucmdbConnectorApi(String host, int port,String user, String pass)
    {
        this.setHost(host);
        this.setPort(port);
        this.setUser(user);
        this.setPass(pass);
        this.setClientContext("ClientContext");
        this.log = null;
    }
    public ucmdbConnectorApi(String host, int port,String user, String pass, String clientContext)
    {
        this.setHost(host);
        this.setPort(port);
        this.setUser(user);
        this.setPass(pass);
        this.setClientContext(clientContext);
        this.log = null;
    }
    public ucmdbConnectorApi(String host, int port,String user, String pass, String clientContext,logger log)
    {
        this.setHost(host);
        this.setPort(port);
        this.setUser(user);
        this.setPass(pass);
        this.setClientContext(clientContext);
        this.log = log;
    }
    //#########################################interface#######################################################
    public void connect() {
        try {
            UcmdbServiceProvider provider = UcmdbServiceFactory.getServiceProvider(getHost(),getPort());
            try {
                ucmdbService = provider.connect(
                        provider.createCredentials(getUser(), getPass()),
                        provider.createClientContext(getClientContext()));
            }
            catch(InvalidCredentialsException ice){
                logging(ice);
                closeConnect();
            }
            catch(CustomerNotAvailableException cnae){
                logging(cnae);
                closeConnect();
            }
        }
        catch(MalformedURLException mue){
            logging(mue);
            closeConnect();
        }
        catch(IncompatibleVersionException ive){
            logging(ive);
            closeConnect();
        }
        catch(Exception e) {
            logging(e);
            closeConnect();
        }
    }
    public UcmdbService getUcmdbService(){
        return ucmdbService;
    }
    public void closeConnect() {
        if(ucmdbService == null)
            ucmdbService = null;
    }
    //#####################################INTERNAL IMPLEMENTATION##############################################
    private void logging(Exception e){
        if(this.log != null) {
            log.log(this.getClass(), e);
        }
        else{
            e.printStackTrace();
        }
    }
    //###########################################getters and setters#########################################
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
    public String getClientContext() {
        return clientContext;
    }
    public void setClientContext(String clientContext) {
        this.clientContext = clientContext;
    }
    public logger getLog() {
        return log;
    }
    public void setLog(logger log) {
        this.log = log;
    }
}