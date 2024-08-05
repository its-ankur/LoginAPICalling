// SettingsFragment.java
package com.example.loginapicalling.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.loginapicalling.R;

// Fragment to display the 'Settings' section
public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Find and initialize the Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        // Find the TextView for the toolbar title
        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
        // Set the title of the toolbar
        toolbarTitle.setText("Settings");

        // Return the view for this fragment
        return view;
    }
}
