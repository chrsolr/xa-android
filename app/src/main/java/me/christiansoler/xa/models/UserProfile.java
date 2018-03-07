package me.christiansoler.xa.models;

public class UserProfile {
    private String username;
    private String password;
	private String signature;

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged() {
        return (this.username != null && !this.username.equals("")) && (this.password != null && !this.password.equals(""));
    }
}
