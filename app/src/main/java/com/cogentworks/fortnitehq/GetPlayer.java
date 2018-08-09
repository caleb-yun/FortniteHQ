package com.cogentworks.fortnitehq;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class GetPlayer extends GetData {

    private PlayerActivity mActivity;
    private Context mContext;

    public GetPlayer(PlayerActivity activity, String username, final boolean alltime) {
        super(activity);
        mActivity = activity;
        mContext = activity;
        endpoint = "https://fortnite-public-api.theapinetwork.com/prod09/users/id";
        activity.setTitle(username);
        params.put("username", username);

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray platforms = jResponse.getJSONArray("platforms");

                    new GetPlayerStats(mActivity,
                            jResponse.getString("uid"),
                            platforms.getString(0),
                            alltime).execute();

                } catch (Exception e) {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setTitle("Network Error")
                            .setNegativeButton("Ok", null)
                            .create();
                    dialog.show();

                    e.printStackTrace();
                }

            }
        };

    }

    class GetPlayerStats extends GetData {



        public GetPlayerStats(final PlayerActivity activity, String uid, String platform, boolean alltime) {

            super(activity);

            endpoint = "https://fortnite-public-api.theapinetwork.com/prod09/users/public/br_stats";
            params.put("user_id", uid);
            params.put("platform", platform);
            if (alltime)
                params.put("window", "alltime");
            else
                params.put("window", "current");

            responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jResponse = new JSONObject(response);
                        JSONObject stats = jResponse.getJSONObject("stats");
                        JSONObject totals = jResponse.getJSONObject("totals");

                        Iterator<String> keys = totals.keys();
                        String[] totalsArray = activity.getResources().getStringArray(R.array.array_totals);
                        for(String title : totalsArray) {
                            String key = keys.next();
                            View view = activity.getLayoutInflater().inflate(R.layout.stat_cell, null);
                            ((TextView)view.findViewById(R.id.title)).setText(title);
                            ((TextView)view.findViewById(R.id.stat)).setText(totals.getString(key));
                            Log.d("GetPlayer", key + totals.getString(key));
                            ((LinearLayout)activity.findViewById(R.id.totals)).addView(view);
                        }


                    } catch (Exception e) {
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setTitle("Network Error")
                                .setNegativeButton("Ok", null)
                                .create();
                        dialog.show();

                        e.printStackTrace();
                    }

                }
            };
        }
    }
}
