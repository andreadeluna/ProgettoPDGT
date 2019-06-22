package com.adeluna.letsorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adeluna.letsorder.R;
import com.adeluna.letsorder.model.Ristorante;

import java.util.ArrayList;

public class RistorantiAdapter  extends ArrayAdapter<Ristorante> {

    private ArrayList<Ristorante> ristoranti;
    private LayoutInflater inflater;
    private int Resource;
    Context context;

    public RistorantiAdapter(Context context, int resource, ArrayList<Ristorante> objects) {
        super(context, resource, objects);

        this.context = context;
        this.Resource = resource;
        this.ristoranti = objects;
        inflater = (LayoutInflater.from(context));


    }

    private class ViewHolder{
        TextView nome;
        TextView indirizzo;
        TextView apertura;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(Resource, null);
            viewHolder = new ViewHolder();
            viewHolder.nome = (TextView)convertView.findViewById(R.id.tv_nome);
            viewHolder.indirizzo = (TextView)convertView.findViewById(R.id.tv_indirizzo);
            viewHolder.apertura = (TextView)convertView.findViewById(R.id.tv_apertura);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.nome.setText(ristoranti.get(position).getNome());
        viewHolder.indirizzo.setText(ristoranti.get(position).getIndirizzo());
        viewHolder.apertura.setText(ristoranti.get(position).getApertura());


        return convertView;
    }

}
