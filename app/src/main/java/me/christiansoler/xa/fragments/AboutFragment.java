package me.christiansoler.xa.fragments;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import me.christiansoler.xa.R;
import me.christiansoler.xa.misc.VolleySingleton;

public class AboutFragment extends Fragment {
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

            ImageView mIvBanner = (ImageView) view.findViewById(R.id.iv_about_banner);
            VolleySingleton.getImageLoader()
                    .get("http://i.imgur.com/OQ7AgPN.jpg",
                            ImageLoader.getImageListener(mIvBanner, 0, 0));

            ImageView mIvAvatar = (ImageView) view.findViewById(R.id.iv_about_avatar);
            VolleySingleton.getImageLoader()
                    .get("http://i.imgur.com/C7sR3GQ.jpg",
                            ImageLoader.getImageListener(mIvAvatar, 0, 0));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_about_title);
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.LOCATION), AboutFragment.class.getSimpleName());
    }
}
