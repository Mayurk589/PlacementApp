package com.proj.placementapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class UploadResumeActivity extends Activity {

    private static final int REQUEST_CODE_SELECT_FILE = 1;
    private static final MediaType MEDIA_TYPE_OCTET_STREAM = MediaType.parse("application/octet-stream");

    private Uri selectedFileUri;
    private TextView selectedFileTextView;
    private EditText usnEditText;
    private EditText phoneEditText;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resume);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phoneNumber");


        selectedFileTextView = findViewById(R.id.tv_selected_file);
        usnEditText = findViewById(R.id.et_usn);
        phoneEditText = findViewById(R.id.et_phone);

        phoneEditText.setText(phoneNumber);


        Button selectFileButton = findViewById(R.id.btn_select_file);
        Button uploadFileButton = findViewById(R.id.btn_upload_file);

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

        String usn = usnEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if (usn.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "USN and Phone Number are required", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                File tempFile = new File(getCacheDir(), getFileName(selectedFileUri));
                try (InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                     FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
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
                        .addFormDataPart("usn", usn)
                        .addFormDataPart("phone", phone)
                        .addFormDataPart("file", tempFile.getName(),
                                RequestBody.create(MEDIA_TYPE_OCTET_STREAM, tempFile));

                Request request = new Request.Builder()
                        .url(Config.uploadResume) // Replace with your server's endpoint
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
