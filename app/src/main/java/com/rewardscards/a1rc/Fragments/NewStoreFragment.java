package com.rewardscards.a1rc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rewardscards.a1rc.Adapters.StoreNewAdapter;
import com.rewardscards.a1rc.Adapters.StoreProductAdapter;
import com.rewardscards.a1rc.Model.NewProuctAdResponse;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreProductModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.ResponseModelCode;
import com.rewardscards.a1rc.params.StoreProductParam;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewStoreFragment extends Fragment {
    RecyclerView recycle_view;
    ArrayList<NewProuctAdResponse.Datum> list = new ArrayList<>();
    StoreNewAdapter adapter;
    ImageView img_back;
    String store_number,store_group_id,store_category_id;
    com.wang.avi.AVLoadingIndicatorView avi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_store_home_fragmnt, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","0.5555");
        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);
        store_group_id  = SharedPrefsUtils.getSharedPreferenceString(getContext(),"sidemenu_group");
        store_category_id  = SharedPrefsUtils.getSharedPreferenceString(getContext(),"sidemenu_sub");
        store_number  = SharedPrefsUtils.getSharedPreferenceString(getContext(),"new_kkkkk");

        startAnim();
        ResponseModelCode storeProductParam = new ResponseModelCode(store_number);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);

        Call<NewProuctAdResponse> call = webApi.fetch_adverts_store_product_new(storeProductParam);
        call.enqueue(new Callback<NewProuctAdResponse>() {
            @Override
            public void onResponse(Call<NewProuctAdResponse> call, Response<NewProuctAdResponse> response) {

                if (response.body().getMessage().equalsIgnoreCase("Fetch Product Succesfully")){
                    for (int i =0; i < response.body().getData().size();i++){
                        list.add(response.body().getData().get(i));
                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    //recyclerView.setLayoutManager(gridLayoutManager);


                    //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recycle_view.setLayoutManager(gridLayoutManager);
                    recycle_view.setItemAnimator(new DefaultItemAnimator());
                    adapter = new StoreNewAdapter(getActivity(), list);
                    recycle_view.setAdapter(adapter);

                }else {
                    Utiss.showToast(getContext(),"Product Not Found",R.color.msg_fail);
                }

                stopAnim();
            }

            @Override
            public void onFailure(Call<NewProuctAdResponse> call, Throwable t) {
                stopAnim();
                //Toast.makeText(getContext(),"No Store Found",Toast.LENGTH_LONG).show();
                Utiss.showToast(getContext(),"No Store Product Found",R.color.msg_fail);
            }
        });




// set a GridLayoutManager with default vertical orientation and 3 number of columns


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
