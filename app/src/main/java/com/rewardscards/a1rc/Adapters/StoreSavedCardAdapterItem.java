package com.rewardscards.a1rc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.Fragments.ProductFragment;

import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.SavedCardResponseModel;
import com.rewardscards.a1rc.Response.StoreProductModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;

import java.util.ArrayList;

public class StoreSavedCardAdapterItem extends RecyclerView.Adapter<StoreSavedCardAdapterItem.ViewHolder> {

    ArrayList<SavedCardResponseModel.Datum> productList;

    Context context;

    public StoreSavedCardAdapterItem(Context context, ArrayList<SavedCardResponseModel.Datum> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public StoreSavedCardAdapterItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_saved_cards_item, null);
        StoreSavedCardAdapterItem.ViewHolder viewHolder = new StoreSavedCardAdapterItem.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreSavedCardAdapterItem.ViewHolder holder, final int position) {

    holder.txt_data.setText(productList.get(position).getCardName());


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_data = (TextView)itemView.findViewById(R.id.txt_data);
        }

    }
}

