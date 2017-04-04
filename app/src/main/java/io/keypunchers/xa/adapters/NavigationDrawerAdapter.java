package io.keypunchers.xa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import io.keypunchers.xa.R;


public class NavigationDrawerAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mTitles;

    public NavigationDrawerAdapter(Context context, String[] titles) {
        mContext = context;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_navigation_drawer, null);

            viewHolder = new ViewHolder();
            assert  convertView != null;
            viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_drawer_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTvTitle.setText(mTitles[position]);

        return convertView;
    }

    private class ViewHolder {
        TextView mTvTitle;
    }
}
