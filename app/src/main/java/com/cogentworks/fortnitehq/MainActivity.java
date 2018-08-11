package com.cogentworks.fortnitehq;

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

public class MainActivity extends AppCompatActivity {

    Fragment newsFragment = null;
    Fragment statsFragment = null;
    Fragment shopFragment = null;
    Fragment challengesFragment = null;

    private final static String PREF_DARK_THEME = "dark_theme";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_news:
                    newsFragment = newsFragment == null ? new NewsFragment() : newsFragment;
                    return loadFragment(newsFragment);

                case R.id.navigation_stats:
                    statsFragment = statsFragment == null ? new HomeFragment() : statsFragment;
                    return loadFragment(statsFragment);

                case R.id.navigation_shop:
                    shopFragment = shopFragment == null ? new ShopFragment() : shopFragment;
                    return loadFragment(shopFragment);

                case R.id.navigation_challenges:
                    challengesFragment = challengesFragment == null ? new ChallengesFragment() : challengesFragment;
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

        //newsFragment = new NewsFragment();
        statsFragment = new HomeFragment();
        loadFragment(statsFragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
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

    // TESTING
    public void onButtonClick(View v) {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }


}
