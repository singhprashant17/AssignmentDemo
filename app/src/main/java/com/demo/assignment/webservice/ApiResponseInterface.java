package com.demo.assignment.webservice;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ApiResponseInterface {
    void onResponse(JSONObject response);

    void onErrorResponse(VolleyError error);
}
