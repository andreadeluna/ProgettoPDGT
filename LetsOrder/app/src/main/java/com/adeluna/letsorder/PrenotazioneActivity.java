package com.adeluna.letsorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrenotazioneActivity extends AppCompatActivity {

    private static final String TAG = "inviaPrenotazione";

    FirebaseDatabase mDatabase;

    // UI
    EditText etNome;
    EditText etOra;
    EditText etPrimo;
    EditText etSecondo;
    EditText etContorno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotazione);

        initFirebase();
        initUI();

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


    public void inviaMessaggio(View view) {

        Log.i(TAG, "Click su Invia Messaggio");

        // Write a message to the database

        DatabaseReference myRef = mDatabase.getReference();

        myRef.child("Utenti").child(etNome.getText().toString()).child("Nome").setValue(etNome.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("Ora").setValue(etOra.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("Primo").setValue(etPrimo.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("Secondo").setValue(etSecondo.getText().toString());
        myRef.child("Utenti").child(etNome.getText().toString()).child("Contorno").setValue(etContorno.getText().toString());





        Intent intent = new Intent(PrenotazioneActivity.this, RiepilogoActivity.class);

        intent.putExtra("nome", etNome.getText().toString());
        intent.putExtra("ora", etOra.getText().toString());
        intent.putExtra("primo", etPrimo.getText().toString());
        intent.putExtra("secondo", etSecondo.getText().toString());
        intent.putExtra("contorno", etContorno.getText().toString());

        startActivity(intent);

    }

}
