package com.petrus.simplelifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class Session_RecyclerViewAdapter extends RecyclerView.Adapter <Session_RecyclerViewAdapter.MyViewHolder>
{
    Context context;
    Session session;

    public Session_RecyclerViewAdapter(Session session, Context context)
    {
        this.session = session;
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
        DecimalFormat format = new DecimalFormat("0.#");

        holder.weight.setText(String.valueOf(format.format(session.get(position).getWeight())));
        holder.reps.setText(String.valueOf(session.get(position).getReps()));
        holder.difficulty.setImageResource(session.get(position).imageValue());
    }

    @Override
    public int getItemCount()
    {
        // the recycler view just wants to know the number of items you want displayed
        return session.size();
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
