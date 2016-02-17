package com.demo.assignment.webservice;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Webservice {

    private Context context;
    private static RequestQueue mRequestQueue;

    public Webservice(Context context) {
        this.context = context;
    }

    public void makeRequest(String url, final ApiResponseInterface responseInterface) {

        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (responseInterface != null) {
                    responseInterface.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (responseInterface != null) {
                    responseInterface.onErrorResponse(error);
                }
            }
        });

        sendRequest(request);

    }

    private void sendRequest(JsonObjectRequest request) {
        final int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy
                .DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        request.setTag(request.toString());
        mRequestQueue.add(request);
    }
}
