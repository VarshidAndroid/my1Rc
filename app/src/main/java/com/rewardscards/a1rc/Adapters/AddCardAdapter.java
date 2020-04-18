package com.rewardscards.a1rc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.EditCardActivity;
import com.rewardscards.a1rc.Fragments.ProfileFragments;

import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreCardListResponseModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCardAdapter extends RecyclerView.Adapter<AddCardAdapter.ViewHolder> {

    List<StoreCardListResponseModel.Datum> productList;

    Context context;

    public AddCardAdapter(Context context, List<StoreCardListResponseModel.Datum> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public AddCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_card_adapter_item, null);
        AddCardAdapter.ViewHolder viewHolder = new AddCardAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddCardAdapter.ViewHolder holder, final int position) {

        holder.layout_add_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)context).pushFragment(new ProfileFragments(),"product");
                Intent intent = new Intent(context, EditCardActivity.class);
                intent.putExtra("card_id",productList.get(position).getCardId());
                intent.putExtra("name",productList.get(position).getCardName());
                context.startActivity(intent);
            }
        });

        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)context).pushFragment(new ProfileFragments(),"product");
                Intent intent = new Intent(context, EditCardActivity.class);
                intent.putExtra("card_id",productList.get(position).getCardId());
                intent.putExtra("name",productList.get(position).getCardName());
                context.startActivity(intent);
            }
        });


        holder.txt_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)context).pushFragment(new ProfileFragments(),"product");
                Intent intent = new Intent(context, EditCardActivity.class);
                intent.putExtra("card_id",productList.get(position).getCardId());
                intent.putExtra("name",productList.get(position).getCardName());
                context.startActivity(intent);
            }
        });


        holder.txt_card.setText(productList.get(position).getCardName());

        Glide
                .with(context)
                .load("https://1rewardscards.com/admin/" + productList.get(position).getCardImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.img_view);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


       // CardView main_layout;
        CircleImageView img_view;
        TextView txt_card;
        LinearLayout layout_add_details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_view = (CircleImageView)itemView.findViewById(R.id.img_view);
            txt_card = (TextView)itemView.findViewById(R.id.txt_card);
            layout_add_details = (LinearLayout)itemView.findViewById(R.id.layout_add_details);
          //  main_layout = (CardView)itemView.findViewById(R.id.main_layout);

        }

    }
}

