package com.proj.placementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EducationalDetailsActivity extends Activity {

    private EditText etTenthScore;
    private EditText etTwelthScore;
    private EditText etCGPL;
    private EditText etOtherCertificationPrograms;
    private EditText etOtherSkill;

    String phoneNumber;

    EditText etPhoneno,etUsn;
    private final String url = Config.educationalDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.educational_details);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phoneNumber");

         etPhoneno = findViewById(R.id.etPhoneno);
        etPhoneno.setText(phoneNumber);

         etUsn = findViewById(R.id.etUsn);
        etTenthScore = findViewById(R.id.etTenthScore);
        etTwelthScore = findViewById(R.id.etTwelthScore);
        etCGPL = findViewById(R.id.etCGPL);
        etOtherCertificationPrograms = findViewById(R.id.etOtherCertificationPrograms);
        etOtherSkill = findViewById(R.id.etOtherSkill);



        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> sendDataToServer());
    }

    private void sendDataToServer() {
        final String Phoneno = etPhoneno.getText().toString().trim();
        final String Usn = etUsn.getText().toString().trim();
        final String tenthScore = etTenthScore.getText().toString().trim();
        final String twelthScore = etTwelthScore.getText().toString().trim();
        final String cgpl = etCGPL.getText().toString().trim();
        final String otherCertificationPrograms = etOtherCertificationPrograms.getText().toString().trim();
        final String otherSkill = etOtherSkill.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Phoneno", Phoneno);
                params.put("Usn", Usn);
                params.put("tenthScore", tenthScore);
                params.put("twelthScore", twelthScore);
                params.put("cgpl", cgpl);
                params.put("otherCertificationPrograms", otherCertificationPrograms);
                params.put("otherSkill", otherSkill);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
