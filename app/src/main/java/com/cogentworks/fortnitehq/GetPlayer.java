package com.cogentworks.fortnitehq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPlayer extends GetData {

    private PlayerActivity mActivity;
    private Context mContext;

    public GetPlayer(final Context context, final String username, final String platform, final boolean alltime) {
        super(context);

        mContext = context;
        endpoint = "https://fortnite-public-api.theapinetwork.com/prod09/users/id";
        params.put("username", username);

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray platforms = jResponse.getJSONArray("platforms");

                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < platforms.length(); i++)
                        list.add(platforms.getString(i));
                    if (!list.contains(platform))
                        throw new Exception();

                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra(PlayerActivity.EXTRA_PLAYER_ID, jResponse.getString("uid"));
                    intent.putExtra(PlayerActivity.EXTRA_PLAYER_NAME, username);
                    intent.putExtra(PlayerActivity.EXTRA_PLAYER_PLATFORM, platform);
                    context.startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(mContext, "Player not found", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        };

    }

    public static class GetPlayerUid extends GetData {

        private PlayerActivity mActivity;
        private Context mContext;

        public GetPlayerUid(final Context context, final String username, final String platform) {
            super(context);

            mContext = context;
            final Activity activity = (Activity) context;

            endpoint = "https://fortnite-public-api.theapinetwork.com/prod09/users/id";
            params.put("username", username);

            responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jResponse = new JSONObject(response);
                        JSONArray platforms = jResponse.getJSONArray("platforms");

                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        sharedPrefs.edit().putString(HomeFragment.PREF_UID, jResponse.getString("uid")).apply();

                        sharedPrefs.edit().putString(HomeFragment.PREF_NAME, username).apply();
                        ((TextView) activity.findViewById(R.id.username)).setText(username);


                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < platforms.length(); i++)
                            list.add(platforms.getString(i));
                        if (!list.contains(platform))
                            throw new Exception();
                        else {
                            sharedPrefs.edit().putString(HomeFragment.PREF_PLATFORM, platform).apply();
                            ((TextView) activity.findViewById(R.id.platform)).setText(platform);
                        }

                        activity.findViewById(R.id.player_container).setVisibility(View.VISIBLE);
                        activity.findViewById(R.id.button_add).setVisibility(View.GONE);

                    } catch (WindowManager.BadTokenException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Player not found", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            };

        }
    }


}
