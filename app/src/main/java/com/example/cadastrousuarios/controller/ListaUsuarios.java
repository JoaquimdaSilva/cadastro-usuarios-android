package com.example.cadastrousuarios.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.adapter.UsuarioAdapter;
import com.example.cadastrousuarios.R;
import com.example.model.Usuario;
import com.example.model.Util;
import com.example.service.UsuarioService;
import com.example.webservice.util.Service;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuarios extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ImageButton btnCadastro;

    private List<Usuario> listaUsuario;
    private RequestQueue mQueue;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_usuarios);
        getSupportActionBar().setTitle("Lista de usuários");

        mQueue = Volley.newRequestQueue(this);
        initComponentes();
        retornaUsuarios();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaUsuario = new ArrayList<>();
        retornaUsuarios();
    }

    private void retornaUsuarios() {
        UsuarioService service = new UsuarioService(new Handler());
        service.todos(new Service.ServiceListener() {
            @Override
            public void onSucess(Object response) {
                listaUsuario = (List<Usuario>) response;
                refreshListView();
            }

            @Override
            public void onConnectionError() {
                Toast.makeText(ListaUsuarios.this,
                        "Não foi possivel conectar-se com o servidor", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    private void refreshListView() {

        UsuarioAdapter adapter = new UsuarioAdapter(listaUsuario, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initComponentes() {
        listaUsuario = new ArrayList<>();
        listView = findViewById(R.id.lista_usuarios);
        btnCadastro = findViewById(R.id.btn_cadastro);
        Util.onClickShadow(btnCadastro);

    }

    public void abreCadastroUsuario(View v) {
        Intent i = new Intent(this, CadastroUsuario.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuario user = listaUsuario.get(position);
        Intent i = new Intent(this, CadastroUsuario.class);

        i.putExtra("usuario", user);
        startActivity(i);
    }
}