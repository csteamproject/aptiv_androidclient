package com.example.vray.aptivinventory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ticketAdapter extends RecyclerView.Adapter<ticketAdapter.ViewHolder> {

    private Context mContext2;
    public static List<ticket> list2;
    public void getEditItem(Intent intent) {
        mContext2.startActivity(intent);
    }

    public ticketAdapter(Context mContext2, List<ticket> list2) {
        this.mContext2 = mContext2;
        this.list2 = list2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext2).inflate(R.layout.single_ticket, parent, false);
        return new ticketAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ticketAdapter.ViewHolder holder, final int position) {
        ticket ticket = list2.get(position);

        holder.textTitle.setText(ticket.getTitle());
        holder.textStatus.setText(ticket.getStatus());
        holder.textName.setText(ticket.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext2, EditTicket.class);
                intent.putExtra("priority", list2.get(position).getPriority());
                intent.putExtra("description", list2.get(position).getDescription());
                intent.putExtra("status", list2.get(position).getStatus());
                intent.putExtra("title", list2.get(position).getTitle());
                intent.putExtra("id", list2.get(position).getId());
                intent.putExtra("itemid", list2.get(position).getItemid());
                getEditItem(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list2.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textStatus, textName;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.title);
            textStatus = itemView.findViewById(R.id.status);
            textName = itemView.findViewById(R.id.itemname);
        }


    }
}
