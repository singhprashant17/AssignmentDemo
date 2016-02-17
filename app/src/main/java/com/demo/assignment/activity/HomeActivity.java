package com.demo.assignment.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.demo.assignment.R;
import com.demo.assignment.adapter.DataListAdapter;
import com.demo.assignment.database.DatabaseHandler;
import com.demo.assignment.model.DataModel;
import com.demo.assignment.webservice.ApiResponseInterface;
import com.demo.assignment.webservice.Webservice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private final String URL = "http://private-21c80-care24androidtest.apiary-mock" +
            ".com/androidtest/getData";
    private RequestQueue mRequestQueue;
    private RecyclerView recyclerView;
    private DataListAdapter adapter;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        databaseHandler = new DatabaseHandler(this);
        initViews();
        getDataFromDatabase();
    }

    private void getDataFromDatabase() {
        List<DataModel> allItems = databaseHandler.getAllItems();
        if (allItems == null || allItems.isEmpty()) {
            return;
        }

        setAdapter(allItems);
    }

    private void setAdapter(List<DataModel> allItems) {
        adapter = new DataListAdapter(this, allItems);
        recyclerView.setAdapter(adapter);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataFromApi();
    }

    private void getDataFromApi() {
        new Webservice(this).makeRequest(URL, new ApiResponseInterface() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    Type type = new TypeToken<ArrayList<DataModel>>() {
                    }.getType();
                    List<DataModel> dataModelList = new Gson().fromJson(dataArray.toString(), type);
                    setAdapter(dataModelList);
                    databaseHandler.saveToDatabase(dataModelList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
