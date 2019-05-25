package com.adeluna.letsorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void tvRegistratiClick(View view) {

        Log.d("LoginActivity", "Registrati Click");

        Intent intent_register = new Intent(this, RegisterActivity.class);
        finish();

        startActivity(intent_register);

    }

}
