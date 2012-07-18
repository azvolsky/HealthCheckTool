package org.healthchecktool.http.invoke.withoutcert;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class jmxHttpConnectWithoutCert {
	private HostnameVerifier hnv;
	public jmxHttpConnectWithoutCert() {
		hnv = null;
	}
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
	public void createHostnameVerifier() throws KeyManagementException,NoSuchAlgorithmException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
		SSLContext.setDefault(ctx);
		hnv = new HostnameVerifier() {@Override public boolean verify(String arg0, SSLSession arg1) {return true;}};
	}
	public HostnameVerifier getHostnameVerifier() {
		return hnv;
	}
}