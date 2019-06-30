package com.adeluna.letsorder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adeluna.letsorder.adapter.RistorantiAdapter;
import com.adeluna.letsorder.model.Ristorante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class MainActivity extends AppCompatActivity {

    ArrayList<Ristorante> ristoranti;
    RistorantiAdapter adapter;

    EditText et_luogo;
    Button btn_cerca;

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();


        // Write a message to the database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        et_luogo = findViewById(R.id.et_luogo);
        btn_cerca = findViewById(R.id.btn_cerca);


        btn_cerca.setOnClickListener(b -> {

            ristoranti = new ArrayList<Ristorante>();
            ListView listView = (ListView) findViewById(R.id.lst_ristoranti);

            DownloadTask task = new DownloadTask();

            String luogo = et_luogo.getText().toString();

            task.execute("https://letsorderapi.glitch.me/?tipo=luogo&lista=" + luogo);

            adapter = new RistorantiAdapter(getApplicationContext(), R.layout.item_ristorante, ristoranti);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String nome = (String) ((TextView) view.findViewById(R.id.tv_nome)).getText();
                    Intent intent = new Intent(MainActivity.this, RistoranteDatiActivity.class);
                    intent.putExtra("nome", nome);
                    intent.putExtra("luogo", luogo);
                    finish();
                    startActivity(intent);

                }
            });

        });


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
                JSONObject jsonObject = new JSONObject(result);

                String ristoranti_str = jsonObject.getString("lista");

                Log.i("JSONDemo", ristoranti_str);
                JSONArray array = new JSONArray(ristoranti_str);

                for(int i=0; i<array.length(); i++){
                    Ristorante rist = new Ristorante();


                    JSONObject jsonPart = array.getJSONObject(i);
                    String nomeRist = jsonPart.getString("nome");
                    String indirizzoRist = jsonPart.getString("indirizzo");
                    String aperturaRist = jsonPart.getString("apertura");
                    String postiRist = jsonPart.getString("posti liberi");

                    rist.setNome(nomeRist);
                    rist.setIndirizzo(indirizzoRist);
                    rist.setApertura(aperturaRist);
                    rist.setPosti("Posti liberi: " + postiRist);

                    ristoranti.add(rist);

                    Log.i("JSONDemo", nomeRist + " " + indirizzoRist + " " + aperturaRist + " " + postiRist);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.layout_menu, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logoutItem) {

            Log.i(TAG, "Logout selezionato");

            // Logout

            mAuth.signOut();

            updateUI();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void updateUI() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }

}
