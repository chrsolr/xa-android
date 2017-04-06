package io.keypunchers.xa.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;

import io.keypunchers.xa.R;
import io.keypunchers.xa.loaders.LatestScreenshotsLoader;

public class UpcomingGamesFragment extends Fragment {


    public UpcomingGamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_games, container, false);

        Toast.makeText(getContext(), "@Upcoming Games Fragment", Toast.LENGTH_LONG).show();


        return view;
    }
}
