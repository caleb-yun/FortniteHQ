package com.cogentworks.fnhq;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChallengesFragment extends Fragment {

    public ListView mListView;
    public ArrayList<ArrayList<ChallengeItem>> listItems = new ArrayList<>();
    public String season;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChallengesFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        mListView = view.findViewById(R.id.list);
        mListView.setAdapter(new ChallengesAdapter(view.getContext(), listItems));
        if (listItems.size() == 0)
            new GetChallenges(getContext(), this).execute();
        mListView.addHeaderView(getLayoutInflater().inflate(R.layout.title, null));
        TextView title = view.findViewById(R.id.title);
        if (season != null)
            title.setText("Season " + season);

        return view;
    }

    public class ChallengesAdapter extends ArrayAdapter<ArrayList<ChallengeItem>> {

        public ChallengesAdapter(Context context, ArrayList<ArrayList<ChallengeItem>> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ArrayList<ChallengeItem> items = getItem(position);

            if (items == null)
                return null;

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.week_cell, parent, false);
            }

            // Lookup view for data population
            TextView title = convertView.findViewById(R.id.week_name);
            LinearLayout list = convertView.findViewById(R.id.challenge_list);

            // Populate the data into the template view using the data object
            title.setText("Week " + (listItems.size() - position));
            Log.d("ChallengesFragment", "Children: " + list.getChildCount());
            if (list.getChildCount() == 0) {
                for (ChallengeItem item : items) {
                    View row = LinearLayout.inflate(getContext(), R.layout.challenge_cell, null);

                    TextView name = row.findViewById(R.id.text1);
                    TextView info = row.findViewById(R.id.text2);
                    TextView stars = row.findViewById(R.id.stars);

                    String difficulty = item.difficulty.equals("hard") ? String.format(" (%s)", item.difficulty.toUpperCase()) : "";
                    name.setText(item.name + difficulty);
                    info.setText(String.format("- / %d", item.total));
                    stars.setText("" + item.stars);

                    list.addView(row);
                }
            }

            // Return the completed view to render on screen
            return convertView;
        }

    }

}