package com.petrus.simplelifts;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Session_RecyclerViewAdapter extends RecyclerView.Adapter <Session_RecyclerViewAdapter.MyViewHolder>
{
    Context context;
    ArrayList<SessionModel> sessionModels;

    public Session_RecyclerViewAdapter(ArrayList<SessionModel> sessionModels, Context context)
    {
        this.sessionModels = sessionModels;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public Session_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        //  is where you inflate the layout (Giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new Session_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Session_RecyclerViewAdapter.MyViewHolder holder, int position)
    {
        // assigning values to the views we created in the recycler_view_row layout file
        // based on the position of the recycler view


        holder.weight.setText(sessionModels.get(position).getWeight());
        holder.reps.setText(sessionModels.get(position).getReps());
        holder.difficulty.setImageResource(sessionModels.get(position).imageValue());
    }

    @Override
    public int getItemCount()
    {
        // the recyler view just wants to know the number of items you want displayed
        return sessionModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {   // grabbing the views from our recycler_view_row layout file
        // kind of like in the onCreate method (ehhh)

        TextView weight, reps, by;
        ImageView difficulty;

        public MyViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);

            weight = itemView.findViewById(R.id.textViewWeight);
            reps = itemView.findViewById(R.id.textViewReps);
            by = itemView.findViewById(R.id.textViewBy);
            difficulty = itemView.findViewById(R.id.imageViewDifficulty);
        }
    }
}
