package io.keypunchers.xa.misc;
import io.keypunchers.xa.models.UserProfile;

public class Singleton {
	private static Singleton instance;
	private UserProfile mUserProfile;
	
	private Singleton(){}
	
	public static Singleton getInstance(){
		if (instance == null)
			instance = new Singleton();
		 
		return instance;
	}
	
	public void destroyUserProfile(){
		mUserProfile = null;
	}
	
	public void setUserProfile(UserProfile profile) {
		if (mUserProfile == null)
			mUserProfile = profile;
	}
	
	public UserProfile getUserProfile() {
		if (mUserProfile == null)
			mUserProfile = new UserProfile();
			
		return mUserProfile;
	}
}
