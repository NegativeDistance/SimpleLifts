package com.petrus.simplelifts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth auth;
    String email;

    Button buttonBench, buttonOHP, buttonSquat, buttonDeadlift, buttonRow;
    ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        buttonBench = findViewById(R.id.buttonBench);
        buttonOHP = findViewById(R.id.buttonOHP);
        buttonSquat = findViewById(R.id.buttonSquat);
        buttonDeadlift = findViewById(R.id.buttonDeadlift);
        buttonRow = findViewById(R.id.buttonRow);

        settings = findViewById(R.id.imageViewSettings);

        buttonBench.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainActivity.this, ActiveLift.class);
            intent.putExtra("mode", "bench");
            startActivity(intent);
        });

        buttonOHP.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainActivity.this, ActiveLift.class);
            intent.putExtra("mode", "overhead");
            startActivity(intent);
        });

        buttonSquat.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainActivity.this, ActiveLift.class);
            intent.putExtra("mode", "squat");
            startActivity(intent);
        });

        buttonDeadlift.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainActivity.this, ActiveLift.class);
            intent.putExtra("mode", "deadlift");
            startActivity(intent);
        });

        buttonRow.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainActivity.this, ActiveLift.class);
            intent.putExtra("mode", "row");
            startActivity(intent);
        });

        settings.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // Check if user is signed in and update UI accordingly;

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null)
        {
            email = currentUser.getEmail();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}