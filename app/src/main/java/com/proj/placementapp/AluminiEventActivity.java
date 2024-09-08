package com.proj.placementapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AluminiEventActivity extends Activity implements View.OnClickListener {

    EditText etEventName, etEventDate, etEventLocation, etEventDescription;
    Button btnCreateEvent;

    private static final String EVENT_URL = Config.addEvent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        etEventName = findViewById(R.id.etEventName);  // Corrected IDs
        etEventDate = findViewById(R.id.etEventDate);
        etEventLocation = findViewById(R.id.etEventLocation);
        etEventDescription = findViewById(R.id.etEventDescription);  // Corrected ID

        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String name = etEventName.getText().toString().trim();
        final String date = etEventDate.getText().toString().trim();
        final String desc = etEventDescription.getText().toString().trim();
        final String loc = etEventLocation.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || desc.isEmpty() || loc.isEmpty()) {
            // At least one field is empty
            Toast.makeText(AluminiEventActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, EVENT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    // Registration successful
                                    Toast.makeText(AluminiEventActivity.this, "Create event successful", Toast.LENGTH_SHORT).show();
                                    // Redirect to login activity or dashboard
                                } else {
                                    // Registration failed
                                    Toast.makeText(AluminiEventActivity.this, "Create event failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(AluminiEventActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            Toast.makeText(AluminiEventActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("date", date);
                    params.put("desc", desc);
                    params.put("loc", loc);
                    return params;
                }
            };
            // Add the request to the RequestQueue
            Volley.newRequestQueue(AluminiEventActivity.this).add(stringRequest);
        }
    }


}
