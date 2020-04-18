package com.rewardscards.a1rc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rewardscards.a1rc.Adapters.SideMenuAdapterItem;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreListModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SidemenuStore extends Fragment {
    RecyclerView recycle_view;
    ArrayList<StoreListModel.Datum> list = new ArrayList<>();
    SideMenuAdapterItem adapter;
    String group_id,cat_id;



    com.wang.avi.AVLoadingIndicatorView avi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.side_menu_fragment, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","0.0");

        group_id  = SharedPrefsUtils.getSharedPreferenceString(getContext(),"sidemenu_group");
        cat_id  = SharedPrefsUtils.getSharedPreferenceString(getContext(),"sidemenu_sub");
        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);
        startAnim();
        recycle_view = (RecyclerView)view.findViewById(R.id.recycle_view);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        com.rewardscards.a1rc.params.StoreListModel storeListModel = new com.rewardscards.a1rc.params.StoreListModel(group_id,cat_id);
        Call<StoreListModel> call = webApi.fetch_store_from_group(storeListModel);
        call.enqueue(new Callback<StoreListModel>() {
            @Override
            public void onResponse(Call<StoreListModel> call, Response<StoreListModel> response) {


                for  (int i =0; i < response.body().getData().size();i++){
                    list.add(response.body().getData().get(i));
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
                //recyclerView.setLayoutManager(gridLayoutManager);


                //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recycle_view.setLayoutManager(gridLayoutManager);
                recycle_view.setItemAnimator(new DefaultItemAnimator());
                adapter = new SideMenuAdapterItem(getActivity(), list);
                recycle_view.setAdapter(adapter);
                stopAnim();
            }

            @Override
            public void onFailure(Call<StoreListModel> call, Throwable t) {
                stopAnim();
                //Toast.makeText(getContext(),"No Store Found",Toast.LENGTH_LONG).show();
                Utiss.showToast(getContext(),"No Store Found",R.color.msg_fail);
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
