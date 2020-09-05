package com.hypoalien.bunkmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterBacklogs extends RecyclerView.Adapter<AdapterBacklogs.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<String > mName;
    private ArrayList<String> mStatus;

    AdapterBacklogs(Context context, ArrayList<String>mName, ArrayList<String>mStatus){
        this.layoutInflater=LayoutInflater.from(context);
        this.mName=mName;
        this.mStatus =mStatus;

    }

    @NonNull
    @Override
    public AdapterBacklogs.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.backlogs_rv,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBacklogs.ViewHolder holder, int position) {
        String subName  =mName.get(position);
        holder.textName.setText(subName);

        String percentValue  =mStatus.get(position);
        holder.textStaus.setText("Semester "+percentValue);

    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textName,textStaus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.mSubject);
            textStaus=itemView.findViewById(R.id.mSem);



        }
    }
}
