package com.cogentworks.fortnitehq;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

public class GetPlayerStats extends GetData {

    private Context mContext;

    public GetPlayerStats(final PlayerActivity activity, String uid, String platform, boolean alltime) {

        super(activity);

        mContext = activity;
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

                    activity.findViewById(R.id.container).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.progress_bar).setVisibility(View.GONE);

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
