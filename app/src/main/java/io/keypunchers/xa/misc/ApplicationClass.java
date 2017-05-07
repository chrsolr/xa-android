package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import io.keypunchers.xa.models.UserProfile;

public class ApplicationClass extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        VolleySingleton.instantiate(this);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		boolean hasCredentials = prefs.contains("XA_USERNAME") && prefs.contains("XA_PASSWORD");
		
		if (hasCredentials) {
			UserProfile profile = new UserProfile();
			profile.setUsername(prefs.getString("XA_USERNAME", null));
			profile.setPassword(prefs.getString("XA_PASSWORD", null));
			
			Singleton.getInstance().setUserProfile(profile);
		}
    }
}
