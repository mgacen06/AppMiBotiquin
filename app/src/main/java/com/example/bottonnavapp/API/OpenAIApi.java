package com.example.bottonnavapp.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    Call<OpenAIResponse> getChatResponse(@Body OpenAIRequest request);
}
