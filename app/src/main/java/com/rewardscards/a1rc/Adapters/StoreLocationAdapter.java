package com.rewardscards.a1rc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreLocationResponseModel;

import java.util.ArrayList;

public class StoreLocationAdapter extends RecyclerView.Adapter<StoreLocationAdapter.ViewHolder> {

    ArrayList<StoreLocationResponseModel.Datum> productList;

    Context context;

    public StoreLocationAdapter(Context context, ArrayList<StoreLocationResponseModel.Datum> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public StoreLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_location_item, null);
        StoreLocationAdapter.ViewHolder viewHolder = new StoreLocationAdapter.ViewHolder(itemLayoutView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreLocationAdapter.ViewHolder holder, final int position) {

        Glide
                .with(context)
                .load("https://1rewardscards.com/admin/" + productList.get(position).getStoreImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.img_data);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView img_data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_data = (ImageView)itemView.findViewById(R.id.img_data);
        }

    }
}

