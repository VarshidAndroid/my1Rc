package com.rewardscards.a1rc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreLocationResponseModel;

import java.util.ArrayList;

public class StorelocationVerAdapter extends RecyclerView.Adapter<StorelocationVerAdapter.ViewHolder> {

    ArrayList<StoreLocationResponseModel.Datum> productList;

    Context context;

    public StorelocationVerAdapter(Context context, ArrayList<StoreLocationResponseModel.Datum> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public StorelocationVerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_hori_ite, null);
        StorelocationVerAdapter.ViewHolder viewHolder = new StorelocationVerAdapter.ViewHolder(itemLayoutView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StorelocationVerAdapter.ViewHolder holder, final int position) {

        holder.txt_head.setText("@"+productList.get(position).getStoreName());
        holder.txt_head_con.setText(productList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        LinearLayout layout_main;
        TextView txt_head,txt_head_con;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_head = (TextView)itemView.findViewById(R.id.txt_head);
            txt_head_con = (TextView)itemView.findViewById(R.id.txt_head_con);

        }

    }
}

