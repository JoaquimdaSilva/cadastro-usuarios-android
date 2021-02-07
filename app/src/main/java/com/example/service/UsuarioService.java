package com.example.service;

import android.os.Handler;

import com.example.model.Usuario;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.example.webservice.RestUtil;
import com.example.webservice.configuracoes.Configuracoes;
import com.example.webservice.util.RequestService;
import com.example.webservice.util.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UsuarioService {

    private android.os.Handler handler;
    private RequestService requestService;

    public UsuarioService(Handler handler, RequestService requestService) {
        this.handler = new Handler();
        this.requestService = new RequestService();
    }


    public UsuarioService(Handler handler) {
    }


    String PATH_CRIAR_OU_ATUALIZAR = "/usuarios";

    public void criarOuAtualizar(final Usuario entity, final Service.ServiceListener listener) {
        requestService = new RequestService();
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = RestUtil.objetoParaJson(entity);
                String url = Configuracoes.SERVER_URL + Configuracoes.PATH_API + PATH_CRIAR_OU_ATUALIZAR;
                String response = requestService.post(url, json);
                try {
                    if (response != null) {
                        // Decodifica caracteres
                        response = URLDecoder.decode(response, "UTF-8");

                        //final Usuario user = (Usuario)RestUtil.jsonParaObjeto(response, Usuario.class);
                        //if (user != null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onSucess(null);
                            }
                        });
                        return;
                        //}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onConnectionError();
                    }
                });
            }
        }).start();
    }

    String PATH_LISTAR_USUARIOS = "/usuarios/listar";

    public void todos(final Service.ServiceListener listener) {
        requestService = new RequestService();
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = requestService.get(Configuracoes.SERVER_URL + Configuracoes.PATH_API + PATH_LISTAR_USUARIOS);
                try {
                    if (response != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Usuario>>() {
                        }.getType();
                        List<Usuario> usuariosList = gson.fromJson(response, type);

                        if (usuariosList != null) {
                            handler.post(() -> listener.onSucess(usuariosList));
                            return;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onConnectionError();
                    }
                });
            }
        }).start();
    }

}
