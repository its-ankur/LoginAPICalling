package com.example.loginapicalling.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapicalling.Adapter.ContactAdapter;
import com.example.loginapicalling.R;

public class ContactFragment extends Fragment {

    private static final int REQUEST_READ_CONTACTS = 1; // Request code for reading contacts permission
    private static final String TAG = "ContactFragment1"; // Tag for logging
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private EditText searchBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Inflating layout");
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        recyclerView = view.findViewById(R.id.contacts_recycler_view); // Initialize RecyclerView
        searchBar = view.findViewById(R.id.search_bar); // Initialize SearchBar

        // Set up RecyclerView with LinearLayoutManager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactAdapter = new ContactAdapter(getContext());
        recyclerView.setAdapter(contactAdapter);

        // Check for READ_CONTACTS permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreateView: Permission not granted, requesting permission");
            // Request permission if not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            Log.d(TAG, "onCreateView: Permission already granted, loading contacts");
            // Load contacts if permission is already granted
            loadContacts();
        }

        // Set up TextWatcher to filter contacts based on search input
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s);
                // Apply filter to the contact list based on search input
                contactAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s);
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: requestCode=" + requestCode);

        if (requestCode == REQUEST_READ_CONTACTS) {
            // Check if the permission request was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: Permission granted, loading contacts");
                // Load contacts if permission granted
                loadContacts();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Permission denied");
                // Show toast if permission denied
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadContacts() {
        Log.d(TAG, "loadContacts: Loading contacts");
        ContentResolver contentResolver = getActivity().getContentResolver();
        // Query contacts from the content provider
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            Log.d(TAG, "loadContacts: Cursor count = " + cursor.getCount());
            if (cursor.getCount() > 0) {
                // Set contacts to adapter if any are found
                contactAdapter.setContacts(cursor);
            } else {
                Log.d(TAG, "loadContacts: No contacts found");
            }
        } else {
            Log.d(TAG, "loadContacts: Cursor is null");
        }
    }
}
