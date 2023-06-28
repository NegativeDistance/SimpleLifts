package com.petrus.simplelifts;

import java.util.ArrayList;

public class Session
{
    ArrayList<SetModel> sets = new ArrayList<>();
    String timestamp;

    Session(){}

    Session(ArrayList<SetModel> sets, String timestamp)
    {
        this.sets = sets;
        this.timestamp = timestamp;
    }

    public void add(SetModel set)
    {
        sets.add(set);
    }

    public void remove(int i)
    {
        sets.remove(i);
    }

    public int size()
    {
        return sets.size();
    }

    public void clear()
    {
        sets.clear();
        timestamp = null;
    }

    public ArrayList<SetModel> getSets()
    {
        return sets;
    }

    public SetModel get(int i)
    {
        return sets.get(i);
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setSets(ArrayList<SetModel> sets)
    {
        this.sets = sets;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
}
