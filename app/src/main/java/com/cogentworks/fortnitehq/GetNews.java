package com.cogentworks.fortnitehq;

import android.content.Context;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetNews extends GetData {

    private NewsFragment mFragment;
    private Context mContext;

    public GetNews(Context context, NewsFragment fragment) {
        super(context);
        mContext = context;
        mFragment = fragment;
        endpoint = "https://fortnite-public-api.theapinetwork.com/prod09/br_motd/get";

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray entries = jResponse.getJSONArray("entries");
                    ArrayList<NewsItem> results = new ArrayList<NewsItem>();
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject currentEntry = entries.getJSONObject(i);
                        NewsItem newsItem = new NewsItem(
                                currentEntry.getString("title"),
                                currentEntry.getString("body"),
                                currentEntry.getString("image"),
                                currentEntry.getInt("time"));
                        results.add(newsItem);
                    }
                    mFragment.listItems.clear();
                    mFragment.listItems.addAll(results);
                    ((BaseAdapter)mFragment.mListView.getAdapter()).notifyDataSetChanged();

                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };

    }
}
