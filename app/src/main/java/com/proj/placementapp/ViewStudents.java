package com.proj.placementapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewStudents extends Activity {

    ViewStudentsAdapter adapter;
    ListView lstView;
    EditText etSearchQuery;
    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<HashMap<String, String>> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_students);

        lstView = findViewById(R.id.listStudents);
        etSearchQuery = findViewById(R.id.etSearchQuery);
        arraylist = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Set up search functionality
        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        getProperties();
    }

    private void getProperties() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.getAllStudents;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        arraylist.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", jsonObject.getString("id"));
                                map.put("name", jsonObject.getString("name"));
                                map.put("phone", jsonObject.getString("phone"));
                                map.put("password", jsonObject.getString("password"));
                                map.put("email", jsonObject.getString("email"));
                                map.put("place", jsonObject.getString("place"));
                                map.put("usn", jsonObject.getString("usn"));
                                arraylist.add(map);
                            }
                            filteredList.addAll(arraylist); // Initially, filtered list is same as array list
                            adapter = new ViewStudentsAdapter(ViewStudents.this, filteredList);
                            lstView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void filter(String query) {
        filteredList.clear();
        for (HashMap<String, String> item : arraylist) {
            if (item.get("usn").toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
