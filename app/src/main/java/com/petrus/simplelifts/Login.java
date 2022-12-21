package com.petrus.simplelifts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Login extends AppCompatActivity
{
    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonSignUp;
    private TextView textViewForgot;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart()
    {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewForgot = findViewById(R.id.textViewForgot);

        buttonLogin.setOnClickListener(view ->
        {
            String email = Objects.requireNonNull(editTextEmail.getText()).toString();
            String password = Objects.requireNonNull(editTextPassword.getText()).toString();

            if (email.trim().isEmpty())
            {
                Toast.makeText(Login.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            }
            else if (password.trim().isEmpty())
            {
                Toast.makeText(Login.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            }
            else
            {
                login(email, password);
            }
        });

        buttonSignUp.setOnClickListener(view ->
        {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        textViewForgot.setOnClickListener(view ->
        {
            Intent intent = new Intent(Login.this, ResetPassword.class);
            startActivity(intent);
        });
    }

    public void login(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}