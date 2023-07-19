package com.petrus.simplelifts;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class DataManager
{
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private ArrayList<String> sessionIDs = new ArrayList<>();
    private int currentPos, maxPos;
    private String activeSessionID, uid, lift;

    DataManager()
    {
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();
        activeSessionID = "0";
        currentPos = 0;
        maxPos = 0;
    }

    public DocumentReference newDocumentID()
    {
        return database.collection("users").document(uid).collection(lift).document();
    }

    public void populate(Session previous, Session_RecyclerViewAdapter adapter)
    {
        sessionIDs.clear();
        database.collection("users").document(uid).collection(lift).orderBy("timestamp", Query.Direction.DESCENDING)
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
                                sessionIDs.add(document.getId());
                                Log.d("fetch", "added to list, size: " + sessionIDs.size() + "\n ID: " + sessionIDs.get(sessionIDs.size() - 1));
                                activeSessionID = sessionIDs.get(0);
                            }
                            currentPos = 0;
                            maxPos = sessionIDs.size() - 1;
                            fetchSets(previous, adapter);
                        }
                        else
                        {
                            Log.d("fetch", "error getting documents", task.getException());
                        }
                    }
                });
    }
    public void fetchSessions()
    {
        sessionIDs.clear();
        database.collection("users").document(uid).collection(lift).orderBy("timestamp")
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
                                sessionIDs.add(document.getId());
                                Log.d("fetch", "added to list, size: " + sessionIDs.size() + "\n ID: " + sessionIDs.get(sessionIDs.size() - 1));
                                activeSessionID = sessionIDs.get(0);
                            }
                        }
                        else
                        {
                            Log.d("fetch", "error getting documents", task.getException());
                        }
                    }
                });
    }

    public void fetchSets(Session previous, Session_RecyclerViewAdapter adapter)
    {
        if (sessionIDs.size() > 0)
        {
            activeSessionID = sessionIDs.get(currentPos);
        }

        Log.d("fetch", "Stupid fucking session ID" + sessionIDs.size());
        if (!activeSessionID.equals("0"))
        {
            Log.d("fetch", "Getting" + lift + " sets for ID: " + activeSessionID);
        }
        previous.clear();
        database.collection("users").document(uid).collection(lift).document(activeSessionID).collection("sets")
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

    public FirebaseFirestore getDatabase()
    {
        return database;
    }

    public void setDatabase(FirebaseFirestore database)
    {
        this.database = database;
    }

    public FirebaseAuth getAuth()
    {
        return auth;
    }

    public void setAuth(FirebaseAuth auth)
    {
        this.auth = auth;
    }

    public ArrayList<String> getSessionIDs()
    {
        return sessionIDs;
    }

    public void setSessionIDs(ArrayList<String> sessionIDs)
    {
        this.sessionIDs = sessionIDs;
    }

    public int getCurrentPos()
    {
        return currentPos;
    }

    public void setCurrentPos(int currentPos)
    {
        this.currentPos = currentPos;
    }

    public int getMaxPos()
    {
        return maxPos;
    }

    public void setMaxPos(int maxPos)
    {
        this.maxPos = maxPos;
    }

    public String getActiveSessionID()
    {
        return activeSessionID;
    }

    public void setActiveSessionID(String activeSessionID)
    {
        this.activeSessionID = activeSessionID;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getLift()
    {
        return lift;
    }

    public void setLift(String lift)
    {
        this.lift = lift;
    }
}
