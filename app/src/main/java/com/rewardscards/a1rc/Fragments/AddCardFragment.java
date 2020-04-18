package com.rewardscards.a1rc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rewardscards.a1rc.Adapters.AddCardAdapter;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreCardListResponseModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardFragment extends Fragment {
    RecyclerView recycle_view;
    AddCardAdapter adapters;
    List<StoreCardListResponseModel.Datum> list = new ArrayList<>();
    com.wang.avi.AVLoadingIndicatorView avi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_card_fragment, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","4.1");
        recycle_view= (RecyclerView)view.findViewById(R.id.recycle_view);

        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);
        startAnim();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<StoreCardListResponseModel> call = webApi.store_cards();
        call.enqueue(new Callback<StoreCardListResponseModel>() {
            @Override
            public void onResponse(Call<StoreCardListResponseModel> call, Response<StoreCardListResponseModel> response) {
                stopAnim();
                if (response.body().getMessage().equalsIgnoreCase("Store Card Fetched Succesfully")){
                    for (int i =0; i < response.body().getData().size();i++){
                        list.add(response.body().getData().get(i));
                    }

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recycle_view.setLayoutManager(mLayoutManager);
                    recycle_view.setItemAnimator(new DefaultItemAnimator());
                    adapters = new AddCardAdapter(getActivity(),list);
                    recycle_view.setAdapter(adapters);

                }else {

                    Utiss.showToast(getContext(),response.body().getMessage(),R.color.msg_fail);;
                }
            }

            @Override
            public void onFailure(Call<StoreCardListResponseModel> call, Throwable t) {
                stopAnim();
                Utiss.showToast(getContext(),"Server Error",R.color.msg_fail);;
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
