package me.christiansoler.xa.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.christiansoler.xa.R;
import me.christiansoler.xa.adapters.ImageListAdapter;

public class ImageListFragment extends Fragment {
    private ArrayList<String> mData;

    public ImageListFragment() {
    }

    public static Fragment newInstance(ArrayList<String> data) {
        ImageListFragment fragment = new ImageListFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        ImageListAdapter mAdapter = new ImageListAdapter(mData);
        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_image_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
