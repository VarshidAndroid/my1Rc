package com.rewardscards.a1rc.Fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.IOnLoadLocationListener;
import com.rewardscards.a1rc.MainActivity;
import com.rewardscards.a1rc.Model.MyLatLng;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.NewMainActivity;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.HomeBanner;
import com.rewardscards.a1rc.Response.StoreLocationResponseModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragments extends Fragment implements OnMapReadyCallback, GeoQueryEventListener, IOnLoadLocationListener {

    ArrayList<String> arrayList = new ArrayList<>();
    ImageView img_1, img_2, img_3, img_4, img_5, img_6, img_7, img_8;

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Marker currentUser;
    private DatabaseReference myLocationRef;
    private GeoFire geoFire;
    private List<LatLng> Near_Mall_Area;
    RelativeLayout layotu_1;
    ArrayList<MyLatLng> list = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private IOnLoadLocationListener listener;
    ProgressDialog progressDialog;
    private DatabaseReference myCity;
    ArrayList<String> listdddddddddd = new ArrayList<>();
    private Location lastLocation;
    MapView mMapView;
    int countr_new;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, null);
        layotu_1 = (RelativeLayout)view.findViewById(R.id.layotu_1);
        img_1 = (ImageView) view.findViewById(R.id.img_1);
        img_2 = (ImageView) view.findViewById(R.id.img_2);
        img_3 = (ImageView) view.findViewById(R.id.img_3);
        img_4 = (ImageView) view.findViewById(R.id.img_4);
        img_5 = (ImageView) view.findViewById(R.id.img_5);
        img_6 = (ImageView) view.findViewById(R.id.img_6);
        img_7 = (ImageView) view.findViewById(R.id.img_7);
        img_8 = (ImageView) view.findViewById(R.id.img_8);






        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
        final Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mpaint = new Paint();
        mpaint.setAntiAlias(true);
        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<HomeBanner> call = webApi.fetch_advertise();
        call.enqueue(new Callback<HomeBanner>() {
            @Override
            public void onResponse(Call<HomeBanner> call, Response<HomeBanner> response) {
                progressDialog.dismiss();
                for (int i =0;i <response.body().getData().size();i++){
                    listdddddddddd.add(response.body().getData().get(i).getStore_number());
                }
                countr_new = Integer.parseInt(response.body().getTotalAdverts());
                if (response.body().getTotalAdverts().equals("1")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.GONE);
                    img_3.setVisibility(View.GONE);
                    img_4.setVisibility(View.GONE);
                    img_5.setVisibility(View.GONE);
                    img_6.setVisibility(View.GONE);
                    img_7.setVisibility(View.GONE);
                    img_8.setVisibility(View.GONE);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);

                } else if (response.body().getTotalAdverts().equals("2")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.VISIBLE);
                    img_3.setVisibility(View.GONE);
                    img_4.setVisibility(View.GONE);
                    img_5.setVisibility(View.GONE);
                    img_6.setVisibility(View.GONE);
                    img_7.setVisibility(View.GONE);
                    img_8.setVisibility(View.GONE);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);


                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(1).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_2);
                } else if (response.body().getTotalAdverts().equals("3")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.VISIBLE);
                    img_3.setVisibility(View.VISIBLE);
                    img_4.setVisibility(View.GONE);
                    img_5.setVisibility(View.GONE);
                    img_6.setVisibility(View.GONE);
                    img_7.setVisibility(View.GONE);
                    img_8.setVisibility(View.GONE);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(1).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_2);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(2).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_3);


                } else if (response.body().getTotalAdverts().equals("4")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.VISIBLE);
                    img_3.setVisibility(View.VISIBLE);
                    img_4.setVisibility(View.VISIBLE);
                    img_5.setVisibility(View.GONE);
                    img_6.setVisibility(View.GONE);
                    img_7.setVisibility(View.GONE);
                    img_8.setVisibility(View.GONE);


                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(1).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_2);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(2).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_3);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(3).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_4);
                } else if (response.body().getTotalAdverts().equals("5")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.VISIBLE);
                    img_3.setVisibility(View.VISIBLE);
                    img_4.setVisibility(View.VISIBLE);
                    img_5.setVisibility(View.VISIBLE);
                    img_6.setVisibility(View.GONE);
                    img_7.setVisibility(View.GONE);
                    img_8.setVisibility(View.GONE);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(1).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_2);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(2).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_3);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(3).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_4);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(4).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_5);
                } else if (response.body().getTotalAdverts().equals("6")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.VISIBLE);
                    img_3.setVisibility(View.VISIBLE);
                    img_4.setVisibility(View.VISIBLE);
                    img_5.setVisibility(View.VISIBLE);
                    img_6.setVisibility(View.VISIBLE);
                    img_7.setVisibility(View.GONE);
                    img_8.setVisibility(View.GONE);


                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(1).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_2);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(2).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_3);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(3).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_4);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(4).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_5);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(5).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_6);

                } else if (response.body().getTotalAdverts().equals("7")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.VISIBLE);
                    img_3.setVisibility(View.VISIBLE);
                    img_4.setVisibility(View.VISIBLE);
                    img_5.setVisibility(View.VISIBLE);
                    img_6.setVisibility(View.VISIBLE);
                    img_7.setVisibility(View.VISIBLE);
                    img_8.setVisibility(View.GONE);


                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(1).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_2);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(2).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_3);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(3).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_4);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(4).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_5);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(5).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_6);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(6).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_7);

                } else if (response.body().getTotalAdverts().equals("8")) {
                    img_1.setVisibility(View.VISIBLE);
                    img_2.setVisibility(View.VISIBLE);
                    img_3.setVisibility(View.VISIBLE);
                    img_4.setVisibility(View.VISIBLE);
                    img_5.setVisibility(View.VISIBLE);
                    img_6.setVisibility(View.VISIBLE);
                    img_7.setVisibility(View.VISIBLE);
                    img_8.setVisibility(View.VISIBLE);


                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(0).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_1);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(1).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_2);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(2).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_3);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(3).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_4);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(4).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_5);
                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(5).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_6);

                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(6).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_7);


                    Glide
                            .with(getContext())
                            .load("https://1rewardscards.com/admin/" + response.body().getData().get(7).getAdsImage())
                            .centerCrop()
                            .placeholder(R.drawable.sale)
                            .into(img_8);
                }
            }

            @Override
            public void onFailure(Call<HomeBanner> call, Throwable t) {
                progressDialog.dismiss();
                Utiss.showToast(getContext(), "Network Error", R.color.msg_fail);
            }
        });


        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 1){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(0)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

        img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 2){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(1)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

        img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 3){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(2)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

        img_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 4){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(3)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

        img_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 5){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(4)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

        img_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 6){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(5)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

        img_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 7){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(6)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

        img_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countr_new >= 8){
                    SharedPrefsUtils.setSharedPreferenceString(getActivity(),"new_kkkkk", String.valueOf(listdddddddddd.get(7)));
                    ((NewMainActivity)getActivity()).pushFragment(new NewStoreFragment(),"new");
                }

            }
        });

    /*    initArea();
        settingGeoFire();


        FirebaseDatabase.getInstance()
                .getReference("Near_Mall_Area")
                .child("MyCity")
                .setValue(Near_Mall_Area)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                    }
                });


        FirebaseDatabase.getInstance().getReference("Near_Mall_Area")
                .child("MyCity")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        List<MyLatLng> latLngList = new ArrayList<>();
                        for (DataSnapshot locationSnapshot : dataSnapshot.getChildren())
                        {
                            MyLatLng latLng = locationSnapshot.getValue(MyLatLng.class);
                            latLngList.add(latLng);
                        }
                        listener.onLoadLocationSuccess(latLngList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onLoadLocationFailed(databaseError.getMessage());
                    }
                });*/


/*        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<HomeBanner> call = webApi.fetch_advertise();
        call.enqueue(new Callback<HomeBanner>() {
            @Override
            public void onResponse(Call<HomeBanner> call, Response<HomeBanner> response) {
                if (response.body().getMessage().equalsIgnoreCase("Fetch Adverts Images Suceessfully")) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        arrayList.add(response.body().getData().get(i).getAdsImage());
                    }

                    if (arrayList.size() == 1) {
                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        img_1.setImageBitmap(imageRounded);
                    } else if (arrayList.size() == 2) {

                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded1 = Bitmap.createBitmap(mbitmap1.getWidth(), mbitmap1.getHeight(), mbitmap1.getConfig());
                        Canvas canvas1 = new Canvas(imageRounded1);
                        Paint mpaint1 = new Paint();
                        mpaint1.setAntiAlias(true);
                        mpaint1.setShader(new BitmapShader(mbitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas1.drawRoundRect((new RectF(0, 0, mbitmap1.getWidth(), mbitmap1.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                    } else if (arrayList.size() == 3) {

                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded1 = Bitmap.createBitmap(mbitmap1.getWidth(), mbitmap1.getHeight(), mbitmap1.getConfig());
                        Canvas canvas1 = new Canvas(imageRounded1);
                        Paint mpaint1 = new Paint();
                        mpaint1.setAntiAlias(true);
                        mpaint1.setShader(new BitmapShader(mbitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas1.drawRoundRect((new RectF(0, 0, mbitmap1.getWidth(), mbitmap1.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded2 = Bitmap.createBitmap(mbitmap2.getWidth(), mbitmap2.getHeight(), mbitmap2.getConfig());
                        Canvas canvas2 = new Canvas(imageRounded2);
                        Paint mpaint2 = new Paint();
                        mpaint2.setAntiAlias(true);
                        mpaint2.setShader(new BitmapShader(mbitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas2.drawRoundRect((new RectF(0, 0, mbitmap2.getWidth(), mbitmap2.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                    } else if (arrayList.size() == 4) {


                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded1 = Bitmap.createBitmap(mbitmap1.getWidth(), mbitmap1.getHeight(), mbitmap1.getConfig());
                        Canvas canvas1 = new Canvas(imageRounded1);
                        Paint mpaint1 = new Paint();
                        mpaint1.setAntiAlias(true);
                        mpaint1.setShader(new BitmapShader(mbitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas1.drawRoundRect((new RectF(0, 0, mbitmap1.getWidth(), mbitmap1.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded2 = Bitmap.createBitmap(mbitmap2.getWidth(), mbitmap2.getHeight(), mbitmap2.getConfig());
                        Canvas canvas2 = new Canvas(imageRounded2);
                        Paint mpaint2 = new Paint();
                        mpaint2.setAntiAlias(true);
                        mpaint2.setShader(new BitmapShader(mbitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas2.drawRoundRect((new RectF(0, 0, mbitmap2.getWidth(), mbitmap2.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded3 = Bitmap.createBitmap(mbitmap3.getWidth(), mbitmap3.getHeight(), mbitmap3.getConfig());
                        Canvas canvas3 = new Canvas(imageRounded3);
                        Paint mpaint3 = new Paint();
                        mpaint3.setAntiAlias(true);
                        mpaint3.setShader(new BitmapShader(mbitmap3, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas3.drawRoundRect((new RectF(0, 0, mbitmap3.getWidth(), mbitmap3.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                    } else if (arrayList.size() == 5) {

                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded1 = Bitmap.createBitmap(mbitmap1.getWidth(), mbitmap1.getHeight(), mbitmap1.getConfig());
                        Canvas canvas1 = new Canvas(imageRounded1);
                        Paint mpaint1 = new Paint();
                        mpaint1.setAntiAlias(true);
                        mpaint1.setShader(new BitmapShader(mbitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas1.drawRoundRect((new RectF(0, 0, mbitmap1.getWidth(), mbitmap1.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded2 = Bitmap.createBitmap(mbitmap2.getWidth(), mbitmap2.getHeight(), mbitmap2.getConfig());
                        Canvas canvas2 = new Canvas(imageRounded2);
                        Paint mpaint2 = new Paint();
                        mpaint2.setAntiAlias(true);
                        mpaint2.setShader(new BitmapShader(mbitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas2.drawRoundRect((new RectF(0, 0, mbitmap2.getWidth(), mbitmap2.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded3 = Bitmap.createBitmap(mbitmap3.getWidth(), mbitmap3.getHeight(), mbitmap3.getConfig());
                        Canvas canvas3 = new Canvas(imageRounded3);
                        Paint mpaint3 = new Paint();
                        mpaint3.setAntiAlias(true);
                        mpaint3.setShader(new BitmapShader(mbitmap3, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas3.drawRoundRect((new RectF(0, 0, mbitmap3.getWidth(), mbitmap3.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded4 = Bitmap.createBitmap(mbitmap4.getWidth(), mbitmap4.getHeight(), mbitmap4.getConfig());
                        Canvas canvas4 = new Canvas(imageRounded4);
                        Paint mpaint4 = new Paint();
                        mpaint4.setAntiAlias(true);
                        mpaint4.setShader(new BitmapShader(mbitmap4, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas4.drawRoundRect((new RectF(0, 0, mbitmap4.getWidth(), mbitmap4.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                    } else if (arrayList.size() == 6) {

                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded1 = Bitmap.createBitmap(mbitmap1.getWidth(), mbitmap1.getHeight(), mbitmap1.getConfig());
                        Canvas canvas1 = new Canvas(imageRounded1);
                        Paint mpaint1 = new Paint();
                        mpaint1.setAntiAlias(true);
                        mpaint1.setShader(new BitmapShader(mbitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas1.drawRoundRect((new RectF(0, 0, mbitmap1.getWidth(), mbitmap1.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded2 = Bitmap.createBitmap(mbitmap2.getWidth(), mbitmap2.getHeight(), mbitmap2.getConfig());
                        Canvas canvas2 = new Canvas(imageRounded2);
                        Paint mpaint2 = new Paint();
                        mpaint2.setAntiAlias(true);
                        mpaint2.setShader(new BitmapShader(mbitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas2.drawRoundRect((new RectF(0, 0, mbitmap2.getWidth(), mbitmap2.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded3 = Bitmap.createBitmap(mbitmap3.getWidth(), mbitmap3.getHeight(), mbitmap3.getConfig());
                        Canvas canvas3 = new Canvas(imageRounded3);
                        Paint mpaint3 = new Paint();
                        mpaint3.setAntiAlias(true);
                        mpaint3.setShader(new BitmapShader(mbitmap3, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas3.drawRoundRect((new RectF(0, 0, mbitmap3.getWidth(), mbitmap3.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded4 = Bitmap.createBitmap(mbitmap4.getWidth(), mbitmap4.getHeight(), mbitmap4.getConfig());
                        Canvas canvas4 = new Canvas(imageRounded4);
                        Paint mpaint4 = new Paint();
                        mpaint4.setAntiAlias(true);
                        mpaint4.setShader(new BitmapShader(mbitmap4, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas4.drawRoundRect((new RectF(0, 0, mbitmap4.getWidth(), mbitmap4.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded5 = Bitmap.createBitmap(mbitmap5.getWidth(), mbitmap5.getHeight(), mbitmap5.getConfig());
                        Canvas canvas5 = new Canvas(imageRounded5);
                        Paint mpaint5 = new Paint();
                        mpaint5.setAntiAlias(true);
                        mpaint5.setShader(new BitmapShader(mbitmap5, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas5.drawRoundRect((new RectF(0, 0, mbitmap5.getWidth(), mbitmap5.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                    } else if (arrayList.size() == 7) {

                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded1 = Bitmap.createBitmap(mbitmap1.getWidth(), mbitmap1.getHeight(), mbitmap1.getConfig());
                        Canvas canvas1 = new Canvas(imageRounded1);
                        Paint mpaint1 = new Paint();
                        mpaint1.setAntiAlias(true);
                        mpaint1.setShader(new BitmapShader(mbitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas1.drawRoundRect((new RectF(0, 0, mbitmap1.getWidth(), mbitmap1.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded2 = Bitmap.createBitmap(mbitmap2.getWidth(), mbitmap2.getHeight(), mbitmap2.getConfig());
                        Canvas canvas2 = new Canvas(imageRounded2);
                        Paint mpaint2 = new Paint();
                        mpaint2.setAntiAlias(true);
                        mpaint2.setShader(new BitmapShader(mbitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas2.drawRoundRect((new RectF(0, 0, mbitmap2.getWidth(), mbitmap2.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded3 = Bitmap.createBitmap(mbitmap3.getWidth(), mbitmap3.getHeight(), mbitmap3.getConfig());
                        Canvas canvas3 = new Canvas(imageRounded3);
                        Paint mpaint3 = new Paint();
                        mpaint3.setAntiAlias(true);
                        mpaint3.setShader(new BitmapShader(mbitmap3, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas3.drawRoundRect((new RectF(0, 0, mbitmap3.getWidth(), mbitmap3.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded4 = Bitmap.createBitmap(mbitmap4.getWidth(), mbitmap4.getHeight(), mbitmap4.getConfig());
                        Canvas canvas4 = new Canvas(imageRounded4);
                        Paint mpaint4 = new Paint();
                        mpaint4.setAntiAlias(true);
                        mpaint4.setShader(new BitmapShader(mbitmap4, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas4.drawRoundRect((new RectF(0, 0, mbitmap4.getWidth(), mbitmap4.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded5 = Bitmap.createBitmap(mbitmap5.getWidth(), mbitmap5.getHeight(), mbitmap5.getConfig());
                        Canvas canvas5 = new Canvas(imageRounded5);
                        Paint mpaint5 = new Paint();
                        mpaint5.setAntiAlias(true);
                        mpaint5.setShader(new BitmapShader(mbitmap5, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas5.drawRoundRect((new RectF(0, 0, mbitmap5.getWidth(), mbitmap5.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap6 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded6 = Bitmap.createBitmap(mbitmap6.getWidth(), mbitmap6.getHeight(), mbitmap6.getConfig());
                        Canvas canvas6 = new Canvas(imageRounded6);
                        Paint mpaint6 = new Paint();
                        mpaint6.setAntiAlias(true);
                        mpaint6.setShader(new BitmapShader(mbitmap6, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas6.drawRoundRect((new RectF(0, 0, mbitmap6.getWidth(), mbitmap6.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100



                    } else if (arrayList.size() == 8) {

                        Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded1 = Bitmap.createBitmap(mbitmap1.getWidth(), mbitmap1.getHeight(), mbitmap1.getConfig());
                        Canvas canvas1 = new Canvas(imageRounded1);
                        Paint mpaint1 = new Paint();
                        mpaint1.setAntiAlias(true);
                        mpaint1.setShader(new BitmapShader(mbitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas1.drawRoundRect((new RectF(0, 0, mbitmap1.getWidth(), mbitmap1.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                        Bitmap mbitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded2 = Bitmap.createBitmap(mbitmap2.getWidth(), mbitmap2.getHeight(), mbitmap2.getConfig());
                        Canvas canvas2 = new Canvas(imageRounded2);
                        Paint mpaint2 = new Paint();
                        mpaint2.setAntiAlias(true);
                        mpaint2.setShader(new BitmapShader(mbitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas2.drawRoundRect((new RectF(0, 0, mbitmap2.getWidth(), mbitmap2.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded3 = Bitmap.createBitmap(mbitmap3.getWidth(), mbitmap3.getHeight(), mbitmap3.getConfig());
                        Canvas canvas3 = new Canvas(imageRounded3);
                        Paint mpaint3 = new Paint();
                        mpaint3.setAntiAlias(true);
                        mpaint3.setShader(new BitmapShader(mbitmap3, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas3.drawRoundRect((new RectF(0, 0, mbitmap3.getWidth(), mbitmap3.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded4 = Bitmap.createBitmap(mbitmap4.getWidth(), mbitmap4.getHeight(), mbitmap4.getConfig());
                        Canvas canvas4 = new Canvas(imageRounded4);
                        Paint mpaint4 = new Paint();
                        mpaint4.setAntiAlias(true);
                        mpaint4.setShader(new BitmapShader(mbitmap4, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas4.drawRoundRect((new RectF(0, 0, mbitmap4.getWidth(), mbitmap4.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded5 = Bitmap.createBitmap(mbitmap5.getWidth(), mbitmap5.getHeight(), mbitmap5.getConfig());
                        Canvas canvas5 = new Canvas(imageRounded5);
                        Paint mpaint5 = new Paint();
                        mpaint5.setAntiAlias(true);
                        mpaint5.setShader(new BitmapShader(mbitmap5, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas5.drawRoundRect((new RectF(0, 0, mbitmap5.getWidth(), mbitmap5.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap6 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded6 = Bitmap.createBitmap(mbitmap6.getWidth(), mbitmap6.getHeight(), mbitmap6.getConfig());
                        Canvas canvas6 = new Canvas(imageRounded6);
                        Paint mpaint6 = new Paint();
                        mpaint6.setAntiAlias(true);
                        mpaint6.setShader(new BitmapShader(mbitmap6, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas6.drawRoundRect((new RectF(0, 0, mbitmap6.getWidth(), mbitmap6.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100

                        Bitmap mbitmap7 = ((BitmapDrawable) getResources().getDrawable(R.drawable.sale)).getBitmap();
                        Bitmap imageRounded7 = Bitmap.createBitmap(mbitmap7.getWidth(), mbitmap7.getHeight(), mbitmap7.getConfig());
                        Canvas canvas7 = new Canvas(imageRounded7);
                        Paint mpaint7 = new Paint();
                        mpaint7.setAntiAlias(true);
                        mpaint7.setShader(new BitmapShader(mbitmap7, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas7.drawRoundRect((new RectF(0, 0, mbitmap7.getWidth(), mbitmap7.getHeight())), 80, 80, mpaint); // Round Image Corner 100 100 100 100


                    }

                } else {
                    Utiss.showToast(getContext(), response.body().getMessage(), R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<HomeBanner> call, Throwable t) {
                Utiss.showToast(getContext(), "Server Error", R.color.msg_fail);
            }
        });*/


        img_1.setImageBitmap(imageRounded);
        img_2.setImageBitmap(imageRounded);
        img_3.setImageBitmap(imageRounded);
        img_4.setImageBitmap(imageRounded);

        img_5.setImageBitmap(imageRounded);
        img_6.setImageBitmap(imageRounded);
        img_7.setImageBitmap(imageRounded);
        img_8.setImageBitmap(imageRounded);
        return view;
    }

    private void settingGeoFire() {
        myLocationRef = FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFire = new GeoFire(myLocationRef);
    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }


    private void initArea() {


        myCity = FirebaseDatabase.getInstance()
                .getReference("Near_Mall_Area")
                .child("MyCity");


        listener = this;


        myCity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<MyLatLng> latLngList = new ArrayList<>();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    MyLatLng latLng = locationSnapshot.getValue(MyLatLng.class);
                    latLngList.add(latLng);
                }
                listener.onLoadLocationSuccess(latLngList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onLoadLocationFailed(databaseError.getMessage());
            }
        });
        myCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mMap != null) {
                    mMap.clear();

                    geoFire.setLocation("YOU", new GeoLocation(lastLocation.getLatitude(),
                            lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (currentUser != null) currentUser.remove();
                            currentUser = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lastLocation.getLatitude(),
                                            lastLocation.getLongitude()))
                                    .title("YOU"));

                            //After add marker ...... move camera

                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(currentUser.getPosition(), 12.0f));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    @Override
    public void onLoadLocationFailed(String message) {

    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("AP-Group", String.format("%s enterd the Mall area", key));
    }

    @Override
    public void onKeyExited(String key) {
        sendNotification("AP-Group", String.format("%s Leave the Mall area", key));
    }

    @Override
    public void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onLoadLocationSuccess(List<MyLatLng> latLngs) {
        Near_Mall_Area = new ArrayList<>();
        for (MyLatLng myLatLng : latLngs) {
            LatLng convert = new LatLng(myLatLng.getLatitude(), myLatLng.getLongitude());
            Near_Mall_Area.add(convert);
            MyLatLng MyLatLng = new MyLatLng(myLatLng.getLatitude(), myLatLng.getLongitude());

            list.add(MyLatLng);
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap = mMap;
                mMap.setMyLocationEnabled(true);


                for (int i = 0; i < list.size(); i++) {
                    LatLng sydney = new LatLng(Double.parseDouble(String.valueOf(list.get(i).getLatitude())), Double.parseDouble(String.valueOf(list.get(i).getLongitude())));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("mall").snippet("address"));
                    // For zooming functionality
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                //To add marker

            }
        });

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        sendNotification("AP-Group", String.format("%s Mmove within the Mall area", key));
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }

    private void buildLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                if (mMap != null) {

                    lastLocation = locationResult.getLastLocation();

                    geoFire.setLocation("YOU", new GeoLocation(lastLocation.getLatitude(),
                            lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (currentUser != null) currentUser.remove();
                            currentUser = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lastLocation.getLatitude(),
                                            lastLocation.getLongitude()))
                                    .title("YOU"));

                            //After add marker ...... move camera

                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(currentUser.getPosition(), 12.0f));
                        }
                    });


                }

            }
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.getUiSettings().setZoomControlsEnabled(true);


        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        //Add circle mall area
        for (LatLng latLng : Near_Mall_Area) {
            mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(500)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(5.0f)
            );
            //Queery for user in mall area

            // 500 m
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 0.5f);
            geoQuery.addGeoQueryEventListener(this);
        }
    }

    private void sendNotification(String title, String content) {

        Toast.makeText(getContext(), "" + content, Toast.LENGTH_SHORT).show();

        String NOTIFICATION_CHANNEL_ID = "1RC_mall_multiple_location";
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(), notification);

    }

}
