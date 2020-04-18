package com.rewardscards.a1rc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.Adapters.ExpandableListAdapter;
import com.rewardscards.a1rc.Fragments.AddCardFragment;
import com.rewardscards.a1rc.Fragments.HomeFragments;
import com.rewardscards.a1rc.Fragments.MyRewardsFragment;
import com.rewardscards.a1rc.Fragments.ProfileFragments;
import com.rewardscards.a1rc.Fragments.SidemenuStore;
import com.rewardscards.a1rc.Fragments.StoreCardFragment;
import com.rewardscards.a1rc.Fragments.StoreFragment;
import com.rewardscards.a1rc.Fragments.StoreProductFragment;
import com.rewardscards.a1rc.Fragments.StoreSavedCardFragment;
import com.rewardscards.a1rc.Model.MenuModel;
import com.rewardscards.a1rc.Model.MyLatLng;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.StoreLatLan;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.ParamStore;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GeoQueryEventListener, IOnLoadLocationListener {

    private AppBarConfiguration mAppBarConfiguration;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    ExpandableListAdapter expandableListAdapter;
    ImageView img_menu, img_rc, img_home, img_cart, img_close, img_Search;
    LinearLayout layout_store, layout_home, layout_cards, layout_myReward;
    RelativeLayout layout_buttons, layout_search;
    DrawerLayout drawer;
    private AddressServices mSensorService;
    ViewPager viewPager;
    FrameLayout viewPager_old;
    Intent mServiceIntent;
    LinearLayout layout_skip, layout_bottm, layout_login, layout_cards_skip;
    LinearLayout layout_home_round, layout_store_local_round, layout_reqards_round, layout_card_round;
    String lat_send,long_send;
    Location currentLocation;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Marker currentUser;
    private DatabaseReference myLocationRef;
    private GeoFire geoFire;
    private List<LatLng> Near_Mall_Area;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private IOnLoadLocationListener listener;

    private DatabaseReference myCity;

    private Location lastLocation;
    private List<LatLng> dang_area = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);



        mSensorService = new AddressServices(getApplicationContext());
        mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());


        if (!isMyServiceRunning(mSensorService.getClass())) {
            //startService(mServiceIntent);
        }


        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ProfileFragments());
        fragmentList.add(new StoreFragment());
        fragmentList.add(new MyRewardsFragment());
        fragmentList.add(new StoreSavedCardFragment());

        viewPager_old = (FrameLayout) findViewById(R.id.viewPager_old);
        layout_cards_skip = (LinearLayout) findViewById(R.id.layout_cards_skip);
        layout_login = (LinearLayout) findViewById(R.id.layout_login);
        layout_skip = (LinearLayout) findViewById(R.id.layout_skip);
        layout_bottm = (LinearLayout) findViewById(R.id.layout_bottm);

        layout_home_round = (LinearLayout) findViewById(R.id.layout_home_round);
        layout_store_local_round = (LinearLayout) findViewById(R.id.layout_store_local_round);
        layout_reqards_round = (LinearLayout) findViewById(R.id.layout_reqards_round);
        layout_card_round = (LinearLayout) findViewById(R.id.layout_card_round);


        layout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMainActivity.this, Login_Screen.class);
                startActivity(intent);
            }
        });

        layout_cards_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.selected_1rc);
                pushFragment(new StoreCardFragment(), "card");
            }
        });
        if (getIntent().getStringExtra("login_step") != null && getIntent().getStringExtra("login_step").equalsIgnoreCase("0")) {

            layout_bottm.setVisibility(View.GONE);
            layout_skip.setVisibility(View.VISIBLE);

        } else {
            layout_bottm.setVisibility(View.VISIBLE);
            layout_skip.setVisibility(View.GONE);

        }

       /* tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplication(), "" + tabLayout.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                viewPager_old.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);


            }
        });*/


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        drawer = findViewById(R.id.drawer_layout);

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        img_menu = (ImageView) findViewById(R.id.img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }

        });


        img_rc = (ImageView) findViewById(R.id.img_rc);
        img_home = (ImageView) findViewById(R.id.img_home);
        img_cart = (ImageView) findViewById(R.id.img_cart);
        img_close = (ImageView) findViewById(R.id.img_close);
        img_Search = (ImageView) findViewById(R.id.img_Search);
        layout_home = (LinearLayout) findViewById(R.id.layout_home);
        layout_store = (LinearLayout) findViewById(R.id.layout_store);
        layout_cards = (LinearLayout) findViewById(R.id.layout_cards);
        layout_myReward = (LinearLayout) findViewById(R.id.laout_My_reward);
        layout_buttons = (RelativeLayout) findViewById(R.id.layout_buttons);
        layout_search = (RelativeLayout) findViewById(R.id.layout_search);


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_search.setVisibility(View.GONE);
                layout_buttons.setVisibility(View.VISIBLE);
                img_menu.setVisibility(View.VISIBLE);
            }
        });


        img_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_search.setVisibility(View.VISIBLE);
                layout_buttons.setVisibility(View.GONE);
                img_menu.setVisibility(View.GONE);
            }
        });
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NewMainActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
                Utiss.showToast(getApplicationContext(), "Coming Soon", R.color.msg_fail);
            }
        });

        layout_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new StoreSavedCardFragment(), "card");

                layout_home_round.setVisibility(View.INVISIBLE);
                layout_store_local_round.setVisibility(View.INVISIBLE);
                layout_reqards_round.setVisibility(View.INVISIBLE);
                layout_card_round.setVisibility(View.VISIBLE);


                layout_home.setVisibility(View.VISIBLE);
                layout_store.setVisibility(View.VISIBLE);
                layout_myReward.setVisibility(View.VISIBLE);
                layout_cards.setVisibility(View.INVISIBLE);


            }
        });
        layout_myReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new MyRewardsFragment(), "my reward");

                layout_home_round.setVisibility(View.INVISIBLE);
                layout_store_local_round.setVisibility(View.INVISIBLE);
                layout_reqards_round.setVisibility(View.VISIBLE);
                layout_card_round.setVisibility(View.INVISIBLE);


                layout_home.setVisibility(View.VISIBLE);
                layout_store.setVisibility(View.VISIBLE);
                layout_myReward.setVisibility(View.INVISIBLE);
                layout_cards.setVisibility(View.VISIBLE);


            }
        });
        layout_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new StoreFragment(), "store");

                layout_home_round.setVisibility(View.INVISIBLE);
                layout_store_local_round.setVisibility(View.VISIBLE);
                layout_reqards_round.setVisibility(View.INVISIBLE);
                layout_card_round.setVisibility(View.INVISIBLE);


                layout_home.setVisibility(View.VISIBLE);
                layout_store.setVisibility(View.INVISIBLE);
                layout_myReward.setVisibility(View.VISIBLE);
                layout_cards.setVisibility(View.VISIBLE);


            }
        });
        layout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_home_round.setVisibility(View.VISIBLE);
                layout_store_local_round.setVisibility(View.INVISIBLE);
                layout_reqards_round.setVisibility(View.INVISIBLE);
                layout_card_round.setVisibility(View.INVISIBLE);


                layout_home.setVisibility(View.INVISIBLE);
                layout_store.setVisibility(View.VISIBLE);
                layout_myReward.setVisibility(View.VISIBLE);
                layout_cards.setVisibility(View.VISIBLE);

                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new ProfileFragments(), "profile");


            }

        });

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), NewMainActivity.class));
                pushFragment(new HomeFragments(), "Home");
                img_rc.setImageResource(R.drawable.rc);
              /*  Intent intent = new Intent(NewMainActivity.this,MainActivity.class);
                startActivity(intent);*/

            }
        });
        img_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.selected_1rc);
                pushFragment(new StoreCardFragment(), "card");


            }

                /*if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }*/

        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = (TextView) headerView.findViewById(R.id.tvUsername);
        CircleImageView imageView = (CircleImageView) headerView.findViewById(R.id.imageView);

        Glide
                .with(getApplicationContext())
                .load("https://1rewardscards.com/API/" + SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "profile_pic"))
                .centerCrop()
                .placeholder(R.drawable.rc)
                .into(imageView);

        tvUsername.setText(SharedPrefsUtils.getSharedPreferenceString(NewMainActivity.this, "first_name"));
        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
      /*  mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

        if (getIntent().getStringExtra("flag").equalsIgnoreCase("rc")) {
            pushFragment(new StoreCardFragment(), "Home");
        } else if (getIntent().getStringExtra("flag").equalsIgnoreCase("side")) {
            pushFragment(new SidemenuStore(), "store");
        } else if (getIntent().getStringExtra("flag").equalsIgnoreCase("sub")) {

            pushFragment(new AddCardFragment(), "Home");
        } else {
            pushFragment(new HomeFragments(), "Home");
        }


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        buildLocationRequest();
                        buildLocationCallback();
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NewMainActivity.this);


                        initArea();
                        settingGeoFire();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(NewMainActivity.this, "You must enable permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.w("isMyServiceRunning?", true + "");
                    return true;
                }
            }
        }
        Log.w("isMyServiceRunning?", false + "");
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.viewPager_old);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void prepareMenuData() {


        MenuModel menuModel = new MenuModel("Let's Go Shopping", true, true, "Let's Go Shopping"); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel("Groceries & Supermarkets", false, false, "Groceries & Supermarkets");
        childModelsList.add(childModel);
        childModel = new MenuModel("Massmart", false, false, "Massmart");
        childModelsList.add(childModel);
        childModel = new MenuModel("Clothing & Brands", false, false, "Clothing & Brands");
        childModelsList.add(childModel);
        childModel = new MenuModel("Cell Phone & Data", false, false, "Cell Phone & Data");
        childModelsList.add(childModel);
        childModel = new MenuModel("Food & Beverages", false, false, "Food & Beverages");
        childModelsList.add(childModel);
        childModel = new MenuModel("Home Tech & Electronics", false, false, "Home Tech & Electronics");
        childModelsList.add(childModel);
        childModel = new MenuModel("Beds & Linen", false, false, "Beds & Linen");
        childModelsList.add(childModel);
        childModel = new MenuModel("Liquor", false, false, "Liquor");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Entertainment", true, true, "Entertainment"); //Menu of Java Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("Cinema & Movies", false, false, "Cinema & Movies");
        childModelsList.add(childModel);
        childModel = new MenuModel("Super Car Club", false, false, "Super Car Club");
        childModelsList.add(childModel);
        childModel = new MenuModel("Boat Cruise", false, false, "Boat Cruise");
        childModelsList.add(childModel);
        childModel = new MenuModel("B-up", false, false, "B-up");
        childModelsList.add(childModel);
        childModel = new MenuModel("Bounce", false, false, "Bounce");
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel("Pharmacies - Retail", true, false, "Pharmacies - Retail"); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        menuModel = new MenuModel("Pharmacies - General", true, false, "Pharmacies - General"); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Travel & Holiday Packages", true, true, "Travel & Holiday Packages"); //Menu of Java Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("Travel Agents", false, false, "Travel Agents");
        childModelsList.add(childModel);
        childModel = new MenuModel("Airlines", false, false, "Airlines");
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Accomodation", true, true, "Accomodation"); //Menu of Java Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("B&B's", false, false, "B&B's");
        childModelsList.add(childModel);
        childModel = new MenuModel("Chalets", false, false, "Chalets");
        childModelsList.add(childModel);
        childModel = new MenuModel("Hotels", false, false, "Hotels");
        childModelsList.add(childModel);
        childModel = new MenuModel("Self Catering", false, false, "Self Catering");
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel("Hardware & Building", true, false, "Hardware & Building"); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Banks & Vaults", true, true, "Banks & Vaults"); //Menu of Java Tutorials
        headerList.add(menuModel);

        childModel = new MenuModel("Vaults", false, false, "Vaults");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel("Sign Out", true, false, "Sign Out"); //Menu of Java Tutorials
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


    }

    private void populateExpandableList() {

        expandableListAdapter = new com.rewardscards.a1rc.Adapters.ExpandableListAdapter(NewMainActivity.this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (headerList.get(groupPosition).code.equalsIgnoreCase("Pharmacies - Retail")) {
                        //SharedPrefsUtils.setSharedPreferenceString(NewMainActivity.this, SharedPrefsUtils.EXPLORE, "1");
                        //Toast.makeText(getApplicationContext(),"Pharmacies - Retail",Toast.LENGTH_LONG).show();
                        //Utiss.showToast(getApplicationContext(),"Pharmacies - Retail",R.color.gold);
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "3");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "no");
                        pushFragment(new SidemenuStore(), "store");

                    } else if (headerList.get(groupPosition).code.equalsIgnoreCase("Pharmacies - General")) {
                        //Toast.makeText(getApplicationContext(),"Pharmacies - General",Toast.LENGTH_LONG).show();
                        //Utiss.showToast(getApplicationContext(),"Pharmacies - General",R.color.gold);
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "4");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "no");
                        pushFragment(new SidemenuStore(), "store");

                    } else if (headerList.get(groupPosition).code.equalsIgnoreCase("Hardware & Building")) {
                        //Toast.makeText(getApplicationContext(),"Hardware & Building",Toast.LENGTH_LONG).show();
                        //Utiss.showToast(getApplicationContext(),"Hardware & Building",R.color.gold);
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "7");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "no");
                        pushFragment(new SidemenuStore(), "store");

                    } else if (headerList.get(groupPosition).code.equalsIgnoreCase("Sign Out")) {
                        //Toast.makeText(getApplicationContext(),"Sign Out",Toast.LENGTH_LONG).show();


                        final AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this, R.style.CustomAlertDialog);
                        ViewGroup viewGroup = v.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.logout, viewGroup, false);
                        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
                        Button buttonyes = dialogView.findViewById(R.id.buttonyes);

                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();
                        buttonOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        buttonyes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utiss.showToast(NewMainActivity.this, "Sign Out", R.color.gold);
                                SharedPrefsUtils.setSharedPreferenceString(NewMainActivity.this, "login", "0");
                                Intent intent = new Intent(NewMainActivity.this, SplashScreenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        alertDialog.show();


                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.code.equalsIgnoreCase("Groceries & Supermarkets")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "1");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Massmart")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "2");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Clothing & Brands")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "3");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Cell Phone & Data")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "4");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Food & Beverages")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "5");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Home Tech & Electronics")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "6");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Beds & Linen")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "7");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Liquor")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "8");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Cinema & Movies")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "9");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Super Car Club")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "10");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Super Car Club")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "11");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("B-up")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "12");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Bounce")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "13");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Travel Agents")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "5");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "14");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Airlines")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "5");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "15");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("B&B's")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "16");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Chalets")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "17");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Chalets")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "18");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Self Catering")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "19");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Vaults")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "9");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "20");
                        pushFragment(new SidemenuStore(), "store");
                    } else if (model.code.equalsIgnoreCase("Contact")) {
                        Intent intent = new Intent(NewMainActivity.this, NewMainActivity.class);
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Faq")) {
                        Intent intent = new Intent(NewMainActivity.this, NewMainActivity.class);
                        startActivity(intent);
                    }
                }

                return false;
            }
        });
    }


    public boolean pushFragment(Fragment fragment, String tag) {
        //   FrameLayout viewPager_old = (FrameLayout)findViewById(R.id.viewPager_old);
        //    viewPager_old.setVisibility(View.VISIBLE);
//        viewPager.setVisibility(View.GONE);

        if (tag.equalsIgnoreCase("home")) {
            layout_home_round.setVisibility(View.INVISIBLE);
            layout_store_local_round.setVisibility(View.INVISIBLE);
            layout_reqards_round.setVisibility(View.INVISIBLE);
            layout_card_round.setVisibility(View.INVISIBLE);


            layout_home.setVisibility(View.VISIBLE);
            layout_store.setVisibility(View.VISIBLE);
            layout_myReward.setVisibility(View.VISIBLE);
            layout_cards.setVisibility(View.VISIBLE);
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.feed_in, R.anim.feed_out)
                    .replace(R.id.viewPager_old, fragment, tag)
                    //.addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {

            if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("0.0")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new HomeFragments(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("0.1")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new SidemenuStore(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("0.2")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new StoreProductFragment(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("1")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new HomeFragments(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("2")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new HomeFragments(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("3")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new HomeFragments(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("4")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new HomeFragments(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("5")) {
                img_rc.setImageResource(R.drawable.rc);
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new HomeFragments(), "Home");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("4.1")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new StoreSavedCardFragment(), "card");
            } else if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "back").equalsIgnoreCase("0.5555")) {
                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "back", "0");
                pushFragment(new HomeFragments(), "Home");
            } else {
                finish();
            }


        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void initArea() {
 /*       dang_area.add(new LatLng(22.4354,70.6009));
        dang_area.add(new LatLng(22.4707,70.0577));
        dang_area.add(new LatLng(23.0225,72.5714));
        dang_area.add(new LatLng(70.753087,22.3355202));
        dang_area.add(new LatLng(22.3380593,70.7526469));*/

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<StoreLatLan> call = webApi.location_store();
        call.enqueue(new Callback<StoreLatLan>() {
            @Override
            public void onResponse(Call<StoreLatLan> call, Response<StoreLatLan> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
                    dang_area.add(new LatLng(Double.parseDouble(response.body().getData().get(i).getLatitude()),
                            Double.parseDouble(response.body().getData().get(i).getLongitude())));
                }
            }

            @Override
            public void onFailure(Call<StoreLatLan> call, Throwable t) {

            }
        });

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


        FirebaseDatabase.getInstance()
                .getReference("Near_Mall_Area")
                .child("MyCity")
                .setValue(Near_Mall_Area)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(NewMainActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                    }
                });


        FirebaseDatabase.getInstance().getReference("Near_Mall_Area")
                .child("MyCity")
                .addListenerForSingleValueEvent(new ValueEventListener() {
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

    }


    private void settingGeoFire() {
        myLocationRef = FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFire = new GeoFire(myLocationRef);
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

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      //  LatLng latLng_n = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //mMap.getUiSettings().setZoomControlsEnabled(true);


        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        //Add circle mall area
        for (LatLng latLng : dang_area) {
            mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(500)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(5.0f)
            );
            //Queery for user in mall area

            // 500 m

            lat_send = String.valueOf(latLng.latitude);
            long_send = String.valueOf( latLng.longitude);


            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 0.5f);
            geoQuery.addGeoQueryEventListener(NewMainActivity.this);
        }
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        //sendNotification("AP-Group", String.format("%s enterd the Mall area", key));

        ParamStore paramStore = new ParamStore(lat_send,long_send, SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"user_id"));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<StoreLatLan> call = webApi.push_notification(paramStore);
        call.enqueue(new Callback<StoreLatLan>() {
            @Override
            public void onResponse(Call<StoreLatLan> call, Response<StoreLatLan> response) {

            }

            @Override
            public void onFailure(Call<StoreLatLan> call, Throwable t) {

            }
        });

    }

    @Override
    public void onKeyExited(String key) {
        //sendNotification("AP-Group", String.format("%s Leave the Mall area", key));

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // sendNotification("AP-Group", String.format("%s Mmove within the Mall area", key));

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Toast.makeText(this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String title, String content) {

        Toast.makeText(this, "" + content, Toast.LENGTH_SHORT).show();

        String NOTIFICATION_CHANNEL_ID = "1RC_mall_multiple_location";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


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


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(), notification);

    }

    @Override
    public void onLoadLocationSuccess(List<MyLatLng> latLngs) {
        Near_Mall_Area = new ArrayList<>();
        for (MyLatLng myLatLng : latLngs) {
            LatLng convert = new LatLng(myLatLng.getLatitude(), myLatLng.getLongitude());
            Near_Mall_Area.add(convert);
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewMainActivity.this);
    }

    @Override
    public void onLoadLocationFailed(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }
}
