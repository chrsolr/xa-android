package io.keypunchers.xa.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.app.ArticleActivity;
import io.keypunchers.xa.loaders.ArticleListLoader;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.ArticleListItem;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ArticleListItem>>, AdapterView.OnItemClickListener {
    private String BASE_URL;
    private int COUNTER_PLUS;
    private ArrayList<ArticleListItem> mData = new ArrayList<>();
    private GenericAdapter<ArticleListItem> mAdapter;

    public ArticleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        setAdapter();

        ListView mLvContent = (ListView) view.findViewById(R.id.lv_news);
        mLvContent.setOnItemClickListener(this);
        mLvContent.setAdapter(mAdapter);
    }

    private void setAdapter() {
        mAdapter = new GenericAdapter<>(getContext(), mData, new GenericAdapter.onSetGetView() {
            @Override
            public View onGetView(int position, View convertView, ViewGroup parent, Context context, ArrayList data) {
                ViewHolder viewHolder;

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.row_article_list, parent, false);

                    viewHolder = new ViewHolder();
                    assert convertView != null;

                    viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
                    viewHolder.mTvSubTitle = (TextView) convertView.findViewById(R.id.tv_news_subtitle);
                    viewHolder.mIvImage = (NetworkImageView) convertView.findViewById(R.id.iv_news_image);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                ArticleListItem item = (ArticleListItem) data.get(position);

                viewHolder.mTvTitle.setText(item.getTitle());
                viewHolder.mTvSubTitle.setText(item.getDesc());
                viewHolder.mIvImage.setImageUrl(item.getImageUrl(), SingletonVolley.getImageLoader());

                return convertView;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int LOADER_ID = getActivity().getResources().getInteger(R.integer.news_loader_id);

        if (getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            COUNTER_PLUS = getArguments().getInt("counter");
            String AB_TITLE = getArguments().getString("ab_title");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
        }

        if (mData.isEmpty()) {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<ArrayList<ArticleListItem>> onCreateLoader(int id, Bundle args) {
        return new ArticleListLoader(getActivity(), BASE_URL, mData, COUNTER_PLUS);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ArticleListItem>> loader, ArrayList<ArticleListItem> data) {
        mData = data;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ArticleListItem>> loader) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = mAdapter.getItem(position).getPageUrl();
        startActivity(new Intent(getActivity(), ArticleActivity.class).putExtra("url", url));
    }

    private class ViewHolder {
        TextView mTvTitle;
        TextView mTvSubTitle;
        NetworkImageView mIvImage;
    }
}