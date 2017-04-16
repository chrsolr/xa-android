package io.keypunchers.xa.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.CommentListAdapter;
import io.keypunchers.xa.models.Comment;

public class CommentListFragment extends Fragment {
    private ArrayList<Comment> mData;

    public CommentListFragment() {
    }

    public static Fragment newInstance(ArrayList<Comment> data) {
        CommentListFragment fragment = new CommentListFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        CommentListAdapter mAdapter = new CommentListAdapter(getActivity(), mData);
        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_comment_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
