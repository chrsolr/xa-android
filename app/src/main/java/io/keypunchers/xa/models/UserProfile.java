package io.keypunchers.xa.models;

import java.util.Map;

public class UserProfile {
	private String username;
	private String password;
	private Map<String, String> cookies;
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public boolean isLogged() {
		return (this.username != null && !this.username.equals("")) && (this.password != null && !this.password.equals(""));
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}
}
