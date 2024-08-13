package com.example.loginapicalling.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapicalling.Filter.ContactFilter;
import com.example.loginapicalling.Model.Contact;
import com.example.loginapicalling.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements Filterable {
    private List<Contact> contactList; // List to hold all contacts
    private List<Contact> contactListFull; // List to hold filtered contacts
    private final Context context;
    private ContactFilter contactFilter; // Filter for searching contacts
    private static final String TAG = "ContactFragment1";

    public ContactAdapter(Context context) {
        this.context = context;
        contactList = new ArrayList<>();
        contactListFull = new ArrayList<>();
        contactFilter = new ContactFilter(contactListFull, this); // Initialize the filter
    }

    // Set contacts from a cursor and notify the adapter
    public void setContacts(Cursor cursor) {
        Log.d(TAG, "setContacts: setting new contacts");

        contactList.clear();
        contactListFull.clear();

        if (cursor != null) {
            Log.d(TAG, "setContacts: cursor is not null");

            // Iterate through the cursor to fetch contact details
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                // Fetch phone number for each contact
                String phoneNumber = "";
                Cursor phoneCursor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null
                );

                if (phoneCursor != null) {
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneCursor.close();
                }

                // Create a Contact object and add it to the lists
                Contact contact = new Contact(contactName, phoneNumber);
                contactList.add(contact);
                contactListFull.add(contact);

                Log.d(TAG, "setContacts: added contact=" + contactName + ", phone=" + phoneNumber);
            }
            cursor.close();
        } else {
            Log.d(TAG, "setContacts: cursor is null");
        }

        notifyDataSetChanged(); // Notify adapter about data changes
        Log.d(TAG, "setContacts: notifyDataSetChanged called");
    }

    // Set contacts from a List and notify the adapter
    public void setContacts(List<Contact> newContactList) {
        Log.d(TAG, "setContacts: setting new contacts");

        contactList.clear();
        contactList.addAll(newContactList);

        notifyDataSetChanged(); // Notify adapter about data changes
        Log.d(TAG, "setContacts: notifyDataSetChanged called");
    }

    @Override
    public Filter getFilter() {
        Log.d(TAG, "getFilter called");
        return contactFilter; // Return the contact filter for searching
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the contact item layout and return the view holder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Bind the contact data to the view holder
        Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhoneNumber());
        holder.initialTextView.setText(String.valueOf(contact.getName().charAt(0)).toUpperCase()); // Display the initial
    }

    @Override
    public int getItemCount() {
        return contactList.size(); // Return the number of items
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView initialTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the contact item layout
            nameTextView = itemView.findViewById(R.id.contact_name);
            phoneTextView = itemView.findViewById(R.id.contact_phone);
            initialTextView = itemView.findViewById(R.id.contact_initial);
        }
    }
}
