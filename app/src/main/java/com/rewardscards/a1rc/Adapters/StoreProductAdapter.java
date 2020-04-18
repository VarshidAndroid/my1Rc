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
import com.rewardscards.a1rc.Fragments.ProfileFragments;

import com.rewardscards.a1rc.NewMainActivity;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreProductModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;

import java.util.ArrayList;

public class StoreProductAdapter extends RecyclerView.Adapter<StoreProductAdapter.ViewHolder> {

    ArrayList<StoreProductModel.Datum> productList;

    Context context;

    public StoreProductAdapter(Context context, ArrayList<StoreProductModel.Datum> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public StoreProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_product_item, null);
        StoreProductAdapter.ViewHolder viewHolder = new StoreProductAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreProductAdapter.ViewHolder holder, final int position) {

        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtils.setSharedPreferenceString(context,"product_id",productList.get(position).getProduct_id());
                ((NewMainActivity)context).pushFragment(new ProductFragment(),"product");
            }
        });

        Glide
                .with(context)
                .load("https://1rewardscards.com/admin/" + productList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.img_product);

        holder.txt_price.setText("₹ " + productList.get(position).getPrice());
        holder.txt_msg.setText(productList.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView main_layout;
        ImageView img_product;
        TextView txt_price,txt_msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_layout = (CardView)itemView.findViewById(R.id.main_layout);
            img_product = (ImageView)itemView.findViewById(R.id.img_product);

            txt_price = (TextView)itemView.findViewById(R.id.txt_price);
            txt_msg = (TextView)itemView.findViewById(R.id.txt_msg);
        }

    }
}

