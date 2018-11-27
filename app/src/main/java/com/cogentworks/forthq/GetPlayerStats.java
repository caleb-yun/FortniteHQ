package com.cogentworks.forthq;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetPlayerStats extends GetData {

    private Context mContext;

    public GetPlayerStats(final PlayerActivity activity, String username, String platform) {

        super(activity);

        mContext = activity;
        endpoint = "https://api.fortnitetracker.com/v1/profile/" + platform.toLowerCase() + "/" + username;
        /*if (alltime)
            params.put("window", "alltime");
        else
            params.put("window", "current");*/

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONObject stats = jResponse.getJSONObject("stats");
                    JSONArray totals = jResponse.getJSONArray("lifeTimeStats");

                    // Totals
                    View totalsContent = activity.pagerAdapter.mFragmentList.get(0).getView();
                    totalsContent.findViewById(R.id.top2_layout).setVisibility(View.GONE);
                    totalsContent.findViewById(R.id.top3_layout).setVisibility(View.GONE);
                    ((TextView)totalsContent.findViewById(R.id.kills)).setText(totals.getJSONObject(10).getString("value"));
                    ((TextView)totalsContent.findViewById(R.id.wins)).setText(totals.getJSONObject(8).getString("value"));
                    ((TextView)totalsContent.findViewById(R.id.matches_played)).setText(totals.getJSONObject(7).getString("value"));
                    /*int tMinutesPlayed = totals.getInt("minutesplayed");
                    int tHoursPlayed = tMinutesPlayed/60;
                    if (tHoursPlayed == 0)
                        ((TextView)totalsContent.findViewById(R.id.time_played)).setText(String.format(tMinutesPlayed + " min"));
                    else {
                        tMinutesPlayed %= tHoursPlayed;
                        ((TextView)totalsContent.findViewById(R.id.time_played)).setText(String.format(tHoursPlayed + " hr " + tMinutesPlayed + " min"));
                    }*/
                    ((TextView)totalsContent.findViewById(R.id.score)).setText(totals.getJSONObject(6).getString("value"));
                    ((TextView)totalsContent.findViewById(R.id.winrate)).setText(totals.getJSONObject(9).getString("value"));
                    ((TextView)totalsContent.findViewById(R.id.kd)).setText(totals.getJSONObject(11).getString("value"));

                    //String[] modeArray = new String[] { "solo", "duo", "squad"};
                    // Solo, Duo, Squad
                    for (int i = 0; i < 3; i++) {

                        View content = activity.pagerAdapter.mFragmentList.get(i+1).getView();
                        JSONObject mode = stats.getJSONObject(stats.names().getString(i));

                        ((TextView)content.findViewById(R.id.kills)).setText(mode.getJSONObject("kills").getString("value"));
                        ((TextView)content.findViewById(R.id.wins)).setText(mode.getJSONObject("top1").getString("value"));
                        String section = stats.names().getString(i);
                        if (section.equals("p2")) { // SOLO
                            ((TextView)content.findViewById(R.id.top2)).setText(mode.getJSONObject("top10").getString("value"));
                            ((TextView)content.findViewById(R.id.top3)).setText(mode.getJSONObject("top25").getString("value"));
                        } else if (section.equals("p10")) { // DUO
                            ((TextView)content.findViewById(R.id.top2)).setText(mode.getJSONObject("top5").getString("value"));
                            ((TextView)content.findViewById(R.id.top2_label)).setText("Top 5");
                            ((TextView)content.findViewById(R.id.top3)).setText(mode.getJSONObject("top12").getString("value"));
                            ((TextView)content.findViewById(R.id.top3_label)).setText("Top 12");
                        } else {
                            ((TextView)content.findViewById(R.id.top2)).setText(mode.getJSONObject("top3").getString("value"));
                            ((TextView)content.findViewById(R.id.top2_label)).setText("Top 3");
                            ((TextView)content.findViewById(R.id.top3)).setText(mode.getJSONObject("top6").getString("value"));
                            ((TextView)content.findViewById(R.id.top3_label)).setText("Top 6");
                        }

                        ((TextView)content.findViewById(R.id.matches_played)).setText(mode.getJSONObject("matches").getString("value"));
                        ((TextView)content.findViewById(R.id.kd)).setText(mode.getJSONObject("kd").getString("value"));
                        ((TextView)content.findViewById(R.id.winrate)).setText(mode.getJSONObject("winRatio").getString("value")+"%");
                        ((TextView)content.findViewById(R.id.score)).setText(mode.getJSONObject("score").getString("value"));
                        /*int minutesPlayed = stats.getInt("minutesplayed_"+mode);
                        int hoursPlayed = minutesPlayed/60;
                        if (hoursPlayed == 0)
                            ((TextView)content.findViewById(R.id.time_played)).setText(String.format(minutesPlayed + " min"));
                        else {
                            minutesPlayed %= hoursPlayed;
                            ((TextView)content.findViewById(R.id.time_played)).setText(String.format(hoursPlayed + " hr " + minutesPlayed + " min"));
                        }*/
                    }

                    activity.findViewById(R.id.container).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.progress_bar).setVisibility(View.GONE);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };
    }
}
