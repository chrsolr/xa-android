package io.keypunchers.xa.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.models.UpcomingGame;

public class UpcomingGamesChildFragment extends Fragment {
    private ArrayList<UpcomingGame> mData;
    private String TAG = UpcomingGamesChildFragment.class.getSimpleName();
    private GenericAdapter<UpcomingGame> mAdapter;

    public UpcomingGamesChildFragment() { }

    public static Fragment newInstance(ArrayList<UpcomingGame> data) {
        UpcomingGamesChildFragment fragment = new UpcomingGamesChildFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upcoming_games_child, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        setAdapter();

        ListView mLvContent = (ListView) view.findViewById(R.id.lv_upcoming_games_child);
        mLvContent.setAdapter(mAdapter);
    }

    private void setAdapter() {
        mAdapter = new GenericAdapter<>(getActivity(), mData, new GenericAdapter.onSetGetView() {
            @Override
            public View onGetView(int position, View convertView, ViewGroup parent, Context context, ArrayList<?> data) {
                ViewHolder viewHolder;

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.row_upcoming_games_child, parent, false);

                    viewHolder = new ViewHolder();
                    assert convertView != null;

                    viewHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_upcoming_games_title);
                    viewHolder.mDate = (TextView) convertView.findViewById(R.id.tv_upcoming_games_date);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.mTitle.setText(mData.get(position).getTitle());
                viewHolder.mDate.setText(mData.get(position).getReleaseDate());

                return convertView;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mData.isEmpty() && getArguments() != null) {
            mData = getArguments().getParcelableArrayList("data");
        }
    }

    private class ViewHolder {
        TextView mTitle;
        TextView mDate;
    }
}
