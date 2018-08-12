package com.cogentworks.fortnitehq;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
                        ArrayList<Player> players = new ArrayList<>();

                        // Totals
                        View totalsContent = activity.pagerAdapter.mFragmentList.get(0).getView();
                        totalsContent.findViewById(R.id.top2_layout).setVisibility(View.GONE);
                        totalsContent.findViewById(R.id.top3_layout).setVisibility(View.GONE);
                        ((TextView)totalsContent.findViewById(R.id.kills)).setText(totals.getString("kills"));
                        ((TextView)totalsContent.findViewById(R.id.wins)).setText(totals.getString("wins"));
                        ((TextView)totalsContent.findViewById(R.id.matches_played)).setText(totals.getString("matchesplayed"));
                        int tMinutesPlayed = totals.getInt("minutesplayed");
                        int tHoursPlayed = tMinutesPlayed/60;
                        if (tHoursPlayed == 0)
                            ((TextView)totalsContent.findViewById(R.id.time_played)).setText(String.format(tMinutesPlayed + " min"));
                        else {
                            tMinutesPlayed %= tHoursPlayed;
                            ((TextView)totalsContent.findViewById(R.id.time_played)).setText(String.format(tHoursPlayed + " hr " + tMinutesPlayed + " min"));
                        }
                        ((TextView)totalsContent.findViewById(R.id.score)).setText(totals.getString("score"));
                        ((TextView)totalsContent.findViewById(R.id.winrate)).setText(totals.getString("winrate")+"%");
                        ((TextView)totalsContent.findViewById(R.id.kd)).setText(totals.getString("kd"));

                        String[] modeArray = new String[] { "solo", "duo", "squad"};
                        // Solo, Duo, Squad
                        for (int i = 0; i < modeArray.length; i++) {

                            View content = activity.pagerAdapter.mFragmentList.get(i+1).getView();
                            String mode = modeArray[i];
                            Log.d("GetPlayer", mode);

                            ((TextView)content.findViewById(R.id.kills)).setText(stats.getString("kills_"+mode));
                            ((TextView)content.findViewById(R.id.wins)).setText(stats.getString("placetop1_"+mode));

                            if (mode.equals("solo")) {
                                ((TextView)content.findViewById(R.id.top2)).setText(stats.getString("placetop10_" + mode));
                                ((TextView)content.findViewById(R.id.top3)).setText(stats.getString("placetop25_" + mode));
                            } else if (mode.equals("duo")) {
                                ((TextView)content.findViewById(R.id.top2)).setText(stats.getString("placetop5_" + mode));
                                ((TextView)content.findViewById(R.id.top2_label)).setText("Top 5");
                                ((TextView)content.findViewById(R.id.top3)).setText(stats.getString("placetop12_" + mode));
                                ((TextView)content.findViewById(R.id.top3_label)).setText("Top 12");
                            } else {
                                ((TextView)content.findViewById(R.id.top2)).setText(stats.getString("placetop3_" + mode));
                                ((TextView)content.findViewById(R.id.top2_label)).setText("Top 3");
                                ((TextView)content.findViewById(R.id.top3)).setText(stats.getString("placetop6_" + mode));
                                ((TextView)content.findViewById(R.id.top3_label)).setText("Top 6");
                            }

                            ((TextView)content.findViewById(R.id.matches_played)).setText(stats.getString("matchesplayed_"+mode));
                            ((TextView)content.findViewById(R.id.kd)).setText(stats.getString("kd_"+mode));
                            ((TextView)content.findViewById(R.id.winrate)).setText(stats.getString("winrate_"+mode)+"%");
                            ((TextView)content.findViewById(R.id.score)).setText(stats.getString("score_"+mode));
                            int minutesPlayed = stats.getInt("minutesplayed_"+mode);
                            int hoursPlayed = minutesPlayed/60;
                            if (hoursPlayed == 0)
                                ((TextView)content.findViewById(R.id.time_played)).setText(String.format(minutesPlayed + " min"));
                            else {
                                minutesPlayed %= hoursPlayed;
                                ((TextView)content.findViewById(R.id.time_played)).setText(String.format(hoursPlayed + " hr " + minutesPlayed + " min"));
                            }
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
