// ApiService.java
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

    /**
     * Method to log in a user.
     *
     * @param loginRequest The login credentials provided by the user.
     * @return A Call object for the API request to log in the user, which will return a LoginResponse.
     */
    @Headers("Content-type: application/json")
    @POST("user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    /**
     * Method to get the details of the authenticated user.
     *
     * @param authHeader The authorization header containing the Bearer token.
     * @return A Call object for the API request to fetch user details, which will return a UserResponse.
     */
    @GET("auth/me")
    Call<UserResponse> getUserDetails(
            @Header("Authorization") String authHeader
    );
}
