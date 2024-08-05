// MyVisitsFragment.java
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

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        TextView toolbarTitle=view.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Settings");
        return view;
    }
}
