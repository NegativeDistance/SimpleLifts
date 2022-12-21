package com.petrus.simplelifts;

public class SessionModel
{
    String weight;
    String reps;
    String difficulty;

    public SessionModel(){}

    public SessionModel(String weight, String reps, String difficulty)
    {
        this.weight = weight;
        this.reps = reps;
        this.difficulty = difficulty;
    }

    public String getWeight()
    {
        return weight;
    }

    public String getReps()
    {
        return reps;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public int imageValue()
    {
        int image = 0;

        switch (difficulty)
        {
            case "easy":
                image = R.drawable.bubble_easy;
                break;
            case "normal":
                image = R.drawable.bubble_normal;
                break;
            case "hard":
                image = R.drawable.bubble_hard;
                break;
        }

        return image;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public void setReps(String reps)
    {
        this.reps = reps;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }
}
