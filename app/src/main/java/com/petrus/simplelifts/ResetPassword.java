package com.petrus.simplelifts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity
{
    private TextInputEditText editTextEmailReset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        editTextEmailReset = findViewById(R.id.editTextEmailReset);
        Button buttonReset = findViewById(R.id.buttonReset);

        auth = FirebaseAuth.getInstance();

        buttonReset.setOnClickListener(view ->
        {
            String email = editTextEmailReset.getText().toString();

            if (email.trim().isEmpty())
            {
                Toast.makeText(ResetPassword.this, "Please enter a valid email", Toast.LENGTH_LONG).show();
            }
            else
            {
                resetPW(email);
            }
        });
    }

    public void resetPW(String email)
    {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(ResetPassword.this, "Password reset", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ResetPassword.this, "Reset failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}