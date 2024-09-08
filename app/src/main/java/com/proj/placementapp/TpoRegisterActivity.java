package com.proj.placementapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TpoRegisterActivity extends Activity {

    private EditText etTPORegName, etTPORegPhone, etTPORegPassword, etTPORegEmail;
    private Button btnTPORegRegister;
    private static final String REGISTER_URL = "http://"+Config.ipAddress+"/PlacementApp/register_tpo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tporegister);

        // Initialize EditText and Button
        etTPORegName = findViewById(R.id.etTPORegName);
        etTPORegPhone = findViewById(R.id.etTPORegPhone);
        etTPORegPassword = findViewById(R.id.etTPORegPassword);
        etTPORegEmail = findViewById(R.id.etTPORegEmail);
        btnTPORegRegister = findViewById(R.id.btnTPORegRegister);

        // Set OnClickListener for Register Button
        btnTPORegRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform registration
                final String name = etTPORegName.getText().toString().trim();
                final String phone = etTPORegPhone.getText().toString().trim();
                final String password = etTPORegPassword.getText().toString().trim();
                final String email = etTPORegEmail.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        // Registration successful
                                        Toast.makeText(TpoRegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        // Redirect to login activity or dashboard
                                    } else {
                                        // Registration failed
                                        Toast.makeText(TpoRegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(TpoRegisterActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors
                                Toast.makeText(TpoRegisterActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name);
                        params.put("phone", phone);
                        params.put("password", password);
                        params.put("email", email);
                        return params;
                    }
                };

                // Add the request to the RequestQueue
                Volley.newRequestQueue(TpoRegisterActivity.this).add(stringRequest);
            }
        });
    }
}
