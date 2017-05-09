package io.keypunchers.xa.fragments;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.ApplicationClass;

public class AboutFragment extends Fragment {
    private Tracker mTracker;

    public AboutFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            String versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            TextView mTvVersionName = (TextView) view.findViewById(R.id.tv_about_app_version_name);
            mTvVersionName.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ApplicationClass application = (ApplicationClass) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_about_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(AboutFragment.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
