package com.petrus.simplelifts;

public class SetModel
{
    Double weight;
    Integer reps;
    String difficulty;

    public SetModel(){}

    public SetModel(Double weight, Integer reps, String difficulty)
    {
        this.weight = weight;
        this.reps = reps;
        this.difficulty = difficulty;
    }

    public Double getWeight()
    {
        return weight;
    }

    public Integer getReps()
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

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public void setReps(Integer reps)
    {
        this.reps = reps;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }
}
