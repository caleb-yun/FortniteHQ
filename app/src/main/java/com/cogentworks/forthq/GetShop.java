package com.cogentworks.forthq;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

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
                        String featuredUrl = currentEntry.getJSONObject("item").getJSONObject("images").getJSONObject("featured").getString("transparent");
                        int featured = currentEntry.getInt("featured");

                        ShopItem item = new ShopItem(name, cost, imageUrl, featuredUrl, featured);

                        //Log.d("GetShop", name + ": " + item.featured);
                        //Log.d("GetShop", featuredUrl);

                        if (i != 0 && results.get(results.size()-1).featured && !item.featured) {
                            results.add(new ShopItem("Daily Items"));
                            Log.d("GetShop", "Daily Items");
                        }

                        results.add(item);
                    }

                    mFragment.itemList.clear();
                    mFragment.itemList.addAll(results);
                    adapter.notifyDataSetChanged();

                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    hideProgressBar(mFragment);
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
