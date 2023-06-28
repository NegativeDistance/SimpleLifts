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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ActiveLift extends AppCompatActivity
{
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database;

    Session sessionPrevious, sessionCurrent;
    Session_RecyclerViewAdapter adapterPrevious, adapterCurrent;

    TextView textViewTitle;
    TextView textViewTopSeek1, textViewTopSeek2;
    TextView textViewBottomSeek1, textViewBottomSeek2, textViewBottomSeek3;

    RadioButton easy, normal, hard;
    Button add, delete, finish;
    EditText weight, reps;

    SeekBar seekBarBarType, seekBarSelectTwo, seekBarSelectThree;

    String uid;
    String lift;
    String benchFlatBB, benchFlatDB, benchInclineBB, benchInclineDB, benchDeclineBB, benchDeclineDB,
            overheadStandingBB, overheadStandingDB, overheadSeatedBB, overheadSeatedDB,
            squatHighBB, squatLowBB, squatFrontBB,
            deadliftConvBB, deadliftConvHex, deadliftSumoBB,
            rowOverhandBB, rowOverhandDB, rowNeutralBB, rowNeutralDB, rowUnderhandBB, rowUnderhandDB;

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

        sessionPrevious = new Session();
        sessionCurrent = new Session();
        adapterPrevious = new Session_RecyclerViewAdapter(sessionPrevious, this);
        adapterCurrent = new Session_RecyclerViewAdapter(sessionCurrent, this);

        RecyclerView recyclerViewPrevious = findViewById(R.id.recyclerViewPrevious);
        RecyclerView recyclerViewCurrent = findViewById(R.id.recyclerViewCurrent);

        recyclerViewPrevious.setAdapter(adapterPrevious);
        recyclerViewPrevious.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCurrent.setAdapter(adapterCurrent);
        recyclerViewCurrent.setLayoutManager(new LinearLayoutManager(this));

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTopSeek1 = findViewById(R.id.textViewTopSeek1);
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
        benchDeclineBB = getString(R.string.benchDeclineBB);
        benchDeclineDB = getString(R.string.benchDeclineDB);
        overheadStandingBB = getString(R.string.overheadStandingBB);
        overheadStandingDB = getString(R.string.overheadStandingDB);
        overheadSeatedBB = getString(R.string.overheadSeatedBB);
        overheadSeatedDB = getString(R.string.overheadSeatedDB);
        squatHighBB = getString(R.string.squatHighBB);
        squatLowBB = getString(R.string.squatLowBB);
        squatFrontBB = getString(R.string.squatFrontBB);
        deadliftConvBB = getString(R.string.deadliftConvBB);
        deadliftConvHex = getString(R.string.deadliftConvHex);
        deadliftSumoBB = getString(R.string.deadliftSumoBB);
        rowOverhandBB = getString(R.string.rowOverhandBB);
        rowOverhandDB = getString(R.string.rowOverhandDB);
        rowNeutralBB = getString(R.string.rowNeutralBB);
        rowNeutralDB = getString(R.string.rowNeutralDB);
        rowUnderhandBB = getString(R.string.rowUnderhandBB);
        rowUnderhandDB = getString(R.string.rowUnderhandDB);

        setup(mode);

        seekBarBarType.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                switch (mode)
                {
                    case "bench":
                        if (progress == 0 && seekBarSelectThree.getProgress() == 0)
                        {
                            lift = benchDeclineBB;
                        }
                        else if (progress == 0 && seekBarSelectThree.getProgress() == 1)
                        {
                            lift = benchFlatBB;
                        }
                        else if (progress == 0 && seekBarSelectThree.getProgress() == 2)
                        {
                            lift = benchInclineBB;
                        }
                        if (progress == 1 && seekBarSelectThree.getProgress() == 0)
                        {
                            lift = benchDeclineDB;
                        }
                        else if (progress == 1 && seekBarSelectThree.getProgress() == 1)
                        {
                            lift = benchFlatDB;
                        }
                        else if (progress == 1 && seekBarSelectThree.getProgress() == 2)
                        {
                            lift = benchInclineDB;
                        }
                        break;

                    case "overhead":
                        if (progress == 0 && seekBarSelectTwo.getProgress() == 0)
                        {
                            lift = overheadStandingBB;
                        }
                        else if (progress == 0 && seekBarSelectTwo.getProgress() == 1)
                        {
                            lift = overheadSeatedBB;
                        }
                        if (progress == 1 && seekBarSelectTwo.getProgress() == 0)
                        {
                            lift = overheadStandingDB;
                        }
                        else if (progress == 1 && seekBarSelectTwo.getProgress() == 1)
                        {
                            lift = overheadSeatedDB;
                        }
                        break;

                    case "deadlift":
                        if (progress == 0 && seekBarSelectTwo.getProgress() == 0)
                        {
                            lift = deadliftConvBB;
                        }
                        if (progress == 0 && seekBarSelectTwo.getProgress() == 1)
                        {
                            lift = deadliftSumoBB;
                        }
                        else if (progress == 1)
                        {
                            seekBarSelectTwo.setProgress(0);
                            lift = deadliftConvHex;
                        }
                        break;

                    case "row":
                        if (progress == 0 && seekBarSelectThree.getProgress() == 0)
                        {
                            lift = rowOverhandBB;
                        }
                        else if (progress == 0 && seekBarSelectThree.getProgress() == 1)
                        {
                            lift = rowNeutralBB;
                        }
                        else if (progress == 0 && seekBarSelectThree.getProgress() == 2)
                        {
                            lift = rowUnderhandBB;
                        }
                        if (progress == 1 && seekBarSelectThree.getProgress() == 0)
                        {
                            lift = rowOverhandDB;
                        }
                        else if (progress == 1 && seekBarSelectThree.getProgress() == 1)
                        {
                            lift = rowNeutralDB;
                        }
                        else if (progress == 1 && seekBarSelectThree.getProgress() == 2)
                        {
                            lift = rowUnderhandDB;
                        }
                        break;
                }

                fetchData(sessionPrevious, adapterPrevious);
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
                switch (mode)
                {
                    case "bench":
                        if (progress == 0 && seekBarBarType.getProgress() == 0)
                        {
                            lift = benchDeclineBB;
                        }
                        else if (progress == 0 && seekBarBarType.getProgress() == 1)
                        {
                            lift = benchDeclineDB;
                        }
                        else if (progress == 1 && seekBarBarType.getProgress() == 0)
                        {
                            lift = benchFlatBB;
                        }
                        else if (progress == 1 && seekBarBarType.getProgress() == 1)
                        {
                            lift = benchFlatDB;
                        }
                        else if (progress == 2 && seekBarBarType.getProgress() == 0)
                        {
                            lift = benchInclineBB;
                        }
                        else if (progress == 2 && seekBarBarType.getProgress() == 1)
                        {
                            lift = benchInclineDB;
                        }
                        break;

                    case "squat":
                        if (progress == 0)
                        {
                            lift = squatLowBB;
                        }
                        else if (progress == 1)
                        {
                            lift = squatHighBB;
                        }
                        else if (progress == 2)
                        {
                            lift = squatFrontBB;
                        }
                        break;

                    case "row":
                        if (progress == 0 && seekBarBarType.getProgress() == 0)
                        {
                            lift = rowOverhandBB;
                        }
                        else if (progress == 0 && seekBarBarType.getProgress() == 1)
                        {
                            lift = rowOverhandDB;
                        }
                        else if (progress == 1 && seekBarBarType.getProgress() == 0)
                        {
                            lift = rowNeutralBB;
                        }
                        else if (progress == 1 && seekBarBarType.getProgress() == 1)
                        {
                            lift = rowNeutralDB;
                        }
                        else if (progress == 2 && seekBarBarType.getProgress() == 0)
                        {
                            lift = rowUnderhandBB;
                        }
                        else if (progress == 2 && seekBarBarType.getProgress() == 1)
                        {
                            lift = rowUnderhandDB;
                        }
                        break;
                }

                fetchData(sessionPrevious, adapterPrevious);
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
                    case "overhead":
                        if (progress == 0 && seekBarBarType.getProgress() == 0)
                        {
                            lift = overheadStandingBB;
                        }
                        else if (progress == 0 && seekBarBarType.getProgress() == 1)
                        {
                            lift = overheadStandingDB;
                        }
                        if (progress == 1 && seekBarBarType.getProgress() == 0)
                        {
                            lift = overheadSeatedBB;
                        }
                        else if (progress == 1 && seekBarBarType.getProgress() == 1)
                        {
                            lift = overheadSeatedDB;
                        }
                        break;

                    case "deadlift":
                        if (progress == 0 && seekBarBarType.getProgress() == 0)
                        {
                            lift = deadliftConvBB;
                        }
                        else if (progress == 0 && seekBarBarType.getProgress() == 1)
                        {
                            lift = deadliftConvHex;
                        }
                        else if (progress == 1)
                        {
                            seekBarBarType.setProgress(0);
                            lift = deadliftSumoBB;
                        }
                        break;
                }
                fetchData(sessionPrevious, adapterPrevious);
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

            sessionCurrent.add(new SetModel(Double.parseDouble(weight.getText().toString()), Integer.parseInt(reps.getText().toString()), difficulty));
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
                sessionCurrent.remove(position - 1);
                adapterCurrent.notifyItemRemoved(position - 1);
                adapterCurrent.notifyItemRangeChanged(position - 1, sessionCurrent.size());

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
            if (sessionCurrent.size() == 0)
            {
                Toast.makeText(getApplicationContext(), "No entries", Toast.LENGTH_LONG).show();
            }
            else
            {
                sessionPrevious.clear();
                adapterPrevious.notifyItemRangeChanged(0, 0);
                adapterPrevious.notifyDataSetChanged();

                Map<String, Object> time = new HashMap<>();
                time.put("timestamp", FieldValue.serverTimestamp());

                DocumentReference sessionReference = database.collection("users").document(uid).collection(lift).document();
                sessionReference.set(time);

                for (int i = 0; i < sessionCurrent.size(); i++)
                {
                    Double weightAdd = sessionCurrent.get(i).getWeight();
                    Integer repsAdd = sessionCurrent.get(i).getReps();
                    String difficultyAdd = sessionCurrent.get(i).getDifficulty();

                    Map<String, Object> data = new HashMap<>();
                    data.put("weight", weightAdd);
                    data.put("reps", repsAdd);
                    data.put("difficulty", difficultyAdd);

                    sessionReference.collection("sets").document(String.valueOf(i + 1)).set(data);

                    sessionPrevious.add(new SetModel(weightAdd, repsAdd, difficultyAdd));
                    adapterPrevious.notifyItemInserted(i);
                }


                sessionCurrent.clear();
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
                seekBarBarType.setVisibility(View.VISIBLE);
                seekBarSelectThree.setVisibility(View.VISIBLE);
                seekBarSelectTwo.setVisibility(View.INVISIBLE);

                textViewTitle.setText(R.string.bench);
                textViewTopSeek1.setVisibility(View.VISIBLE);
                textViewTopSeek2.setVisibility(View.VISIBLE);
                textViewTopSeek2.setText(R.string.db);

                textViewBottomSeek1.setText(R.string.decline);
                textViewBottomSeek2.setVisibility(View.VISIBLE);
                textViewBottomSeek2.setText(R.string.flat);
                textViewBottomSeek3.setText(R.string.incline);
                break;

            case "overhead":
                lift = overheadStandingBB;
                seekBarBarType.setVisibility(View.VISIBLE);
                seekBarSelectThree.setVisibility(View.INVISIBLE);
                seekBarSelectTwo.setVisibility(View.VISIBLE);

                textViewTitle.setText(R.string.ohp);
                textViewTopSeek1.setVisibility(View.VISIBLE);
                textViewTopSeek2.setVisibility(View.VISIBLE);
                textViewTopSeek2.setText(R.string.db);

                textViewBottomSeek1.setText(R.string.standing);
                textViewBottomSeek2.setVisibility(View.INVISIBLE);
                textViewBottomSeek3.setText(R.string.seated);
                break;

            case "squat":
                lift = squatHighBB;
                seekBarBarType.setVisibility(View.INVISIBLE);
                seekBarSelectThree.setVisibility(View.VISIBLE);
                seekBarSelectTwo.setVisibility(View.INVISIBLE);

                textViewTitle.setText(R.string.squat);
                textViewTopSeek1.setVisibility(View.INVISIBLE);
                textViewTopSeek2.setVisibility(View.INVISIBLE);

                textViewBottomSeek1.setText(R.string.lowbar);
                textViewBottomSeek2.setVisibility(View.VISIBLE);
                textViewBottomSeek2.setText(R.string.highbar);
                textViewBottomSeek3.setText(R.string.frontsquat);
                break;

            case "deadlift":
                lift = deadliftConvBB;
                seekBarBarType.setVisibility(View.VISIBLE);
                seekBarSelectThree.setVisibility(View.INVISIBLE);
                seekBarSelectTwo.setVisibility(View.VISIBLE);

                textViewTitle.setText(R.string.deadlift);
                textViewTopSeek1.setVisibility(View.VISIBLE);
                textViewTopSeek2.setVisibility(View.VISIBLE);
                textViewTopSeek2.setText(R.string.hexbar);

                textViewBottomSeek1.setText(R.string.conventional);
                textViewBottomSeek2.setVisibility(View.INVISIBLE);
                textViewBottomSeek3.setText(R.string.sumo);
                break;

            case "row":
                lift = rowOverhandBB;
                seekBarBarType.setVisibility(View.VISIBLE);
                seekBarSelectThree.setVisibility(View.VISIBLE);
                seekBarSelectThree.setProgress(0);
                seekBarSelectTwo.setVisibility(View.INVISIBLE);

                textViewTitle.setText(R.string.row);
                textViewTopSeek1.setVisibility(View.VISIBLE);
                textViewTopSeek2.setVisibility(View.VISIBLE);
                textViewTopSeek2.setText(R.string.db);

                textViewBottomSeek1.setText(R.string.overhand);
                textViewBottomSeek2.setVisibility(View.VISIBLE);
                textViewBottomSeek2.setText(R.string.neutral);
                textViewBottomSeek3.setText(R.string.underhand);
                break;
        }
        fetchData(sessionPrevious, adapterPrevious);
    }

    private void fetchData(Session previous, Session_RecyclerViewAdapter adapter)
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
                                previous.add(document.toObject(SetModel.class));
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