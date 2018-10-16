package com.cogentworks.forthq;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.BaseAdapter;

import com.android.volley.Response;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetNews extends AsyncTask<String, Void, Void> {

    private Context context;
    private NewsFragment fragment;
    protected String endpoint;
    protected Response.Listener responseListener;
    private ArrayList<NewsItem> results = null;

    public GetNews(Context context, NewsFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(String... taskParams) {
        try {
            String url = "https://www.epicgames.com/fortnite/news";
            Document document = Jsoup.connect(url).get();

            Element blog = document.selectFirst(".blog-view");
            Elements posts = blog.select("a");
            results = new ArrayList<NewsItem>();

            for (Element post : posts) {
                NewsItem item = new NewsItem();
                item.image = StringUtils.substringBetween(post.select(".background-image,.feature-image").attr("style"), "url(", ")");
                item.title = post.selectFirst("h1,h2").text();
                item.body = post.selectFirst(".date,.feature-date").text();
                item.url = post.attr("href");

                results.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        fragment.listItems.clear();
        fragment.listItems.addAll(results);
        ((BaseAdapter)fragment.mListView.getAdapter()).notifyDataSetChanged();
        hideProgressBar(fragment);
    }

    protected void hideProgressBar(Fragment fragment) {
        fragment.getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }
}
