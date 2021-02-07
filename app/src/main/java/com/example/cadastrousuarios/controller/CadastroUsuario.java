package com.example.cadastrousuarios.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cadastrousuarios.R;
import com.example.model.Usuario;
import com.example.model.Util;
import com.example.service.UsuarioService;
import com.example.webservice.util.Service;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class CadastroUsuario extends AppCompatActivity {

    private EditText nome;
    private EditText dataNascimento;
    private ImageButton btnSlcImagem;
    private ImageButton btnSalvar;
    private TextWatcher dateWatcher;
    private ProgressBar mProgressBar;
    private Uri imageUri;
    private ImageView imgUsuario;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Usuario user;
    private boolean validado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario);
        FirebaseApp.initializeApp(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_slc_imagem);

        if (getIntent().hasExtra("usuario")) {
            user = (Usuario) getIntent().getSerializableExtra("usuario");
        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initComponentes();
        carregaUsuario();
    }

    public void carregaUsuario() {
        if (user != null && user.getId() > 0) {
            nome.setText(user.getNome());
            dataNascimento.setText(user.getDataNascimento());
            recuperaImagem(user);
        }
    }

    public void selecionarImagem(View v) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    private void salvarUsuario() {
        if (validaCampos()) {

            exibirProgress(true);

            if (user.getId() != null && user.getId() > 0) {
                user.setId(user.getId());
            } else {
                user = new Usuario();
            }

            user.setNome(nome.getText().toString());
            user.setDataNascimento(dataNascimento.getText().toString());
            if (user.getId() != null && user.getId() > 0) {
                user.setId(user.getId());
            }

            if (Util.imageToBase64(this.getBitmapAvatar()).length() > 0) {
                user.setAvatar(Util.imageToBase64(this.getBitmapAvatar()));
            } else {
                user.setAvatar(null);
            }

            UsuarioService service = new UsuarioService(new Handler());
            service.criarOuAtualizar(user, new Service.ServiceListener() {
                @Override
                public void onSucess(Object response) {
                    Toast.makeText(CadastroUsuario.this,
                            "Usuario salvo com sucesso!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnectionError() {
                    Toast.makeText(CadastroUsuario.this,
                            "Verifique sua conexao e tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        exibirProgress(false);
        fecharCadastro();
    }

    private Bitmap getBitmapAvatar() {
        Bitmap bitmap;
        if (imgUsuario.getDrawable() instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) imgUsuario.getDrawable()).getBitmap();
        } else {
            Drawable d = imgUsuario.getDrawable();
            bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            d.draw(canvas);
        }

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgUsuario.setImageURI(imageUri);
        }
    }

    private void recuperaImagem(Usuario usuario) {
        if (usuario.getAvatar() != null && usuario.getAvatar().length() > 0) {
            imgUsuario.setImageBitmap(Util.base64ToBitmap(usuario.getAvatar()));
        } else {
            imgUsuario.setImageResource(R.drawable.avatar);
        }
    }

    public boolean validaCampos() {
        if (nome.getText().length() <= 0) {
            Toast.makeText(CadastroUsuario.this,
                    "Verifique o campo nome.", Toast.LENGTH_LONG).show();
            return validado = false;
        } else if (dataNascimento.getText().length() < 8) {
            Toast.makeText(CadastroUsuario.this,
                    "Verifique o campo data de nascimento", Toast.LENGTH_LONG).show();
            return validado = false;
        }

        return validado = true;
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

        imgUsuario = findViewById(R.id.img_user);
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

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarUsuario();
            }
        });

    }

    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public void fecharCadastro() {
        finish();
    }


}
