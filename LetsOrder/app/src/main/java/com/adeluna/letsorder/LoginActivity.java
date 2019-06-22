package com.adeluna.letsorder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity1";

    //TODO: Definire a livello globale le variabili e le costanti

    // Variabili

    EditText mNomeUtente, mPassword;
    TextView tvLogin;
    Button btnRegistrati;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        initUI();

        mAuth = FirebaseAuth.getInstance();

        tvLogin.setOnClickListener(v -> {
            Log.d("LoginActivity1", "Registrati Click");
            finish();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });


        btnRegistrati.setOnClickListener(v -> {

            Log.d("LoginActivity1", "Login Button Click");

            String nomeUtente = mNomeUtente.getText().toString();
            String password = mPassword.getText().toString();

            Log.d("LoginActivity1", nomeUtente);
            Log.d("LoginActivity1", password);

            if (!(nomeUtente.length() > 7) || !emailValida(nomeUtente)) {

                Snackbar snack = Snackbar.make(v, "E-mail non valida", Snackbar.LENGTH_LONG);
                SnackbarHelper.configSnackbar(v.getContext(), snack);
                snack.show();

            } else if (!(password.length() > 7)) {

                Snackbar snack = Snackbar.make(v, "Password non valida", Snackbar.LENGTH_LONG);
                SnackbarHelper.configSnackbar(v.getContext(), snack);
                snack.show();

            } else
                loginUser(nomeUtente, password, v);


        });
    }

    private void initUI() {

        tvLogin = findViewById(R.id.tvLogin);
        btnRegistrati = findViewById(R.id.btnRegistrati);
        mNomeUtente = findViewById(R.id.etRegName);
        mPassword = findViewById(R.id.etRegPass);

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

            finish();

            // Passare il nome utente a MainAtivity con putExtra
            startActivity(new Intent(this, MainActivity.class).putExtra("msg", email));

        }

    }


    private boolean emailValida(String email) {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.matches();
    }

    private void loginUser(String email, String password, View view) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        Snackbar snack = Snackbar.make(view, "Autenticazione fallita.", Snackbar.LENGTH_LONG);
                        SnackbarHelper.configSnackbar(view.getContext(), snack);
                        snack.show();

                    }

                    // ...
                });

    }


}
