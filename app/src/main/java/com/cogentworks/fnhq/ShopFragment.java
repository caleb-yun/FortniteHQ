package com.cogentworks.fnhq;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;

public class ShopFragment extends Fragment {

    ListView mListView;
    ArrayList<ShopItem> itemList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        mListView = view.findViewById(R.id.list);
        mListView.setAdapter(new ShopAdapter(view.getContext(), itemList));

        new GetShop(this).execute();
        return view;
    }


    public class ShopAdapter extends ArrayAdapter<ShopItem> {

        public ShopAdapter(Context context, ArrayList<ShopItem> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ShopItem shopItem = getItem(position);

            if (shopItem == null)
                return null;

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shop_cell, parent, false);


            if (shopItem.isTitle) {
                convertView.findViewById(R.id.card_view).setVisibility(View.GONE);
                TextView title = convertView.findViewById(R.id.title);
                title.setVisibility(View.VISIBLE);
                title.setText(shopItem.name);

            } else {
                TextView title = convertView.findViewById(R.id.text1);
                TextView description = convertView.findViewById(R.id.text2);
                ImageView image = convertView.findViewById(R.id.image1);

                title.setText(shopItem.name);
                description.setText(shopItem.cost);

                Glide.with(convertView)
                        .load(shopItem.imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(image);
            }

            return convertView;
        }
    }
}
