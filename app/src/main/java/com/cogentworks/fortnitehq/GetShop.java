package com.cogentworks.fortnitehq;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GetShop extends GetData {

    private ShopFragment mFragment;
    private Context mContext;

    public GetShop(ShopFragment fragment) {
        super(fragment.getContext());
        mContext = fragment.getContext();
        mFragment = fragment;
        endpoint = "https://fortnite-public-api.theapinetwork.com/prod09/store/get";

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jResponse = new JSONObject(response);
                    JSONArray items = jResponse.getJSONArray("items");
                    ArrayList<ArrayList<ShopItem>> results = new ArrayList<>();
                    TableLayout featuredTable = mFragment.getView().findViewById(R.id.table_featured);

                    for (int i = 0; i < items.length(); i+=3) {
                        ArrayList<ShopItem> row = new ArrayList<>();
                        TableRow tableRow = (TableRow) mFragment.getLayoutInflater().inflate(R.layout.shop_row, null);
                        featuredTable.addView(tableRow);

                        for (int j = 0; j < 3; j++) {
                            JSONObject currentEntry = items.getJSONObject(i+j);
                            String name = currentEntry.getString("name");
                            String cost = currentEntry.getString("cost");
                            String imageUrl = currentEntry.getJSONObject("item").getJSONObject("images").getString("background");
                            int featured = currentEntry.getInt("featured");

                            ImageView imageView = tableRow.findViewById(getResId("image"+(j+1), R.id.class));

                            if (imageUrl != null) {
                                Glide.with(mContext)
                                        .load(imageUrl)
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(imageView);
                            }

                            //row.add(new ShopItem(name, cost, imageUrl, featured));
                        }


                        //results.add(row);
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


    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
