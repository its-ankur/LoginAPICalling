package com.example.loginapicalling.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.loginapicalling.ApiService;
import com.example.loginapicalling.R;
import com.example.loginapicalling.Response.UserResponse;
import com.example.loginapicalling.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserDetailsActivity extends AppCompatActivity {

    private TextView nameView, emailView, genderView, countryView, dateOfBirthView,name,userName;
    private static final String TAG = "API";
    private Button logout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_details_page);
        Log.d("UserDetailsStarted","User details activity started");
        nameView = findViewById(R.id.nameView);
        name=findViewById(R.id.name);
        emailView=findViewById(R.id.emailView);
        genderView=findViewById(R.id.genderView);
        userName=findViewById(R.id.userNameView);
        logout=findViewById(R.id.LogoutButton);

        // Check for token and fetch user details if token exists
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        editor = sharedPreferences.edit();
        if (token != null) {
            fetchUserDetails(token);
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                showLogoutConfirmationDialog(v);
            }
        });
    }

    private void showLogoutConfirmationDialog(View v) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);

        // Create and configure the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setCancelable(false); // Prevent closing by tapping outside

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Find and configure the buttons
        Button positiveButton = dialogView.findViewById(R.id.dialog_positive_button);
        Button negativeButton = dialogView.findViewById(R.id.dialog_negative_button);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User clicked Yes button
                logout(v);
                dialog.dismiss(); // Close the dialog
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User cancelled the dialog
                dialog.dismiss(); // Close the dialog
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void logout(View v) {
        // Log the logout event
        Log.d("Logout", "Logout button pressed");

        // Clear the SharedPreferences values
        editor.putString("token", "");
        boolean success = editor.commit();

        // Log the success of the commit operation
        Log.d("Logout", "SharedPreferences cleared: " + success);
        Toast.makeText(UserDetailsActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
        // Start the LoginPage activity
        Intent intent = new Intent(UserDetailsActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void fetchUserDetails(String token) {
        Log.d("Fetch","Fetch user details called");
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<UserResponse> call = apiService.getUserDetails("Bearer " + token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    name.setText(userResponse.getFirstName()+" "+userResponse.getLastName());
                    nameView.setText(userResponse.getFirstName() + " " + userResponse.getLastName());
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
