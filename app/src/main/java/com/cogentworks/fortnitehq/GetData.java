package com.cogentworks.fortnitehq;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public abstract class GetData extends AsyncTask<String, Void, Void> {

    private Context context;
    protected String endpoint;
    protected Response.Listener responseListener;
    protected Map<String, String> params;

    public GetData(Context context) {
        this.context = context;
        params = new HashMap<String, String>();
    }

    @Override
    protected Void doInBackground(String... taskParams) {

        RequestQueue queue = Volley.newRequestQueue(context);

        String url = endpoint;
        StringRequest request = new StringRequest(Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "58498a34ef21b56e7d7bffc610ab8ab8");
                return params;
            }

            @Override
            protected Map<String,String> getParams() {
                params.put("language", "en");
                return params;
            }
        };

        queue.add(request);
        return null;
    }

    protected void hideProgressBar(Fragment fragment) {
        fragment.getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

}
