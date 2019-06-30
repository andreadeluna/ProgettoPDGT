package com.adeluna.letsorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.adeluna.letsorder.adapter.RistorantiAdapter;
import com.adeluna.letsorder.adapter.RistorantiDatiAdapter;
import com.adeluna.letsorder.model.Ristorante;
import com.adeluna.letsorder.model.RistoranteDati;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RistoranteDatiActivity extends AppCompatActivity {

    ArrayList<RistoranteDati> ristoranti;
    RistorantiDatiAdapter adapter;

    String nome;
    String luogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ristorante_dati);

        Intent intent = getIntent();
        nome = intent.getExtras().getString("nome");
        luogo = intent.getExtras().getString("luogo");

        nome = nome.replaceAll("\\s+","");
        luogo = luogo.replaceAll("\\s+","");

        ristoranti = new ArrayList<RistoranteDati>();
        ListView listView = (ListView) findViewById(R.id.lst_dati_rist);

        DownloadTask task = new DownloadTask();

        task.execute("https://letsorderapi.glitch.me/?tipo=diretto&lista=" + nome + "" + luogo + "");

        adapter = new RistorantiDatiAdapter(getApplicationContext(), R.layout.item_dati_ristorante, ristoranti);
        listView.setAdapter(adapter);



    }



    public void prenotazioneRist(View view){

        //finish();
        startActivity(new Intent(RistoranteDatiActivity.this, PrenotazioneActivity.class));

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1){
                    char cur = (char)data;
                    result += cur;
                    data = reader.read();
                }
                return result;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Log.i("JSONDemo", result);

            try {

                if(result != null){


                    JSONObject jsonObject = new JSONObject(result);

                    String ristoranti_str = jsonObject.getString("lista");

                    Log.i("LetsOrder", ristoranti_str);
                    JSONArray array = new JSONArray(ristoranti_str);


                    RistoranteDati rist = new RistoranteDati();


                    JSONObject jsonPart = array.getJSONObject(0);
                    String nomeRist = jsonPart.getString("nome");
                    String indirizzoRist = jsonPart.getString("indirizzo");
                    String postiRist = jsonPart.getString("posti liberi");
                    String aperturaRist = jsonPart.getString("apertura");
                    String valutazioneRist = jsonPart.getString("valutazione");
                    String numRist = jsonPart.getString("numtell");
                    String sitoRist = jsonPart.getString("sitoweb");


                    rist.setNomeRist(nomeRist);
                    rist.setIndirizzoRist(indirizzoRist);
                    rist.setPostiRist(postiRist);
                    rist.setAperturaRist(aperturaRist);
                    rist.setValutazioneRist(valutazioneRist);
                    rist.setNumRist(numRist);
                    rist.setSitoRist(sitoRist);


                    ristoranti.add(rist);

                    Log.i("LetsOrder", nomeRist
                            + " " + indirizzoRist + " "
                            + postiRist + " "
                            + aperturaRist+ " "
                            + valutazioneRist+ " "
                            + numRist+ " "
                            + sitoRist);


                }
                else{

                    Toast.makeText(RistoranteDatiActivity.this, "Errore API", Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            adapter.notifyDataSetChanged();
        }
    }

}
