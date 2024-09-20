package com.example.lab3.services;

import com.example.lab3.others.LoginRequest;
import com.example.lab3.others.Usuario;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface LogueoService {

    @POST("auth/login")
    Call<Usuario> login(@Body LoginRequest loginRequest);

}
