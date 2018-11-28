package com.example.vray.aptivinventory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;

    public static List<Item> list;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Item item = list.get(position);

        holder.textName.setText(item.getName());
        holder.brand.setText(item.getBrand());
        holder.model.setText(item.getModel());
        holder.serialNumber.setText(item.getSerialNum());
        holder.operable.setText(item.getOperable());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditItem.class);
                intent.putExtra("brand", list.get(position).getBrand());
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("model", list.get(position).getModel());
                intent.putExtra("serialNumber", list.get(position).getSerialNum());
                intent.putExtra("price", list.get(position).getPrice());
                intent.putExtra("quantity", list.get(position).getQuantity());
                intent.putExtra("operable", list.get(position).getOperable());
                intent.putExtra("itemid", list.get(position).getItemid());
                getEditItem(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, serialNumber, brand, model, operable;



        public ViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.main_title);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            brand = itemView.findViewById(R.id.brand);
            model = itemView.findViewById(R.id.model);
            operable = itemView.findViewById(R.id.Operable);
        }


    }


}
