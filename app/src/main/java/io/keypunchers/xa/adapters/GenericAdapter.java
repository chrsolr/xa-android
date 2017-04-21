package io.keypunchers.xa.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.*;

public class GenericAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mData;
    private onSetGetView mAdapter;

    public GenericAdapter(Context mContext, List<T> mData, onSetGetView adapter) {
        this.mContext = mContext;
        this.mData = mData;
        this.mAdapter = adapter;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mAdapter.onGetView(position, convertView, parent, mContext, mData);
    }

    public interface onSetGetView {
        View onGetView(int position, View convertView, ViewGroup parent, Context context, List<?> data);
    }
}
