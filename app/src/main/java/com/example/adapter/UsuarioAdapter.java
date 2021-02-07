package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.example.cadastrousuarios.R;
import com.example.model.Usuario;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class UsuarioAdapter extends BaseAdapter {

    private List<Usuario> itens;
    private LayoutInflater inflater = null;
    private Context context;
    private ImageLoader imageLoader;

    public UsuarioAdapter(List<Usuario> itens, Context context) {
        this.itens = itens;
        this.context = context;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        // Cria uma instância do layout XML para os objetos correspondentes
        if(convertView == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_usuario, null);
        }

        Usuario u = itens.get(position);

        TextView txtNome = v.findViewById(R.id.nome_usuario);
        txtNome.setText(u.getNome());
        TextView txtCodigo = v.findViewById(R.id.codigo_usuario);
        txtCodigo.setText(u.getId().toString());
        TextView txtData = v.findViewById(R.id.data_nascimento);
        txtData.setText(u.getDataNascimento());
        ImageView imgAvatar = v.findViewById(R.id.imgAvatarList);


        if(u.getAvatar() != null && u.getAvatar().length() > 0){
            imgAvatar.setImageBitmap(Util.base64ToBitmap(u.getAvatar()));
        } else {
            imgAvatar.setImageResource(R.drawable.avatar);
        }

        return v;
    }
}