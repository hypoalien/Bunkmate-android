package com.hypoalien.bunkmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<String > dataTime;
    private List<String> dataSubject;

    Adapter(Context context, List<String>dataTime,List<String>dataSubject){
        this.layoutInflater=LayoutInflater.from(context);
        this.dataTime=dataTime;
        this.dataSubject=dataSubject;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.today_layout_rv,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String timetext =dataTime.get(position);
        holder.textTime.setText(timetext);
        String subjectText=dataSubject.get(position);
        holder.textSubject.setText(subjectText);
    }

    @Override
    public int getItemCount() {
        return dataSubject.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView textTime,textSubject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTime=itemView.findViewById(R.id.today_time);
            textSubject=itemView.findViewById(R.id.today_sub_name);

        }
    }
}
