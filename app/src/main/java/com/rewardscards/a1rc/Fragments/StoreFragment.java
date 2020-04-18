package com.rewardscards.a1rc.Fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rewardscards.a1rc.Adapters.StoreLocationAdapter;
import com.rewardscards.a1rc.Adapters.StorelocationVerAdapter;
import com.rewardscards.a1rc.MainActivity;
import com.rewardscards.a1rc.Model.LatLngBean;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.StoreLocationResponseModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreFragment extends Fragment implements OnMapReadyCallback {

    RecyclerView recycle_view,recycle_view_ver;
    ArrayList<StoreLocationResponseModel.Datum> list = new ArrayList<>();
    StoreLocationAdapter  adapter;
    StorelocationVerAdapter verAdapter;
    com.wang.avi.AVLoadingIndicatorView avi;

    private GoogleMap mMap;

    private MapView mMapView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_fragment, null);

        recycle_view = (RecyclerView)view.findViewById(R.id.recycle_view);
        recycle_view_ver = (RecyclerView)view.findViewById(R.id.recycle_view_ver);

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);
        startAnim();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<StoreLocationResponseModel> call = webApi.store_locator();
        call.enqueue(new Callback<StoreLocationResponseModel>() {
            @Override
            public void onResponse(Call<StoreLocationResponseModel> call, Response<StoreLocationResponseModel> response) {
                stopAnim();
                if (response.body().getMessage().equalsIgnoreCase("Fetch Store Details Suceessfully")){
                    for (int i =0;i < response.body().getData().size();i++){
                        list.add(response.body().getData().get(i));
                    }


                    LinearLayoutManager HorizontalLayout
                            = new LinearLayoutManager(
                            getActivity(),
                            LinearLayoutManager.HORIZONTAL,
                            false);
                    //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false);/
                    recycle_view.setLayoutManager(HorizontalLayout);
                    recycle_view.setItemAnimator(new DefaultItemAnimator());
                    adapter = new StoreLocationAdapter(getActivity(), list);
                    recycle_view.setAdapter(adapter);



                    LinearLayoutManager   HorizontalLayout1
                            = new LinearLayoutManager(
                            getActivity(),
                            LinearLayoutManager.VERTICAL,
                            false);
                    //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL,false);/
                    recycle_view_ver.setLayoutManager(HorizontalLayout1);
                    recycle_view_ver.setItemAnimator(new DefaultItemAnimator());
                    verAdapter = new StorelocationVerAdapter(getActivity(), list);
                    recycle_view_ver.setAdapter(verAdapter);

                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            mMap = mMap;
                            mMap.setMyLocationEnabled(true);


                            for (int i = 0; i <list.size();i++){
                                LatLng sydney = new LatLng(Double.parseDouble(list.get(i).getLatitude()), Double.parseDouble(list.get(i).getLongitude()));
                                mMap.addMarker(new MarkerOptions().position(sydney).title(list.get(i).getStoreName()).snippet(list.get(i).getAddress()));
                                // For zooming functionality
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                            //To add marker

                        }
                    });


                }else {
                    Utiss.showToast(getContext(),response.body().getMessage(),R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<StoreLocationResponseModel> call, Throwable t) {
                stopAnim();
                Utiss.showToast(getContext(),"Server Error",R.color.msg_fail);
            }
        });




      //  mapFragment.getMapAsync(getActivity());


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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
