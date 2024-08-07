// UserDetailsFragment.java
package com.example.loginapicalling.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.example.loginapicalling.Activities.MainActivity;
import com.example.loginapicalling.Retrofit.ApiService;
import com.example.loginapicalling.R;
import com.example.loginapicalling.Response.UserResponse;
import com.example.loginapicalling.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsFragment extends Fragment {

    // UI elements
    private TextView nameView, emailView, genderView, userName, name;
    private static final String TAG = "API"; // Tag for logging
    private Button logout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_show_details_page, container, false);

        // Initialize UI elements
        nameView = view.findViewById(R.id.nameView);
        emailView = view.findViewById(R.id.emailView);
        genderView = view.findViewById(R.id.genderView);
        userName = view.findViewById(R.id.userNameView);
        logout = view.findViewById(R.id.LogoutButton);
        name = view.findViewById(R.id.name);

        // Initialize SharedPreferences for storing user token
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Retrieve token from SharedPreferences and fetch user details if token exists
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            fetchUserDetails(token);
        }

        // Set up click listener for the logout button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog(); // Show confirmation dialog for logout
            }
        });

        return view;
    }

    // Method to show a confirmation dialog for logout
    private void showLogoutConfirmationDialog() {
        // Inflate the custom layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);

        // Create and configure the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView)
                .setCancelable(false); // Prevent dialog from closing by tapping outside

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Find and configure the dialog buttons
        Button positiveButton = dialogView.findViewById(R.id.dialog_positive_button);
        Button negativeButton = dialogView.findViewById(R.id.dialog_negative_button);

        // Handle the positive button click (confirm logout)
        positiveButton.setOnClickListener(v -> {
            logout(); // Perform logout
            dialog.dismiss(); // Close the dialog
        });

        // Handle the negative button click (cancel logout)
        negativeButton.setOnClickListener(v -> {
            dialog.dismiss(); // Close the dialog
        });

        // Show the dialog
        dialog.show();
    }

    // Method to handle the logout process
    private void logout() {
        // Log the logout event
        Log.d("Logout", "Logout button pressed");

        // Clear the SharedPreferences values
        editor.putString("token", "");
        boolean success = editor.commit();

        // Log the success of the commit operation
        Log.d("Logout", "SharedPreferences cleared: " + success);
        Toast.makeText(requireContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();

        // Start the MainActivity
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Close the current activity
    }

    // Method to fetch user details using the provided token
    private void fetchUserDetails(String token) {
        Log.d("Fetch", "Fetch user details called");
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.getUserDetails("Bearer " + token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    // Update UI with user details
                    String name1 = userResponse.getFirstName() + " " + userResponse.getLastName();
                    Log.d("Name", "Name = " + name1);
                    name.setText(name1);
                    nameView.setText(name1);
                    emailView.setText(userResponse.getEmail());
                    genderView.setText(userResponse.getGender());
                    userName.setText(userResponse.getUsername());
                } else {
                    Log.e(TAG, "Request failed with status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(TAG, "Network request failed", t);
            }
        });
    }
}
