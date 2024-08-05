package com.example.loginapicalling.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.loginapicalling.ApiService;
import com.example.loginapicalling.LoginRequest;
import com.example.loginapicalling.R;
import com.example.loginapicalling.Response.LoginResponse;
import com.example.loginapicalling.Response.UserResponse;
import com.example.loginapicalling.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "API123";
    private ApiService apiService;
    private TextInputEditText emailInput, passwordInput;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EdgeToEdge.enable(this);

        Log.d(TAG, "onCreate called");
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        apiService = RetrofitClient.getClient().create(ApiService.class);


        loginButton = findViewById(R.id.btnLogin);
        // Check if token exists and fetch user details if necessary
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            Log.d(TAG, "Token found, fetching user details...");
            fetchUserDetails("Bearer " + token);
        }
        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        passwordInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
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
                    Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
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

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password, 30);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "Login successful, token: " + loginResponse.getToken());
                    editor.putString("token", loginResponse.getToken());
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
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
        emailInput.setText("");
        passwordInput.setText("");
    }
}
