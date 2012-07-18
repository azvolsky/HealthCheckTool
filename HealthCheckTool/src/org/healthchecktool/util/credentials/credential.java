package org.healthchecktool.util.credentials;

public class credential {
	private		String		protocol;
	private 	String 		host;
	private 	int 		port;
	private 	String 		user;
	private 	String 		pass;
	
	public credential(String protocol,String host,int port,String user,String pass) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

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
}