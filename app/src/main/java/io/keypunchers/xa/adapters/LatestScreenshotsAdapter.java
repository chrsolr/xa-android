package io.keypunchers.xa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.SingletonVolley;


public class LatestScreenshotsAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray mData;

    public LatestScreenshotsAdapter(Context context, JSONArray data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mData.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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
            convertView = inflater.inflate(R.layout.row_latest_screenshots, null);

            viewHolder = new ViewHolder();
            assert  convertView != null;
            viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_latest_screenshot_title);
            viewHolder.mTvSubTitle = (TextView) convertView.findViewById(R.id.tv_latest_screenshot_subtitle);
            viewHolder.mIvScreenshot = (NetworkImageView) convertView.findViewById(R.id.iv_latest_screenshot_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            JSONObject json = mData.getJSONObject(position);

            viewHolder.mTvTitle.setText(json.getString("title"));
            viewHolder.mTvSubTitle.setText(json.getString("date"));
            viewHolder.mIvScreenshot.setImageUrl(json.getString("image_url"), SingletonVolley.getImageLoader());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mTvTitle;
        TextView mTvSubTitle;
        NetworkImageView mIvScreenshot;
    }
}
