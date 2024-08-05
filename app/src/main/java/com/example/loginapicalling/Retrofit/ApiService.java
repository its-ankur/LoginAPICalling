package com.example.loginapicalling.Retrofit;

import com.example.loginapicalling.Response.LoginResponse;
import com.example.loginapicalling.Response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-type: application/json")
    @POST("user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("auth/me")
    Call<UserResponse> getUserDetails(
            @Header("Authorization") String authHeader
    );
}
