package io.keypunchers.xa.models;

import java.util.Map;

public class UserProfile {
	private String username;
	private String password;
	private Map<String, String> cookies;
	private long expire;
	
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
		if (System.currentTimeMillis() > expire)
			cookies = null;

		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.expire = System.currentTimeMillis() + 72000;
		this.cookies = cookies;
	}
}
