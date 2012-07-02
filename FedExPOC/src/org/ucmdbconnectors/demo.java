package org.ucmdbconnectors;

/**
 * Created with IntelliJ IDEA.
 * User: asyvorotka
 * Date: 25/06/12
 * Time: 17:24
 * To change this template use File | Settings | File Templates.
 */

//I commented this three lines because I do not use ucmdbConnectorApi con = new ucmdbConnectorApi(); see that line
//import com.hp.ucmdb.api.UcmdbService;
//
//import test.ucmdb.jmx.http.jmxHttpInvoke;
//import test.ucmdb.util.logger.logger;

public class demo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        logger log = new logger("HealthCheckTool.Error.log");
//added connection details to constructor of ucmdbConnectorApi
//          ucmdbConnectorApi con = new ucmdbConnectorApi("hpws-asinyuk.od5.lohika.com", 8080, "cert", "cert");
//          con.setLog(log);
//          con.connect();
//          UcmdbService us = con.getUcmdbService();
//          Object monitors[] = us.getSystemHealthService().getPerformanceMetrics().getMonitorNames().toArray();
//          for(int i=0;i<monitors.length;i++)
//              System.out.println(monitors[i]);
//          con.closeConnect();
//
        jmxHttpInvoke jhi = new jmxHttpInvoke();
        jhi.setLog(log);
        jhi.setJmxInvokeCredentials("localhost", 8080, "sysadmin", "sysadmin");
        Object params[] = {"1","node"};//new Object[3];
        //jhi.setJmxInvokeMethod("UCMDB:service=Discovery Manager", "getAllRegisteredProbes", params);
        //jhi.setJmxInvokeMethod("UCMDB:service=Security Services", "showAllSystemUsers", null);
        jhi.setJmxInvokeMethod("UCMDB:service=Class Model Services", "retrieveClassHierarchy", params);
        jhi.invoke();
        System.out.println(jhi.getResponse());
        jhi.setPort(8443);
        jhi.setProtocol("HTTPS");
        //jhi.invoke();   this is for https.
        System.out.println(jhi.getResponse());

        log.closeFile();
    }

}