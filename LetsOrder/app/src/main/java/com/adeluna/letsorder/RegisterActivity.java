package com.adeluna.letsorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    // Costanti

    static final String CHAT_PREFS = "ChatPrefs";
    static final String NOME_KEY = "username";

    EditText mConfermaPassword;
    EditText mEmail;
    EditText mPassword;
    EditText mNome;


    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            Toast.makeText(this, "Utente già loggato", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();

        mAuth = FirebaseAuth.getInstance();
    }


    private void initUI() {

        mEmail = findViewById(R.id.etRegEmail);
        mPassword = findViewById(R.id.etRegPass);
        mConfermaPassword = findViewById(R.id.etRegPassConf);
        mNome = findViewById(R.id.etRegName);

    }


    private void createFirebaseUser(String email, String password, final String nome) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.i("ChatUPRegistration", "createUserWithEmail:success");

                        salvaNome();

                        // Caricare nome in Firebase
                        setNome(nome);

                        finish();   // Libera la memoria
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                    } else {

                        // If sign in fails, display a message to the user.
                        Log.i("ChatUPRegistration", "createUserWithEmail:failure", task.getException());

                        // Chiamare l'alert dialog

                        showDialog("Errore durante la registrazione", "Errore", android.R.drawable.ic_dialog_alert);

                    }

                });

    }


    // Salvare il campo nome all'interno delle SharedPreferences

    private void salvaNome() {

        String nome = mNome.getText().toString();

        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(NOME_KEY, nome).apply();

    }


    // Caricare nome in Firebase

    private void setNome(String nome) {

        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();

        user.updateProfile(changeRequest).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                Log.i("setNome", "Nome caricato con successo");

            } else {

                Log.i("setNome", "Errore nel caricamento del nome");

            }

        });

    }


    // Creare un alert dialog da mostrare in caso di registration failed

    private void showDialog(String message, String title, int icon) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(icon)
                .show();


    }


    public void btnRegistratiClick(View view) {

        Log.d("RegisterActivity", "Button Registrati Click");

        String nome = mNome.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        // Validazioni dati

        if (!nomeValido(nome)) {

            Toast.makeText(getApplicationContext(), "Nome non valido", Toast.LENGTH_SHORT).show();

        } else if (!emailValida(email)) {

            Toast.makeText(getApplicationContext(), "E-mail non valida", Toast.LENGTH_SHORT).show();

        } else if (!passwordValida(password)) {

            Toast.makeText(getApplicationContext(), "Password non valida", Toast.LENGTH_SHORT).show();

        } else {

            createFirebaseUser(email, password, nome);

        }


    }


    private boolean nomeValido(String nome) {

        boolean ret;

        if (nome.length() > 3) {

            ret = true;

        } else {

            ret = false;

        }

        return ret;

    }


    private boolean emailValida(String email) {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }


    private boolean passwordValida(String password) {

        String confermaPassword = mConfermaPassword.getText().toString();
        return confermaPassword.equals(password) && password.length() > 7;

    }


    public void tvLoginClick(View view) {

        Log.d("RegisterActivity", "TextView Login Click");

        startActivity(new Intent(this, LoginActivity.class));
        finish();


    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
