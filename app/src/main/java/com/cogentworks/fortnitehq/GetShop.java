package com.cogentworks.fortnitehq;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GetShop extends GetData {

    private ShopFragment mFragment;
    private Context mContext;

    public GetShop(final ShopFragment fragment) {
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
                    ArrayList<ShopItem> results = new ArrayList<>();

                    ShopFragment.ShopAdapter adapter = ((ShopFragment.ShopAdapter) mFragment.mListView.getAdapter());

                    results.add(new ShopItem("Featured Items"));

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject currentEntry = items.getJSONObject(i);

                        String name = currentEntry.getString("name");
                        String cost = currentEntry.getString("cost");
                        String imageUrl = currentEntry.getJSONObject("item").getJSONObject("images").getString("background");
                        int featured = currentEntry.getInt("featured");

                        ShopItem item = new ShopItem(name, cost, imageUrl, featured);

                        Log.d("GetShop", name + ": " + item.featured);

                        if (i != 0 && results.get(results.size()-1).featured && !item.featured) {
                            results.add(new ShopItem("Daily Items"));
                            Log.d("GetShop", "Daily Items");
                        }

                        results.add(item);
                    }

                    mFragment.itemList.clear();
                    mFragment.itemList.addAll(results);
                    adapter.notifyDataSetChanged();

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
