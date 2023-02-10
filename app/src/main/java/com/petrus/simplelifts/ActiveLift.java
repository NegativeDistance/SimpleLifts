package com.petrus.simplelifts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActiveLift extends AppCompatActivity
{
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database;

    ArrayList<SessionModel> sessionModelsPrevious, sessionModelsCurrent;
    Session_RecyclerViewAdapter adapterPrevious, adapterCurrent;

    TextView textViewTitle;
    TextView textViewTopSeek2;
    TextView textViewBottomSeek1, textViewBottomSeek2, textViewBottomSeek3;

    RadioButton easy, normal, hard;
    Button add, delete, finish;
    EditText weight, reps;

    SeekBar seekBarBarType, seekBarSelectTwo, seekBarSelectThree;

    String uid;
    String lift;
    String benchFlatBB, benchFlatDB, benchInclineBB, benchInclineDB, benchDeclineBB, benchDeclineDB, squatHighBB, squatLowBB, squatFrontBB,
            deadliftConvBB, deadliftConvHex, deadliftSumoBB;

    int position = 0;
    String difficulty;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_lift);

        database = FirebaseFirestore.getInstance();

        //Find where to put this/set it for the user
        uid = auth.getUid();
        mode = getIntent().getStringExtra("mode");

        sessionModelsPrevious = new ArrayList<>();
        sessionModelsCurrent = new ArrayList<>();
        adapterPrevious = new Session_RecyclerViewAdapter(sessionModelsPrevious, this);
        adapterCurrent = new Session_RecyclerViewAdapter(sessionModelsCurrent, this);

        RecyclerView recyclerViewPrevious = findViewById(R.id.recyclerViewPrevious);
        RecyclerView recyclerViewCurrent = findViewById(R.id.recyclerViewCurrent);

        recyclerViewPrevious.setAdapter(adapterPrevious);
        recyclerViewPrevious.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCurrent.setAdapter(adapterCurrent);
        recyclerViewCurrent.setLayoutManager(new LinearLayoutManager(this));

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTopSeek2 = findViewById(R.id.textViewTopSeek2);
        textViewBottomSeek1 = findViewById(R.id.textViewBottomSeek1);
        textViewBottomSeek2 = findViewById(R.id.textViewBottomSeek2);
        textViewBottomSeek3 = findViewById(R.id.textViewBottomSeek3);

        seekBarBarType = findViewById(R.id.seekBarBarType);
        seekBarSelectThree = findViewById(R.id.seekBarSelectThree);
        seekBarSelectTwo = findViewById(R.id.seekBarSelectTwo);

        easy = findViewById(R.id.radioButtonEasy);
        normal = findViewById(R.id.radioButtonNormal);
        hard = findViewById(R.id.radioButtonHard);

        add = findViewById(R.id.buttonAdd);
        delete = findViewById(R.id.buttonDelete);
        finish = findViewById(R.id.buttonFinish);

        weight = findViewById(R.id.editTextWeight);
        reps = findViewById(R.id.editTextReps);

        benchFlatBB = getString(R.string.benchFlatBB);
        benchFlatDB = getString(R.string.benchFlatDB);
        benchInclineBB = getString(R.string.benchInclineBB);
        benchInclineDB = getString(R.string.benchInclineDB);
        benchDeclineBB = getString(R.string.benchInclineBB);
        benchDeclineDB = getString(R.string.benchInclineDB);
        squatHighBB = getString(R.string.squatHighBB);
        squatLowBB = getString(R.string.squatLowBB);
        squatFrontBB = getString(R.string.squatFrontBB);
        deadliftConvBB = getString(R.string.deadliftConvBB);
        deadliftConvHex = getString(R.string.deadliftConvHex);
        deadliftSumoBB = getString(R.string.deadliftSumoBB);

        setup(mode);

        seekBarBarType.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                switch (mode)
                {
                    case "bench":
                        if (progress == 0)
                        {
                            if (seekBarSelectThree.getProgress() == 0)
                            {
                                lift = benchDeclineBB;
                            }
                            else if (seekBarSelectThree.getProgress() == 1)
                            {
                                lift = benchFlatBB;
                            }
                            else if (seekBarSelectThree.getProgress() == 2)
                            {
                                lift = benchInclineBB;
                            }
                        }
                        else if (progress == 1)
                        {
                            if (seekBarSelectThree.getProgress() == 0)
                            {
                                lift = benchDeclineDB;
                            }
                            else if (seekBarSelectThree.getProgress() == 1)
                            {
                                lift = benchFlatDB;
                            }
                            else if (seekBarSelectThree.getProgress() == 2)
                            {
                                lift = benchInclineDB;
                            }
                        }
                        break;

                    case "deadlift":
                        if (progress == 0)
                        {
                            if  (seekBarSelectTwo.getProgress() == 0)
                            {
                                lift = deadliftConvBB;
                            }
                            else if (seekBarSelectTwo.getProgress() == 1)
                            {
                                lift = deadliftSumoBB;
                            }
                        }
                        else if (progress == 1)
                        {
                            seekBarSelectTwo.setProgress(0);
                            lift = deadliftConvHex;
                        }
                        break;
                }

                fetchData(sessionModelsPrevious, adapterPrevious);
                Log.d("switch", "current lift: " + lift);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        seekBarSelectThree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                if (progress == 0)
                {
                    if (seekBarBarType.getProgress() == 0)
                    {
                        lift = benchDeclineBB;
                    }
                    else if (seekBarBarType.getProgress() == 1)
                    {
                        lift = benchDeclineDB;
                    }
                }
                else if (progress == 1)
                {
                    if (seekBarBarType.getProgress() == 0)
                    {
                        lift = benchFlatBB;
                    }
                    else if (seekBarBarType.getProgress() == 1)
                    {
                        lift = benchFlatDB;
                    }
                }
                else if (progress == 2)
                {
                    if (seekBarBarType.getProgress() == 0)
                    {
                        lift = benchInclineBB;
                    }
                    else if (seekBarBarType.getProgress() == 1)
                    {
                        lift = benchInclineDB;
                    }
                }
                fetchData(sessionModelsPrevious, adapterPrevious);
                Log.d("switch", "current lift: " + lift);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        seekBarSelectTwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                switch (mode)
                {
                    case "ohpress":
                        break;

                    case "deadlift":
                        if (progress == 0)
                        {
                            if (seekBarBarType.getProgress() == 0)
                            {
                                lift = deadliftConvBB;
                            }
                            else if (seekBarBarType.getProgress() == 1)
                            {
                                lift = deadliftSumoBB;
                            }
                        }
                        else if (progress == 1)
                        {
                            seekBarBarType.setProgress(0);
                            lift = deadliftConvHex;
                        }
                        break;
                }
                fetchData(sessionModelsPrevious, adapterPrevious);
                Log.d("switch", "current lift: " + lift);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        add.setOnClickListener(view ->
        {
            if (easy.isChecked())
            {
                difficulty = "easy";
            }
            else if (normal.isChecked())
            {
                difficulty = "normal";
            }
            else if (hard.isChecked())
            {
                difficulty = "hard";
            }

            sessionModelsCurrent.add(new SessionModel(Double.parseDouble(weight.getText().toString()), Integer.parseInt(reps.getText().toString()), difficulty));
            adapterCurrent.notifyItemInserted(position);

            delete.setClickable(true);
            finish.setClickable(true);

            position++;
        });

        delete.setOnClickListener(view ->
        {
            if (position == 0)
            {
                Toast.makeText(getApplicationContext(), "No entries", Toast.LENGTH_LONG).show();
            }
            else
            {
                sessionModelsCurrent.remove(position - 1);
                adapterCurrent.notifyItemRemoved(position - 1);
                adapterCurrent.notifyItemRangeChanged(position - 1, sessionModelsCurrent.size());

                position--;

                if (position == 0)
                {
                    delete.setClickable(false);
                    finish.setClickable(false);
                }
            }
        });

        finish.setOnClickListener(view ->
        {
            if (sessionModelsCurrent.size() == 0)
            {
                Toast.makeText(getApplicationContext(), "No entries", Toast.LENGTH_LONG).show();
            }
            else
            {
                sessionModelsPrevious.clear();
                adapterPrevious.notifyItemRangeChanged(0, 0);
                adapterPrevious.notifyDataSetChanged();

                for (int i = 0; i < sessionModelsCurrent.size(); i++)
                {
                    Double weightAdd = sessionModelsCurrent.get(i).getWeight();
                    Integer repsAdd = sessionModelsCurrent.get(i).getReps();
                    String difficultyAdd = sessionModelsCurrent.get(i).getDifficulty();
                    int set = i + 1;

                    Map<String, Object> data = new HashMap<>();
                    data.put("weight", weightAdd);
                    data.put("reps", repsAdd);
                    data.put("difficulty", difficultyAdd);
                    data.put("set", set);

                    database.collection("users").document(uid).collection(lift)
                            .document("set" + (i + 1))
                            .set(data);

                    sessionModelsPrevious.add(new SessionModel(weightAdd, repsAdd, difficultyAdd));
                    adapterPrevious.notifyItemInserted(i);
                }

                sessionModelsCurrent.clear();
                adapterCurrent.notifyItemRangeChanged(0, 0);
                adapterCurrent.notifyDataSetChanged();

                position = 0;
            }
        });
    }

    private void setup(String mode)
    {
        switch(mode)
        {
            case "bench":
                lift = benchFlatBB;
                textViewTitle.setText(R.string.bench);
                textViewTopSeek2.setText(R.string.db);

                seekBarSelectThree.setVisibility(View.VISIBLE);
                seekBarSelectTwo.setVisibility(View.INVISIBLE);
                textViewBottomSeek1.setText(R.string.decline);
                textViewBottomSeek2.setVisibility(View.VISIBLE);
                textViewBottomSeek2.setText(R.string.flat);
                textViewBottomSeek3.setText(R.string.incline);
                break;

            case "squat":
                lift = squatHighBB;
                textViewTitle.setText(R.string.squat);

                seekBarSelectThree.setVisibility(View.VISIBLE);
                seekBarSelectTwo.setVisibility(View.INVISIBLE);
                textViewBottomSeek1.setText(R.string.lowbar);
                textViewBottomSeek2.setVisibility(View.VISIBLE);
                textViewBottomSeek2.setText(R.string.highbar);
                textViewBottomSeek3.setText(R.string.frontsquat);
                break;

            case "deadlift":
                lift = benchFlatBB;
                textViewTitle.setText(R.string.deadlift);
                textViewTopSeek2.setText(R.string.hexbar);

                seekBarSelectThree.setVisibility(View.INVISIBLE);
                seekBarSelectTwo.setVisibility(View.VISIBLE);
                textViewBottomSeek1.setText(R.string.conventional);
                textViewBottomSeek2.setVisibility(View.INVISIBLE);
                textViewBottomSeek3.setText(R.string.sumo);
                break;
        }
        fetchData(sessionModelsPrevious, adapterPrevious);
    }

    private void fetchData(ArrayList<SessionModel> previous, Session_RecyclerViewAdapter adapter)
    {
        previous.clear();
        database.collection("users").document(uid).collection(lift).orderBy("set")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                previous.add(document.toObject(SessionModel.class));
                                Log.d("fetch", "added to list, size: " + previous.size());
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Log.d("fetch", "error getting documents", task.getException());
                        }
                    }
                });
    }
}