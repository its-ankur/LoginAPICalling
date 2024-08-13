package com.example.loginapicalling.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.loginapicalling.R;

public class InAppBrowserActivity extends AppCompatActivity {

    private WebView webView; // WebView for displaying web content

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view for this activity
        setContentView(R.layout.activity_in_app_browser);
        // Disable night mode for the activity
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Set the orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Initialize the WebView
        webView = findViewById(R.id.webView);
        // Set WebViewClient to handle web page navigation within the WebView
        webView.setWebViewClient(new WebViewClient());

        // Get the URL from the intent extras
        String url = getIntent().getStringExtra("url");
        // Load the URL if it's not null
        if (url != null) {
            webView.loadUrl(url);
        }
    }
}
