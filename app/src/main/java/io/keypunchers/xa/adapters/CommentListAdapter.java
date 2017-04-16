package io.keypunchers.xa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.models.Comment;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Comment> mData;

    public CommentListAdapter(Context context, ArrayList<Comment> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_comment_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment item = mData.get(position);

        holder.mTitle.setText(item.getTitle());
        holder.mDate.setText(item.getDate());
        holder.mText.setText(item.getText());

        Picasso.with(mContext)
                .load(item.getImageUrl())
                .placeholder(R.drawable.x360a_comments_notag)
                .error(R.drawable.x360a_comments_notag)
                .noFade()
                .into(holder.mIvImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        TextView mTitle;
        TextView mDate;
        TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_comment_list);
            mTitle = (TextView) itemView.findViewById(R.id.tv_comment_title);
            mDate = (TextView) itemView.findViewById(R.id.tv_comment_date);
            mText = (TextView) itemView.findViewById(R.id.tv_comment_text);
        }
    }
}