package com.rewardscards.a1rc.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rewardscards.a1rc.Adapters.StoreProductAdapter;
import com.rewardscards.a1rc.Adapters.StoreSavedCardAdapterItem;
import com.rewardscards.a1rc.EditCardActivity;
import com.rewardscards.a1rc.MainActivity;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.NewMainActivity;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.SavedCardResponseModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.AddCAardToUSerPAram;
import com.rewardscards.a1rc.params.UerIdParam;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreSavedCardFragment extends Fragment {

    ImageView add_btn;
    RecyclerView recycle_view;
    ArrayList<SavedCardResponseModel.Datum> list =new ArrayList<>();
    StoreSavedCardAdapterItem adapter;
    com.wang.avi.AVLoadingIndicatorView avi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_saved_cards, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","4");
        recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);
        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        UerIdParam addCAardToUSerPAram=new UerIdParam(SharedPrefsUtils.getSharedPreferenceString(getContext(),"user_id"));
        startAnim();
        Call<SavedCardResponseModel> call  = webApi.users_card(addCAardToUSerPAram);
        call.enqueue(new Callback<SavedCardResponseModel>() {
            @Override
            public void onResponse(Call<SavedCardResponseModel> call, Response<SavedCardResponseModel> response) {
                stopAnim();
                if (response.body().getMessage().equalsIgnoreCase("Card Not Found")){
                    Utiss.showToast(getContext(),"Card Not Found",R.color.msg_fail);
                }else {
                    for (int i =0; i < response.body().getData().size();i++){
                        list.add(response.body().getData().get(i));
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    //recyclerView.setLayoutManager(gridLayoutManager);


                    //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recycle_view.setLayoutManager(gridLayoutManager);
                    recycle_view.setItemAnimator(new DefaultItemAnimator());
                    recycle_view.setHasFixedSize(true);
                    adapter = new StoreSavedCardAdapterItem(getActivity(), list);
                    recycle_view.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<SavedCardResponseModel> call, Throwable t) {
                stopAnim();
                Utiss.showToast(getContext(),"Server Error",R.color.msg_fail);
            }
        });





        add_btn = (ImageView)view.findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewMainActivity)getActivity()).pushFragment(new AddCardFragment(), "save");
            }
        });

                /*Intent intent =  new Intent(getActivity(), EditCardActivity.class);
                startActivity(intent);*/

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
