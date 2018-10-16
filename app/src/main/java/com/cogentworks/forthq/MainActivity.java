package com.cogentworks.forthq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Fragment currentFragment = null;

    Fragment newsFragment = null;
    Fragment statsFragment = null;
    Fragment shopFragment = null;
    Fragment challengesFragment = null;

    public final static String PREF_DARK_THEME = "dark_theme";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_news:
                    if (newsFragment == null) {
                        newsFragment = new NewsFragment();
                        return loadFragment(newsFragment, true);
                    }
                    return loadFragment(newsFragment);

                case R.id.navigation_stats:
                    if (statsFragment == null) {
                        statsFragment = new PlayerFragment();
                        return loadFragment(statsFragment, true);
                    }
                    return loadFragment(statsFragment);

                case R.id.navigation_shop:
                    if (shopFragment == null) {
                        shopFragment = new ShopFragment();
                        return loadFragment(shopFragment, true);
                    }
                    return loadFragment(shopFragment);

                case R.id.navigation_challenges:
                    if (challengesFragment == null) {
                        challengesFragment = new ChallengesFragment();
                        return loadFragment(challengesFragment, true);
                    }
                    return loadFragment(challengesFragment);

                default:
                    return false;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = sharedPrefs.getBoolean(PREF_DARK_THEME, false);
        if (useDarkTheme)
            setTheme(R.style.AppDarkTheme);

        setContentView(R.layout.activity_main);

        newsFragment = new NewsFragment();
        currentFragment = newsFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, newsFragment)
                .commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private boolean loadFragment(Fragment fragment) {
        return loadFragment(fragment, false);
    }

    private boolean loadFragment(Fragment fragment, boolean add) {
        if (fragment != null) {
            if (add) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .add(R.id.fragment_container, fragment)
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .show(fragment)
                        .commit();
            }
            currentFragment = fragment;
            return true;
        }

        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public static final int REQUEST_EXIT = 2;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.dark_check);
        boolean isDark = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getBoolean(PREF_DARK_THEME, false);
        checkable.setChecked(isDark);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dark_check:
                boolean isChecked = !item.isChecked();
                item.setChecked(isChecked);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(PREF_DARK_THEME, isChecked);
                editor.commit();

                Intent intent = getIntent();
                finish();
                startActivity(intent);

                return true;
            default:
                return false;
        }
    }


    public void onMoreClick(View v) {
        Intent intent = new Intent(this, PlayerActivity.class);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        intent.putExtra(PlayerActivity.EXTRA_PLAYER_ID, sharedPrefs.getString(PlayerFragment.PREF_UID, null));
        intent.putExtra(PlayerActivity.EXTRA_PLAYER_PLATFORM, sharedPrefs.getString(PlayerFragment.PREF_PLATFORM, null));
        intent.putExtra(PlayerActivity.EXTRA_PLAYER_NAME, sharedPrefs.getString(PlayerFragment.PREF_NAME, null));
        startActivity(intent);
    }

    public void onAddPlayer(View v) {
        final Context context = this;
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_configure, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Player")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = dialogView.findViewById(R.id.config_username);
                        String username = String.valueOf(editText.getText());
                        Spinner platformSpinner = dialogView.findViewById(R.id.spinner_platform);
                        String platform = platformSpinner.getSelectedItem().toString();

                        new GetPlayer.GetPlayerUid(context, username, platform.toLowerCase()).execute();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        Spinner regionSpinner = dialogView.findViewById(R.id.spinner_platform);
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this, R.array.array_platform, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);
    }

    public void onRemovePlayer(View v) {
        final Activity activity = this;
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_configure, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Remove Player")
                .setMessage("Are you sure?")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                        sharedPrefs.edit().remove(PlayerFragment.PREF_UID).apply();
                        sharedPrefs.edit().remove(PlayerFragment.PREF_NAME).apply();
                        sharedPrefs.edit().remove(PlayerFragment.PREF_PLATFORM).apply();

                        activity.findViewById(R.id.player_container).setVisibility(View.GONE);
                        activity.findViewById(R.id.button_add).setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        Spinner regionSpinner = dialogView.findViewById(R.id.spinner_platform);
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this, R.array.array_platform, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);
    }

    public void onSearchPlayer(View v) {

        final Context context = this;
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_configure, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Search")
                .setView(dialogView)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = dialogView.findViewById(R.id.config_username);
                        String username = String.valueOf(editText.getText());
                        Spinner platformSpinner = dialogView.findViewById(R.id.spinner_platform);
                        String platform = platformSpinner.getSelectedItem().toString();

                        new GetPlayer(context, username, platform.toLowerCase(), true).execute();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

        Spinner regionSpinner = dialogView.findViewById(R.id.spinner_platform);
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this, R.array.array_platform, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);


    }

}
