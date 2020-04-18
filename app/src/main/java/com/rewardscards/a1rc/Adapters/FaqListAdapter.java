package com.rewardscards.a1rc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.FaqActivity;
import com.rewardscards.a1rc.Fragments.ProductFragment;

import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.FAQLISTREsponseModel;
import com.rewardscards.a1rc.Response.StoreProductModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;

import java.util.ArrayList;

public class FaqListAdapter extends RecyclerView.Adapter<FaqListAdapter.ViewHolder> {

    ArrayList<FAQLISTREsponseModel.Datum> productList;
    int a = 1;
    Context context;

    public FaqListAdapter(Context context, ArrayList<FAQLISTREsponseModel.Datum> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public FaqListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_list_item, null);
        FaqListAdapter.ViewHolder viewHolder = new FaqListAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqListAdapter.ViewHolder holder, final int position) {

        holder.img123.setImageResource(R.drawable.ic_faq_arrow);
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, FaqActivity.class);
                intent.putExtra("q_id",productList.get(position).getQ_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/


                if (holder.img123.getDrawable().getConstantState() !=
                        context.getResources().getDrawable(R.drawable.ic_faq_arrow).getConstantState())
                {
                    if (a == 1){
                        holder.txt_data.setVisibility(View.VISIBLE);
                        holder.img123.setImageResource(R.drawable.down_arrow);
                        a = 2;
                    }else if(a == 2){
                        holder.txt_data.setVisibility(View.GONE);
                        holder.img123.setImageResource(R.drawable.ic_faq_arrow);
                        a = 1;
                    }

                }else {

                }

            }
        });



        holder.txt_text.setText(" " + productList.get(position).getQuestion());
        holder.txt_data.setText(productList.get(position).getAnswer());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        RelativeLayout main_layout;
        TextView txt_text,txt_data;
        ImageView img123;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
            txt_text = (TextView)itemView.findViewById(R.id.txt_text);
            txt_data = (TextView)itemView.findViewById(R.id.txt_data);
            img123 = (ImageView)itemView.findViewById(R.id.img123);
        }

    }
}

