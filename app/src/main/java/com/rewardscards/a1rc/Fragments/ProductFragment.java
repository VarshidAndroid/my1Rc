package com.rewardscards.a1rc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.MainActivity;
import com.rewardscards.a1rc.Model.StoreProductDetilsModel;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.ProductDetails;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends Fragment {
    LinearLayout layout_line_one;
    String product_id;
    com.wang.avi.AVLoadingIndicatorView avi;
    ImageView img_product;
    TextView txt_product_price,txt_product_,txt_product_Details;
    LinearLayout layout_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_details_fragment, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","0.2");
        product_id = SharedPrefsUtils.getSharedPreferenceString(getContext(),"product_id");
        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);
        img_product = (ImageView)view.findViewById(R.id.img_product);
        txt_product_price = (TextView)view.findViewById(R.id.txt_product_price);
        txt_product_ = (TextView)view.findViewById(R.id.txt_product_);
        txt_product_Details = (TextView)view.findViewById(R.id.txt_product_Details);
        layout_data = (LinearLayout)view.findViewById(R.id.layout_data);
        startAnim();
        ProductDetails productDetails = new ProductDetails(product_id,
                SharedPrefsUtils.getSharedPreferenceString(getContext(),"user_id"));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<StoreProductDetilsModel> call = webApi.fetch_product_details(productDetails);
        call.enqueue(new Callback<StoreProductDetilsModel>() {
            @Override
            public void onResponse(Call<StoreProductDetilsModel> call, Response<StoreProductDetilsModel> response) {
                stopAnim();
                if (response.body().getMessage().equalsIgnoreCase("product Details Not Found!!")){
                    Utiss.showToast(getContext(),response.body().getMessage(),R.color.msg_fail);
                    layout_data.setVisibility(View.GONE);
                    txt_product_Details.setVisibility(View.GONE);
                }else {
                    try {
                        layout_data.setVisibility(View.VISIBLE);
                        txt_product_Details.setVisibility(View.VISIBLE);

                        Glide
                                .with(getContext())
                                .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getImage())
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(img_product);

                        txt_product_price.setText("₹ " + response.body().getData().get(0).getPrice());
                        txt_product_.setText(response.body().getData().get(0).getProductName());
                        txt_product_Details.setText(response.body().getData().get(0).getDescription());
                    }catch (Exception e){
                        Utiss.showToast(getContext(),"Network Error",R.color.msg_fail);
                        e.printStackTrace();
                    }



                }
            }

            @Override
            public void onFailure(Call<StoreProductDetilsModel> call, Throwable t) {
                stopAnim();
                Utiss.showToast(getContext(),"Server Error",R.color.msg_fail);
            }
        });

        return view;
    }

    void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }

}
