package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.keypunchers.xa.R;
import io.keypunchers.xa.models.UserProfile;
import android.support.multidex.MultiDex;

public class ApplicationClass extends Application {
	private Tracker mTracker;

    @Override
    public void onCreate(){
        super.onCreate();
		MultiDex.install(this);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		boolean hasCredentials = prefs.contains(getString(R.string.XA_PASSWORD)) && prefs.contains(getString(R.string.XA_PASSWORD));
		
		if (hasCredentials) {
			UserProfile profile = new UserProfile();
			profile.setUsername(prefs.getString(getString(R.string.XA_USERNAME), null));
			profile.setPassword(prefs.getString(getString(R.string.XA_PASSWORD), null));
			
			Singleton.getInstance().setUserProfile(profile);
		}
    }

	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			mTracker = analytics.newTracker(R.xml.global_tracker);
		}
		return mTracker;
	}
}
