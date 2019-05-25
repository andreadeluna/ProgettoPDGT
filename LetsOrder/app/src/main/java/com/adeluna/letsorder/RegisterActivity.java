package com.adeluna.letsorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void tvLoginClick(View view) {

        Log.d("RegisterActivity", "TextView Login Click");

        Intent intent_login = new Intent(this, LoginActivity.class);
        finish();

        startActivity(intent_login);

    }

}
