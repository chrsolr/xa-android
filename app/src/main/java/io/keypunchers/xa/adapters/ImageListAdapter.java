package io.keypunchers.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.views.ScaledImageView;
import com.bumptech.glide.Glide;
import io.keypunchers.xa.misc.VolleySingleton;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.Response;
import android.graphics.Bitmap;
import com.android.volley.VolleyError;
import android.support.design.widget.Snackbar;
import com.android.volley.toolbox.ImageLoader;


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mData;

    public ImageListAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_image_list, parent, false);

        return new ViewHolder(view, mData, context);
    }

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String item = mData.get(position);
		
		VolleySingleton.getImageLoader().get(item, ImageLoader.getImageListener(holder.mIvImage, 0, 0));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
	
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private Context mContext;
        private ArrayList<String> mData;

        ScaledImageView mIvImage;

        public ViewHolder(View itemView, ArrayList<String> data, Context context) {
            super(itemView);
			mData = data;
            mContext = context;

            mIvImage = (ScaledImageView) itemView.findViewById(R.id.iv_image_list);
			
			itemView.setOnClickListener(this);
        }
		
		@Override
        public void onClick(View v) {
        	Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(getAdapterPosition())));
			mContext.startActivity(mIntent);
        }
    }
}
