package com.example.loginapicalling.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.loginapicalling.Activities.InAppBrowserActivity;
import com.example.loginapicalling.R;

// Fragment to display the 'Settings' section
public class SettingsFragment extends Fragment {

    // URLs for in-app and external browsing
    private static final String APP_DEVELOPMENT_LINK = "https://orientaloutsourcing.com/app-development/";
    private static final String MAIN_WEBSITE_LINK = "https://orientaloutsourcing.com/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Find and initialize the Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        // Set the title of the toolbar
        if (toolbar != null) {
            TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
            if (toolbarTitle != null) {
                toolbarTitle.setText("Settings");
            }
        }

        // Find and initialize the button
        RelativeLayout textContainer = view.findViewById(R.id.textContainer);
        textContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutUsDialog(); // Show the dialog when the button is clicked
            }
        });

        // Return the view for this fragment
        return view;
    }

    // Method to show the 'About Us' dialog
    private void showAboutUsDialog() {
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the dialog layout
        View dialogView = inflater.inflate(R.layout.dailog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView)
                .setCancelable(false); // Dialog cannot be dismissed by tapping outside
        AlertDialog dialog = builder.create();

        // Find and initialize dialog buttons
        TextView app = dialogView.findViewById(R.id.app_message);
        TextView main = dialogView.findViewById(R.id.main_message);
        TextView back = dialogView.findViewById(R.id.back_message);

        // Set up click listeners for each button
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInAppBrowser(APP_DEVELOPMENT_LINK); // Open URL in the app's internal browser
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalBrowser(MAIN_WEBSITE_LINK); // Open URL in the device's external browser
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Show the dialog
        dialog.show();
    }

    // Method to open a URL in the app's internal browser
    private void openInAppBrowser(String url) {
        Intent intent = new Intent(getActivity(), InAppBrowserActivity.class);
        intent.putExtra("url", url); // Pass the URL to the InAppBrowserActivity
        startActivity(intent);
    }

    // Method to open a URL in the device's external browser
    private void openExternalBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
