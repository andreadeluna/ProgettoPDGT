package com.adeluna.letsorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiepilogoActivity extends AppCompatActivity {



    // UI
    TextView tvNomeOrd;
    TextView tvOraOrd;
    TextView tvPrimoOrd;
    TextView tvSecondoOrd;
    TextView tvContornoOrd;

    String nome;
    String ora;
    String primo;
    String secondo;
    String contorno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo);



        Intent intent = getIntent();
        nome = intent.getExtras().getString("nome");
        ora = intent.getExtras().getString("ora");
        primo = intent.getExtras().getString("primo");
        secondo = intent.getExtras().getString("secondo");
        contorno = intent.getExtras().getString("contorno");


        tvNomeOrd = (TextView) findViewById(R.id.tv_nome_ord);
        tvOraOrd = (TextView) findViewById(R.id.tv_ora_ord);
        tvPrimoOrd = (TextView) findViewById(R.id.tv_primo_ord);
        tvSecondoOrd = (TextView) findViewById(R.id.tv_secondo_ord);
        tvContornoOrd = (TextView) findViewById(R.id.tv_contorno_ord);

        tvNomeOrd.setText("• " + nome);
        tvOraOrd.setText("• " + ora);
        tvPrimoOrd.setText("• " + primo);
        tvSecondoOrd.setText("• " + secondo);
        tvContornoOrd.setText("• " + contorno);


    }


}
