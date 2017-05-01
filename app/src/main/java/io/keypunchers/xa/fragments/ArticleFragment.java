package io.keypunchers.xa.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.xml.sax.XMLReader;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.Article;

public class ArticleFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        if (!mData.getImageUrls().isEmpty()) {

            VolleySingleton.getImageLoader().get(mData.getImageUrls().get(0), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    setupUI(view);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(ArticleFragment.class.getSimpleName(), error.getMessage());
                }
            });
        } else {
            setupUI(view);
        }
    }

    private void setupUI(View view) {

        VolleySingleton.getImageLoader()
                .get(mData.getAuthorProfileUrl(),
                        ImageLoader.getImageListener(((ImageView) view.findViewById(R.id.iv_article_author_avatar)), 0, 0));

        ((TextView) view.findViewById(R.id.tv_article_title))
                .setText(mData.getHeaderTitle());

        ((TextView) view.findViewById(R.id.tv_article_author_firstname))
                .setText(mData.getAuthorFirstName());

        ((TextView) view.findViewById(R.id.tv_article_author_lastname))
                .setText(mData.getAuthorLastName());

        ((TextView) view.findViewById(R.id.tv_article_author_firstname))
                .setText(mData.getAuthorFirstName());

        ((TextView) view.findViewById(R.id.tv_article_date))
                .setText(mData.getHeaderDate());

        Spanned text;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(mData.getBodyText(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM, null, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                    if (tag.equals("ul") && !opening) output.append("\n");
                    if (tag.equals("li") && opening) output.append("\n\t•\t");
                    if (tag.equals("blockquote")) output.append("\t\t");
                }
            });
        } else {
            text = Html.fromHtml(mData.getBodyText(), null, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                    if (tag.equals("ul") && !opening) output.append("\n");
                    if (tag.equals("li") && opening) output.append("\n\t•\t");
                    if (tag.equals("blockquote")) output.append("\n\t\t");
                }
            });
        }

        TextView mTvBody = (TextView) view.findViewById(R.id.tv_article_body);
        mTvBody.setText(text);
        mTvBody.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
