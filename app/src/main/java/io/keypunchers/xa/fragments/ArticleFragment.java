package io.keypunchers.xa.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.xml.sax.XMLReader;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Article;

public class ArticleFragment extends Fragment {
    private final String TAG = ArticleFragment.class.getSimpleName();
    private Article mData;

    public ArticleFragment() {
    }

    public static Fragment newInstance(Article data) {
        ArticleFragment fragment = new ArticleFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelable(TAG);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        ((NetworkImageView) getActivity().findViewById(R.id.iv_article_author_avatar))
                .setImageUrl(mData.getAuthorProfileImageUrl(), SingletonVolley.getImageLoader());

        ((TextView) getActivity().findViewById(R.id.tv_article_title))
                .setText(mData.getHeaderTitle());

        ((TextView) getActivity().findViewById(R.id.tv_article_date))
                .setText(mData.getHeaderDate());

        Spanned text;
        TextView mTvBody = (TextView) getActivity().findViewById(R.id.tv_article_body);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(mData.getBodyText(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM, null, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                    if (tag.equals("ul") && !opening) output.append("\n");
                    if (tag.equals("li") && opening) output.append("\n\t•\t\t");
                }
            });
        } else {
            text = Html.fromHtml(mData.getBodyText(), null, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                    if (tag.equals("ul") && !opening) output.append("\n");
                    if (tag.equals("li") && opening) output.append("\n\t•\t\t");
                }
            });
        }

        mTvBody.setText(text);
        mTvBody.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TAG, mData);
    }
}
