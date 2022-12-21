package com.petrus.simplelifts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SignUp extends AppCompatActivity
{
    private TextInputEditText editTextEmailRegister, editTextPasswordRegister;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        editTextEmailRegister = findViewById(R.id.editTextEmailRegister);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(view ->
        {
            String email = Objects.requireNonNull(editTextEmailRegister.getText()).toString();
            String password = Objects.requireNonNull(editTextPasswordRegister.getText()).toString();

            Log.d("signup", "button press");

            if (email.trim().isEmpty())
            {
                Toast.makeText(SignUp.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            }
            else if (password.trim().isEmpty())
            {
                Toast.makeText(SignUp.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            }
            else
            {
                register(email, password);
                Log.d("signup", "register call");
            }
        });
    }

    public void register(String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent (SignUp.this, MainActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    Log.d("signup", "success");
                }
                else
                {
                    Toast.makeText(SignUp.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    Log.d("signup", "fail");
                }
            }
        });
    }
}