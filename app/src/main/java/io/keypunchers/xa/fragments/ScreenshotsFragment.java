package io.keypunchers.xa.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ScreenshotsAdapter;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.LatestScreenshot;

public class ScreenshotsFragment extends Fragment {
    private ArrayList<LatestScreenshot> mData = new ArrayList<>();
    private String BASE_URL;
    private ScreenshotsAdapter mAdapter;

    public ScreenshotsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_screenshots, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        mAdapter = new ScreenshotsAdapter(getActivity(), mData);
        RecyclerView mRvContent = (RecyclerView) getActivity().findViewById(R.id.rv_latest_screenshot);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mData.isEmpty() && getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            String AB_TITLE = getArguments().getString("ab_title");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
        }

        if (mData.isEmpty()) {
            makeNetworkCall();
        }
    }

    private void makeNetworkCall() {
        RequestQueue mQueue = VolleySingleton.getRequestQueque();

        JsonArrayRequest mRequest = new JsonArrayRequest
                (Request.Method.GET, BASE_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject json = response.getJSONObject(i);

                                String title = json.getString("title");
                                String date = json.getString("date");
                                String image_url = json.getJSONObject("image_urls").getString("med");
                                String game_permalink = json.getString("game_permalink");

                                LatestScreenshot screenshot = new LatestScreenshot();
                                screenshot.setTitle(title);
                                screenshot.setDate(date);
                                screenshot.setImageUrl(image_url);
                                screenshot.setGamePermalink(game_permalink);

                                mData.add(screenshot);
                            }

                            mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mData.size());
                        } catch (Exception e) {
                            Log.e("Error: ", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                    }
                });

        mQueue.add(mRequest);
    }
}
