package com.example.cadastrousuarios.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cadastrousuarios.R;
import com.example.model.Usuario;
import com.example.model.Util;
import com.example.service.UsuarioService;
import com.example.webservice.util.Service;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class CadastroUsuario extends AppCompatActivity {

    private EditText nome;
    private EditText dataNascimento;
    private ImageButton btnSlcImagem;
    private ImageButton btnSalvar;
    private TextWatcher dateWatcher;
    private Uri imageUri;
    private Usuario user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario);

        if (getIntent().hasExtra("usuario")) {
            user = (Usuario) getIntent().getSerializableExtra("usuario");
        }

        initComponentes();
        carregaUsuario();
    }

    public void carregaUsuario(){
        if(user != null && user.getId() > 0){
            nome.setText(user.getNome());
            dataNascimento.setText(user.getDataNascimento());
        }
    }

    public void limparCampos(){
        if(user != null && user.getId() > 0) {
            nome.setText(null);
            dataNascimento.setText(null);
        }
    }

    public void selecionarImagem(View v) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    private void salvarUsuario() {
        user = new Usuario();
        user.setNome(nome.getText().toString());
        user.setDataNascimento(dataNascimento.getText().toString());

        UsuarioService service = new UsuarioService(new Handler());
        service.criarOuAtualizar(user, new Service.ServiceListener() {
            @Override
            public void onSucess(Object response) {
                if (response instanceof Usuario) {
                    Toast.makeText(CadastroUsuario.this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    limparCampos();
                    finish();
                }
            }

            @Override
            public void onConnectionError() {

                Toast.makeText(CadastroUsuario.this,
                        "Não foi possivel conectar-se com o servidor", Toast.LENGTH_LONG)
                        .show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initComponentes() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String imputNome = nome.getText().toString().trim();
                String imputDate = dataNascimento.getText().toString().trim();

                btnSalvar.setEnabled(!imputNome.isEmpty() && !imputDate.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        nome = findViewById(R.id.edt_nome);
        dataNascimento = findViewById(R.id.editTextDate);

        btnSlcImagem = findViewById(R.id.slc_imagem);
        btnSalvar = findViewById(R.id.bt_salvar);
        Util.onClickShadow(btnSalvar);

        dataNascimento = (EditText) findViewById(R.id.editTextDate);
        dataNascimento.addTextChangedListener(tw);

        dateWatcher = Util.MaskEditTextDate.insert(null, dataNascimento);
        dataNascimento.addTextChangedListener(dateWatcher);
        dataNascimento.setInputType(InputType.TYPE_CLASS_NUMBER);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarUsuario();
            }
        });

    }


}
