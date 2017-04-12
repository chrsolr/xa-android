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
import io.keypunchers.xa.models.UpcomingGame;

public class UpcomingGamesChildFragment extends Fragment {
    private ArrayList<UpcomingGame> mData;
    private String TAG = UpcomingGamesChildFragment.class.getSimpleName();

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

        UpcomingGamesAdapter mAdapter = new UpcomingGamesAdapter(getActivity(), mData);
        ListView mLvContent = (ListView) view.findViewById(R.id.lv_upcoming_games_child);
        mLvContent.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            mData = getArguments().getParcelableArrayList("data");
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelableArrayList(TAG);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG, mData);
    }

    private class UpcomingGamesAdapter extends BaseAdapter {
        private final Context mContext;
        private final ArrayList<UpcomingGame> mData;

        UpcomingGamesAdapter(Context context, ArrayList<UpcomingGame> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public UpcomingGame getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            viewHolder.mDate.setText(mData.get(position).getDate());

            return convertView;
        }

        private class ViewHolder {
            TextView mTitle;
            TextView mDate;
        }
    }
}
