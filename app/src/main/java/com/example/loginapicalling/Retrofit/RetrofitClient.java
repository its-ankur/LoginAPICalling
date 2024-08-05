// RetrofitClient.java
package com.example.loginapicalling.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Base URL for the API
    private static final String BASE_URL = "https://dummyjson.com/";

    // Single instance of Retrofit for the entire application
    private static Retrofit retrofit = null;

    /**
     * Gets the singleton instance of Retrofit.
     *
     * @return The Retrofit instance.
     */
    public static Retrofit getClient() {
        // Check if the Retrofit instance is not created
        if (retrofit == null) {
            // Create the Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Set the base URL for API requests
                    .addConverterFactory(GsonConverterFactory.create()) // Add the Gson converter for JSON
                    .build(); // Build the Retrofit instance
        }
        // Return the single instance of Retrofit
        return retrofit;
    }
}
