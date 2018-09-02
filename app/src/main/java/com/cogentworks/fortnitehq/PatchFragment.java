package com.cogentworks.fortnitehq;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class PatchFragment extends Fragment {

    public ListView mListView;
    public ArrayList<NewsItem> listItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        mListView = view.findViewById(R.id.list);
        mListView.setAdapter(new PatchAdapter(view.getContext(), listItems));

        new GetPatch(view.getContext(), this).execute();
        return view;
    }

    public class PatchAdapter extends ArrayAdapter<NewsItem> {
        public PatchAdapter(Context context, ArrayList<NewsItem> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final NewsItem newsItem = getItem(position);

            if (newsItem == null)
                return null;

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_cell, parent, false);
            }

            // Lookup view for data population
            TextView title = convertView.findViewById(R.id.text1);
            TextView info = convertView.findViewById(R.id.text2);
            TextView timeText = convertView.findViewById(R.id.time);

            // Populate the data into the template view using the data object
            title.setText(newsItem.title);
            info.setText(newsItem.body);
            final Context context = convertView.getContext();
            final Resources resources = context.getResources();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(resources.getColor(R.color.colorPrimaryDark));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(newsItem.url));
                }
            });

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date = format.parse(newsItem.date);
                long diffInMs = (new java.util.Date()).getTime() - date.getTime();
                long hours = diffInMs/1000/60/60;
                if (hours == 0)
                    timeText.setText(Long.toString(diffInMs/1000/60) + " minutes ago");
                else if (hours < 24)
                    timeText.setText(Long.toString(hours) + " hours ago");
                else if (hours < 48)
                    timeText.setText("1 day ago");
                else
                    timeText.setText(Long.toString(hours/24) + " days ago");
            } catch (ParseException e) {
                e.printStackTrace();
            }




            Glide.with(convertView)
                    .load(newsItem.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into((ImageView) convertView.findViewById(R.id.thumbnail));

            // Return the completed view to render on screen
            return convertView;
        }

    }
}
