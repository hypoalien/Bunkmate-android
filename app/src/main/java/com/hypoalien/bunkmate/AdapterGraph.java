package com.hypoalien.bunkmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterGraph extends RecyclerView.Adapter<AdapterGraph.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<String > mName;
    private ArrayList<String> mPercent;

    AdapterGraph(Context context, ArrayList<String>mName,ArrayList<String>mPercent){
        this.layoutInflater=LayoutInflater.from(context);
        this.mName=mName;
        this.mPercent =mPercent;

    }

    @NonNull
    @Override
    public AdapterGraph.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.graphs_rv,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        String subName =mName.get(position);
        holder.textName.setText(subName);

        String percentValue  =mPercent.get(position);
        holder.textPercent.setText(percentValue);


        holder.progressBar.setProgress((int)Double.parseDouble(mPercent.get(position).trim().replace("%", "")));
        holder.progressBar2.setProgress(100);

    }


    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textName,textPercent;
        ProgressBar progressBar,progressBar2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName=itemView.findViewById(R.id.textView13);
            textPercent=itemView.findViewById(R.id.textView10);
            progressBar=itemView.findViewById(R.id.progressBar);
            progressBar2=itemView.findViewById(R.id.progressBar2);
        }
    }
}
