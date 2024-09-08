package com.proj.placementapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlacementDetailsActivity extends Activity {

    private static final int REQUEST_CODE_SELECT_FILE = 1;
    private static final MediaType MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream");

    private Uri selectedFileUri;
    private TextView selectedFileTextView;
    EditText editTextPhone, editTextCompanyName, editTextRefNo, editTextInterviewDate, editTextJoiningDate,editTextSalary;
    RadioGroup radioGroupCampus;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placement_details);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phoneNumber");


        selectedFileTextView = findViewById(R.id.tv_selected_file);
        editTextPhone = findViewById(R.id.editPhone);
        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextRefNo = findViewById(R.id.editTextRefNo);
        editTextInterviewDate = findViewById(R.id.editTextInterviewDate);
        editTextJoiningDate = findViewById(R.id.editTextJoiningDate);
        radioGroupCampus = findViewById(R.id.radioGroupCampus);
        editTextSalary = findViewById(R.id.editTextSalary);

        editTextPhone.setText(phoneNumber);


        Button selectFileButton = findViewById(R.id.btn_SelectOffer_file);
        Button uploadFileButton = findViewById(R.id.btn_Submit);

        selectFileButton.setOnClickListener(v -> selectFile());
        uploadFileButton.setOnClickListener(v -> uploadFile());
    }

    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Support all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_FILE && resultCode == RESULT_OK) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                selectedFileTextView.setText(getFileName(selectedFileUri));
            }
        }
    }

    private String getFileName(Uri uri) {
        String path = uri.getPath();
        if (path != null) {
            int lastIndex = path.lastIndexOf('/');
            return (lastIndex != -1) ? path.substring(lastIndex + 1) : "unknown_file";
        }
        return "unknown_file";
    }

    public void uploadFile() {
        if (selectedFileUri == null) {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            return;
        }



        String phone = editTextPhone.getText().toString();
        String company = editTextCompanyName.getText().toString();
        String ref = editTextRefNo.getText().toString();
        String interdate = editTextInterviewDate.getText().toString();
        String joindate = editTextJoiningDate.getText().toString();
        RadioButton selectedRadioButton = findViewById(radioGroupCampus.getCheckedRadioButtonId());
        String campus = selectedRadioButton.getText().toString();

        String salary = editTextSalary.getText().toString();


        if (phone.isEmpty() || company.isEmpty() || ref.isEmpty() || interdate.isEmpty() || joindate.isEmpty()|| campus.isEmpty() || salary.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                File tempFile = new File(getCacheDir(), getFileName(selectedFileUri));
                try (InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                     FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while (true) {
                        assert inputStream != null;
                        if ((len = inputStream.read(buffer)) == -1) break;
                        outputStream.write(buffer, 0, len);
                    }
                }

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("phone", phone)
                        .addFormDataPart("company", company)
                        .addFormDataPart("ref", ref)
                        .addFormDataPart("interdate", interdate)
                        .addFormDataPart("joindate", joindate)
                        .addFormDataPart("selectedRadio",campus )
                        .addFormDataPart("salary", salary)

                        .addFormDataPart("file", tempFile.getName(),
                                RequestBody.create(MEDIA_TYPE_OCTET_STREAM, tempFile));

                Request request = new Request.Builder()
                        .url(Config.uploadOffer) // Replace with your server's endpoint
                        .post(multipartBuilder.build())
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
