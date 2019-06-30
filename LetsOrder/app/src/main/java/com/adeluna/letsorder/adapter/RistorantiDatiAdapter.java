package com.adeluna.letsorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adeluna.letsorder.R;
import com.adeluna.letsorder.model.RistoranteDati;

import java.util.ArrayList;

public class RistorantiDatiAdapter extends ArrayAdapter<RistoranteDati> {

    private ArrayList<RistoranteDati> ristoranti;
    private LayoutInflater inflater;
    private int Resource;
    Context context;

    public RistorantiDatiAdapter(Context context, int resource, ArrayList<RistoranteDati> objects) {
        super(context, resource, objects);

        this.context = context;
        this.Resource = resource;
        this.ristoranti = objects;
        inflater = (LayoutInflater.from(context));


    }

    private class ViewHolder{
        TextView nome;
        TextView indirizzo;
        TextView posti_liberi;
        TextView apertura;
        TextView valutazione;
        TextView numtel;
        TextView sitoweb;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RistorantiDatiAdapter.ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(Resource, null);
            viewHolder = new RistorantiDatiAdapter.ViewHolder();
            viewHolder.nome = (TextView)convertView.findViewById(R.id.tv_nome_rist);
            viewHolder.indirizzo = (TextView)convertView.findViewById(R.id.tv_indirizzo_rist);
            viewHolder.posti_liberi = (TextView)convertView.findViewById(R.id.tv_posti_rist);
            viewHolder.apertura = (TextView)convertView.findViewById(R.id.tv_apertura_rist);
            viewHolder.valutazione = (TextView)convertView.findViewById(R.id.tv_valutazione_rist);
            viewHolder.numtel = (TextView)convertView.findViewById(R.id.tv_numero_rist);
            viewHolder.sitoweb = (TextView)convertView.findViewById(R.id.tv_sito_rist);


            convertView.setTag(viewHolder);
        }else{
            viewHolder = (RistorantiDatiAdapter.ViewHolder)convertView.getTag();
        }

        viewHolder.nome.setText(ristoranti.get(position).getNomeRist());
        viewHolder.indirizzo.setText(ristoranti.get(position).getIndirizzoRist());
        viewHolder.posti_liberi.setText(ristoranti.get(position).getPostiRist());
        viewHolder.apertura.setText(ristoranti.get(position).getAperturaRist());
        viewHolder.valutazione.setText(ristoranti.get(position).getValutazioneRist());
        viewHolder.numtel.setText(ristoranti.get(position).getNumRist());
        viewHolder.sitoweb.setText(ristoranti.get(position).getSitoRist());


        return convertView;
    }


}
