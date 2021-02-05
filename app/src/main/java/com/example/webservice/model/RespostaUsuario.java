package com.example.webservice.model;

import com.example.model.Usuario;

import java.io.Serializable;
import java.util.List;

public class RespostaUsuario implements Serializable {

    private List<Usuario> list;


    public List<Usuario> getList() {
        return list;
    }

    public void setList(List<Usuario> list) {
        this.list = list;
    }
}
