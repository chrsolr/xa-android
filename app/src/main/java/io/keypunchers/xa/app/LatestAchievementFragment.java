package io.keypunchers.xa.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.keypunchers.xa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestAchievementFragment extends Fragment {


    public LatestAchievementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getContext(), "@Latest Achievements Fragment", Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_achievement, container, false);
    }

}
