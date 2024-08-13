package com.example.loginapicalling.Filter;

import android.util.Log;
import android.widget.Filter;

import com.example.loginapicalling.Adapter.ContactAdapter;
import com.example.loginapicalling.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactFilter extends Filter {
    private final List<Contact> contactListFull; // List containing all contacts
    private final ContactAdapter contactAdapter; // Adapter to update with filtered results
    private static final String TAG = "ContactFragment1";

    public ContactFilter(List<Contact> contactListFull, ContactAdapter adapter) {
        this.contactListFull = contactListFull;
        this.contactAdapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        Log.d(TAG, "performFiltering called with constraint=" + constraint);
        List<Contact> filteredList = new ArrayList<>(); // List to hold filtered contacts

        try {
            if (constraint == null || constraint.length() == 0) {
                // If no constraint, return the full list
                filteredList.addAll(contactListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d(TAG, "performFiltering: Applying filter pattern=" + filterPattern);
                for (Contact contact : contactListFull) {
                    String name = contact.getName() != null ? contact.getName().toLowerCase() : "";
                    String phoneNumber = contact.getPhoneNumber() != null ? contact.getPhoneNumber().toLowerCase() : "";

                    // Check if the contact's name or phone number matches the filter pattern
                    if (name.contains(filterPattern) || phoneNumber.contains(filterPattern)) {
                        filteredList.add(contact);
                        Log.d(TAG, "performFiltering: Match found=" + contact.getName());
                    }
                }
            }

            FilterResults results = new FilterResults();
            Log.d(TAG,"results declared = "+results);
            results.values = filteredList; // Set the filtered list as results
            Log.d(TAG,"results.values is getting set = "+results.values+" "+filteredList);
            results.count = filteredList.size(); // Set the count of filtered results
            Log.d(TAG, String.valueOf(results.count));

            Log.d(TAG, "performFiltering: Filtered List=" + filteredList);
            return results;
        } catch (Exception e) {
            Log.e(TAG, "performFiltering: Exception occurred", e);
            return new FilterResults(); // Return empty results in case of exception
        }
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        Log.d(TAG, "publishResults: constraint=" + constraint);

        if (results.values instanceof List<?>) {
            // Safe casting with type check
            @SuppressWarnings("unchecked")
            List<Contact> filteredList = (List<Contact>) results.values;

            Log.d(TAG, "publishResults: Filtered list size=" + filteredList.size());
            contactAdapter.setContacts(filteredList); // Update adapter with filtered results
        } else {
            Log.d(TAG, "publishResults: Results.values is not a List or is null");
        }
    }
}
