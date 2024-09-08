package com.proj.placementapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.proj.placementapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class displayPdf extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pdf);

        mViewPager = findViewById(R.id.viewPager);

        String url = getIntent().getStringExtra("url");
        new RetrievePdfStream().execute(url);
    }

    private class RetrievePdfStream extends AsyncTask<String, Void, ParcelFileDescriptor> {
        @Override
        protected ParcelFileDescriptor doInBackground(String... strings) {
            ParcelFileDescriptor fileDescriptor = null;
            try {
                File file = new File(getCacheDir(), "temp.pdf");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = new URL(strings[0]).openStream();

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileDescriptor;
        }

        @Override
        protected void onPostExecute(ParcelFileDescriptor fileDescriptor) {
            if (fileDescriptor != null) {
                // File found, proceed to display it
                try {
                    PdfPagerAdapter adapter = new PdfPagerAdapter(displayPdf.this, fileDescriptor);
                    mViewPager.setAdapter(adapter);
                } finally {
                    try {
                        if (fileDescriptor != null) {
                            fileDescriptor.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // File not found, display a message
                Toast.makeText(displayPdf.this, "Error: File not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
