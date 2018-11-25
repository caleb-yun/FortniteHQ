package com.cogentworks.forthq;

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
        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
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
                params.put("TRN-Api-Key", "26a39dc9-31ff-45a3-84cb-6b1eedfbcdad");
                return params;
            }

            @Override
            protected Map<String,String> getParams() {
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
