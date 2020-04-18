package com.rewardscards.a1rc.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.Fragments.HomeFragments;
import com.rewardscards.a1rc.Fragments.ProductFragment;
import com.rewardscards.a1rc.Fragments.StoreProductFragment;

import com.rewardscards.a1rc.NewMainActivity;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreListModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;

import java.util.ArrayList;

public class SideMenuAdapterItem extends RecyclerView.Adapter<SideMenuAdapterItem.ViewHolder> {

    ArrayList<StoreListModel.Datum> productList;
    Context context;

    public SideMenuAdapterItem(Context context, ArrayList<StoreListModel.Datum> productList) {
        this.context = context;
        this.productList = productList;


    }


    @NonNull
    @Override
    public SideMenuAdapterItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_menu_store_item, null);
        SideMenuAdapterItem.ViewHolder viewHolder = new SideMenuAdapterItem.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SideMenuAdapterItem.ViewHolder holder, final int position) {

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)context).pushFragment(new ProductFragment(),"Home");
                SharedPrefsUtils.setSharedPreferenceString(context,"store_number", productList.get(position).getStoreNumber());
                ((NewMainActivity)context).pushFragment(new StoreProductFragment(),"store");

            }
        });

        Bitmap mbitmap=((BitmapDrawable) context.getResources().getDrawable(R.drawable.sale)).getBitmap();
        Bitmap imageRounded=Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas=new Canvas(imageRounded);
        Paint mpaint=new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 180, 180, mpaint); // Round Image Corner 100 100 100 100

        holder.img_store.setImageBitmap(imageRounded);
        Glide
                .with(context)
                .load("https://1rewardscards.com/admin/" + productList.get(position).getStoreImage())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.img_store);




        //holder.img_store


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        RelativeLayout layout_main;
        ImageView  img_store;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_main = (RelativeLayout) itemView.findViewById(R.id.layout_main);
            img_store = (ImageView)itemView.findViewById(R.id.img_store);
        }

    }
}
