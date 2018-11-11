package com.example.vray.aptivinventory;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    private List<Item> list;
    public void getEditItem(Intent intent) {
        mContext.startActivity(intent);
    }

    public ItemAdapter(Context mContext, List<Item> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Item item = list.get(position);

        holder.textName.setText(item.getName());
        holder.textPrice.setText(String.valueOf(item.getPrice()));
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));
        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditItem.class);
                intent.putExtra("itemid", position);
                getEditItem(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textPrice, textQuantity;
        public Button editItem;

        public ViewHolder(View itemView) {
            super(itemView);
            editItem = itemView.findViewById(R.id.editItem);

            textName = itemView.findViewById(R.id.main_title);
            textPrice = itemView.findViewById(R.id.main_price);
            textQuantity = itemView.findViewById(R.id.main_quantity);
        }
    }

}
