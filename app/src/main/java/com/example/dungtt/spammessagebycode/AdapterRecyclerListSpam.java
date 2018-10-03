package com.example.dungtt.spammessagebycode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class
AdapterRecyclerListSpam extends RecyclerView.Adapter{

    private List<ContactSpam> list;

    private int position;

    ListenerEvent listenerEvent;

    public AdapterRecyclerListSpam(ArrayList list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setListenerEvent(ListenerEvent event){
        this.listenerEvent = event;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View chanelView = inflater.inflate(R.layout.spamnumber, parent, false);

        ViewHolder viewHolder = new ViewHolder(chanelView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final String str = (String) list.get(position).getNumberSpam();
        TextView textView = (TextView) holder.itemView.findViewById(R.id.numberPhoneSpam);
        Log.w(TAG, "onBindViewHolder: "+str);
        textView.setText(str);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getPosition());
                System.out.println("Long click " + getPosition());
                listenerEvent.onLongClickItem(getPosition());
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}