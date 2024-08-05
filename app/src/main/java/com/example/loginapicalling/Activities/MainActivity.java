package com.example.loginapicalling.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.loginapicalling.Fragments.UserDetailsFragment;
import com.example.loginapicalling.Retrofit.ApiService;
import com.example.loginapicalling.Retrofit.LoginRequest;
import com.example.loginapicalling.R;
import com.example.loginapicalling.Response.LoginResponse;
import com.example.loginapicalling.Response.UserResponse;
import com.example.loginapicalling.Retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// MainActivity handles user login and manages authentication
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "API123"; // Tag for logging
    private ApiService apiService; // API service instance
    private TextInputEditText emailInput, passwordInput; // Email and password input fields
    private Button loginButton; // Login button
    private SharedPreferences sharedPreferences; // SharedPreferences for storing the token
    private SharedPreferences.Editor editor; // SharedPreferences editor for modifying preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Disable night mode for this activity
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Set the layout for this activity
        setContentView(R.layout.activity_main);
        // Lock screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);

        // Log activity creation
        Log.d(TAG, "onCreate called");

        // Initialize views
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Initialize ApiService
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Check if a token exists and fetch user details if necessary
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            Log.d(TAG, "Token found, fetching user details...");
            fetchUserDetails("Bearer " + token);
        }

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(); // Handle login
            }
        });

        // Set touch listener for password field to toggle visibility
        passwordInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // Right drawable index
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if the touch is on the right drawable
                    if (event.getRawX() >= (passwordInput.getRight() - passwordInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle between showing and hiding the password
                        if (passwordInput.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            // Change the drawable to viewOn
                            passwordInput.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_crossed_eye, 0);
                        } else {
                            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            // Change the drawable to viewOff
                            passwordInput.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
                        }
                        // Move the cursor to the end of the text
                        passwordInput.setSelection(passwordInput.getText().length());
                        // Request focus on the EditText
                        passwordInput.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    // Fetch user details using the provided authorization header
    private void fetchUserDetails(String authHeader) {
        Log.d(TAG, "fetchUserDetails called");
        Call<UserResponse> detailsCall = apiService.getUserDetails(authHeader);
        detailsCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.d(TAG, "fetchUserDetails onResponse");
                if (response.isSuccessful()) {
                    UserResponse userDetails = response.body();
                    Log.d(TAG, "User Details: " + userDetails.toString());
                    // Start BottomNavigation activity if user details are fetched successfully
                    Intent intent = new Intent(MainActivity.this, BottomNavigation.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Failed to fetch user details: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch user details", t);
            }
        });
    }

    // Handle user login
    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Check for empty email or password
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create login request object
        LoginRequest loginRequest = new LoginRequest(email, password, 30);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "Login successful, token: " + loginResponse.getToken());
                    // Save token in SharedPreferences
                    editor.putString("token", loginResponse.getToken());
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Fetch user details with the newly obtained token
                    fetchUserDetails("Bearer " + loginResponse.getToken());
                } else {
                    Log.e(TAG, "Login failed: " + response.code());
                    Toast.makeText(MainActivity.this, "Login failed, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Login request failed", t);
                Toast.makeText(MainActivity.this, "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
        // Clear input fields
        emailInput.setText("");
        passwordInput.setText("");
    }
}
