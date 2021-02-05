package com.example.service;

import com.example.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("/listar")
    Call<List<Usuario>> getUsuarios();
}
