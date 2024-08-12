// SettingsFragment.java
package com.example.loginapicalling.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
                showAboutUsDialog();
            }
        });

        // Return the view for this fragment
        return view;
    }

    private void showAboutUsDialog() {
        LayoutInflater inflater=getLayoutInflater();
        View dailogView=inflater.inflate(R.layout.dailog_layout,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setView(dailogView)
                .setCancelable(false);
        AlertDialog dialog=builder.create();
        TextView app = dailogView.findViewById(R.id.app_message);
        TextView main = dailogView.findViewById(R.id.main_message);
        TextView back = dailogView.findViewById(R.id.back_message);

        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInAppBrowser(APP_DEVELOPMENT_LINK);
                dialog.dismiss();
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExternalBrowser(MAIN_WEBSITE_LINK);
                dialog.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();
    }

    private void openInAppBrowser(String url) {
        Intent intent = new Intent(getActivity(), InAppBrowserActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void openExternalBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
