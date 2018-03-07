package me.christiansoler.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import me.christiansoler.xa.R;
import me.christiansoler.xa.misc.VolleySingleton;
import me.christiansoler.xa.models.LatestScreenshot;
import me.christiansoler.xa.views.ScaledImageView;


public class LatestScreenshotsAdapter extends RecyclerView.Adapter<LatestScreenshotsAdapter.ViewHolder> {
    private ArrayList<LatestScreenshot> mData;

    public LatestScreenshotsAdapter(ArrayList<LatestScreenshot> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_latest_screenshots, parent, false);

        return new ViewHolder(view, context, mData);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LatestScreenshot item = mData.get(position);

        holder.mTvTitle.setText(item.getTitle());
        holder.mTvSubTitle.setText(item.getDateAdded());

        VolleySingleton.getImageLoader()
                .get(item.getImageUrl(),
                        ImageLoader.getImageListener(holder.mIvScreenshot, 0, 0));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTvTitle;
        TextView mTvSubTitle;
        ScaledImageView mIvScreenshot;
        private Context mContext;
        private ArrayList<LatestScreenshot> mData;

        public ViewHolder(View itemView, Context context, ArrayList<LatestScreenshot> data) {
            super(itemView);
            mContext = context;
            mData = data;

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_latest_screenshot_title);
            mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_latest_screenshot_subtitle);
            mIvScreenshot = (ScaledImageView) itemView.findViewById(R.id.iv_latest_screenshot_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String screenshots_url = mData.get(getAdapterPosition()).getImageUrl().replace("/thu", "/med");

            Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(screenshots_url));
            mContext.startActivity(mIntent);
        }
    }
}
