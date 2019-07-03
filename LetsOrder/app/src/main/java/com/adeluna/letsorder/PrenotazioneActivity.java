package com.adeluna.letsorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrenotazioneActivity extends AppCompatActivity {

    private static final String TAG = "inviaPrenotazione";

    FirebaseDatabase mDatabase;

    // UI
    EditText etNome;
    EditText etOra;
    EditText etPrimo;
    EditText etSecondo;
    EditText etContorno;

    String nome;
    String luogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotazione);

        Intent intent = getIntent();
        nome = intent.getExtras().getString("nome");
        luogo = intent.getExtras().getString("luogo");

        nome = nome.replaceAll("\\s+","");
        luogo = luogo.replaceAll("\\s+","");


        initUI();
        initFirebase();

        checkNetworkConnection();

    }


    private void initUI() {

        etNome = (EditText)findViewById(R.id.et_nome_ord);
        etOra = (EditText)findViewById(R.id.et_ora_ord);
        etPrimo = (EditText)findViewById(R.id.et_primo_ord);
        etSecondo = (EditText)findViewById(R.id.et_secondo_ord);
        etContorno = (EditText)findViewById(R.id.et_contorno_ord);

    }

    private void initFirebase() {

        mDatabase = FirebaseDatabase.getInstance();

    }


    // check network connection
    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // show "Connected" & type of network "WIFI or MOBILE"
            Log.i("Connessione","Connesso");



        } else {
            // show "Not Connected"
            Log.i("Connessione","Non connesso");
        }

        return isConnected;
    }



    private String httpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }


    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return httpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i("OnPostExecute", result);
        }
    }


    public void send(View view) {

        Toast.makeText(this, "Ordinazione presa in carico", Toast.LENGTH_SHORT).show();

        DatabaseReference myRef = mDatabase.getReference();

        myRef.child("Utenti").child(etNome.getText().toString()).child("nome").setValue(etNome.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("ora").setValue(etOra.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("primo").setValue(etPrimo.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("secondo").setValue(etSecondo.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("contorno").setValue(etContorno.getText().toString());


        // perform HTTP POST request
        if(checkNetworkConnection())
            new HTTPAsyncTask().execute("https://letsorderapi.glitch.me/?tipo=diretto&lista=" + nome + "" + luogo + "");
        else
            Toast.makeText(this, "Non connesso!", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(PrenotazioneActivity.this, RiepilogoActivity.class);

        intent.putExtra("nome", etNome.getText().toString());
        intent.putExtra("ora", etOra.getText().toString());
        intent.putExtra("primo", etPrimo.getText().toString());
        intent.putExtra("secondo", etSecondo.getText().toString());
        intent.putExtra("contorno", etContorno.getText().toString());

        startActivity(intent);

    }

    private JSONObject buidJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject("ordinazione");
        jsonObject.accumulate("nome", etNome.getText().toString());
        jsonObject.accumulate("ora",  etOra.getText().toString());
        jsonObject.accumulate("primo",  etPrimo.getText().toString());
        jsonObject.accumulate("secondo",  etSecondo.getText().toString());
        jsonObject.accumulate("contorno",  etContorno.getText().toString());

        return jsonObject;
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }



}
