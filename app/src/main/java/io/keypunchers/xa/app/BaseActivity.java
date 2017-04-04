package io.keypunchers.xa.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.keypunchers.xa.R;

public class BaseActivity extends AppCompatActivity {
    private final String PREF_USER_LEARNED_DRAWER = "USER_LEARNED_DRAWER";
    private final String STATE_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private boolean mUserLearnedDrawer = false;
    private int mCurrentSelectedPosition = 2;
    private String[] mDrawerTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().getThemedContext();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedDrawer = prefs.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);
    }
}
