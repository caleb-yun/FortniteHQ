package com.cogentworks.forthq;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlayerFragment extends Fragment {


    public static final String PREF_UID = "player_uid";
    public static final String PREF_NAME = "player_name";
    public static final String PREF_PLATFORM = "player_platform";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uid = sharedPrefs.getString(PREF_UID, null);
        String username = sharedPrefs.getString(PREF_NAME, null);
        String platform = sharedPrefs.getString(PREF_PLATFORM, null);

        View view = inflater.inflate(R.layout.fragment_stats, null);

        if (username != null || platform != null) {
            if (uid == null) {
                new GetPlayer.GetPlayerUid(getContext(), username, platform).execute();
            } else {
                ((TextView) view.findViewById(R.id.username)).setText(username);
                ((TextView) view.findViewById(R.id.platform)).setText(platform);

            }
        } else {
            view.findViewById(R.id.button_add).setVisibility(View.VISIBLE);
            view.findViewById(R.id.player_container).setVisibility(View.GONE);
        }

        return view;
    }

}
