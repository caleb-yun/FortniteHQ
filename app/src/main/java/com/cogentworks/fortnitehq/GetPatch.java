package com.cogentworks.fortnitehq;

import android.content.Context;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetPatch extends GetData {

    private PatchFragment mFragment;
    private Context mContext;

    public GetPatch(Context context, PatchFragment fragment) {
        super(context);
        mContext = context;
        mFragment = fragment;
        endpoint = "https://fortnite-public-api.theapinetwork.com/prod09/patchnotes/get";

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray entries = jResponse.getJSONArray("blogList");
                    ArrayList<NewsItem> results = new ArrayList<NewsItem>();
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject currentEntry = entries.getJSONObject(i);
                        NewsItem newsItem = new NewsItem(
                                currentEntry.getString("title"),
                                currentEntry.getString("shareDescription"),
                                currentEntry.getString("image"),
                                currentEntry.getString("date"),
                                "https://www.epicgames.com/fortnite" + currentEntry.getString("externalLink"));
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
                } finally {
                    hideProgressBar(mFragment);
                }

            }
        };

    }
}
