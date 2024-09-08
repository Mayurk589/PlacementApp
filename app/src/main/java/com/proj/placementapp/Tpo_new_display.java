package com.proj.placementapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Tpo_new_display extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpo_new_display);

        webView = findViewById(R.id.webView);

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Allow file access
        webSettings.setAllowFileAccess(true);

        // Allow file access from file URLs
        webSettings.setAllowFileAccessFromFileURLs(true);

        // Allow universal access from file URLs
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        // Enable DOM Storage
        webSettings.setDomStorageEnabled(true);

        // Load the website
        webView.loadUrl("https://iseplacementapp.000webhostapp.com/webplace/home.php");

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());

        // Handle JavaScript alerts, confirmations, and prompts
        webView.setWebChromeClient(new WebChromeClient());

        // Add a JavaScript interface to communicate between Java and JavaScript
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        // Set a DownloadListener to handle file downloads
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // Request permissions if not already granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Tpo_new_display.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Tpo_new_display.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        return;
                    }
                }

                // Start the download
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));

                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

                Toast.makeText(Tpo_new_display.this, "Downloading File", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Define a Java class to handle JavaScript calls
    public class WebAppInterface {
        @JavascriptInterface
        public void showToast(String message) {
            Toast.makeText(Tpo_new_display.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    // Call this method from your activity when you want to print
    public void printPage() {
        webView.loadUrl("javascript:printPage()");
    }

    @Override
    public void onBackPressed() {
        // Check if WebView can navigate back
        if (webView.canGoBack()) {
            webView.goBack(); // Go back within the WebView
        } else {
            super.onBackPressed(); // Otherwise, perform default back button behavior
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can do the download now
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
