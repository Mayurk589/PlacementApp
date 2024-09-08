package com.proj.placementapp;
import android.app.Activity;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class ContactDetailsActivity extends Activity {

    private EditText etName, etPhone, etEmail, etAddress, etPermanentAddress, etTown, etUsn;
    private Button btnSubmit;
    private String url = Config.contactDetails;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phoneNumber");



        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etPermanentAddress = findViewById(R.id.etPermanentAddress);
        etTown = findViewById(R.id.etTown);
        etUsn = findViewById(R.id.etUsn);
        btnSubmit = findViewById(R.id.btnSubmit);


        etPhone.setText(phoneNumber);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer() {
        final String name = etName.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String permanentAddress = etPermanentAddress.getText().toString().trim();
        final String town = etTown.getText().toString().trim();
        final String usn = etUsn.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("phone", phone);
                params.put("email", email);
                params.put("address", address);
                params.put("permanent_address", permanentAddress);
                params.put("town", town);
                params.put("usn",usn);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
