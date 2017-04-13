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

import io.keypunchers.xa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_about_title);

        setupUI();
    }

    private void setupUI() {
        try {
            String app_version = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;

            ((TextView) getActivity().findViewById(R.id.tv_about_version)).setText(app_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}