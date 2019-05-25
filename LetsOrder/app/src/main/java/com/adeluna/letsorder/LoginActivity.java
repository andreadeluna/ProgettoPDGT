package com.adeluna.letsorder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //TODO: Definire a livello globale le variabili e le costanti

    // Variabili

    EditText mNomeUtente;
    EditText mPassword;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser currentUser) {

        // Se l'utente è già loggato andare in MainActivity

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            // Email address

            String email = user.getEmail();

            Intent intent_main = new Intent(this, MainActivity.class);

            // Passare il nome utente a MainAtivity con putExtra

            intent_main.putExtra("msg", email);
            finish();

            startActivity(intent_main);

        }

    }


    public void btnLoginClick(View view) {

        Log.d("LoginActivity", "Login Button Click");

        // Collegare le variabili ai Widgets

        mNomeUtente = (EditText)findViewById(R.id.etRegName);
        mPassword = (EditText)findViewById(R.id.etRegPass);

        String nomeUtente = mNomeUtente.getText().toString();
        String password = mPassword.getText().toString();

        Log.d("LoginActivity", nomeUtente);
        Log.d("LoginActivity", password);

        if(!(nomeUtente.length() > 7) || !(nomeUtente.contains("@"))){

            Toast.makeText(this, "E-mail non valida", Toast.LENGTH_LONG).show();

        }
        else if(!(password.length() > 7)){

            Toast.makeText(this, "Password non valida", Toast.LENGTH_LONG).show();

        }
        else{

            loginUser(nomeUtente, password);

        }

    }


    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Autenticazione fallita.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }


    public void tvRegistratiClick(View view) {

        Log.d("LoginActivity", "Registrati Click");

        Intent intent_register = new Intent(this, RegisterActivity.class);
        finish();

        startActivity(intent_register);

    }

}
