package com.rewardscards.a1rc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rewardscards.a1rc.Fragments.HomeFragments;
import com.rewardscards.a1rc.Fragments.MyRewardsFragment;
import com.rewardscards.a1rc.Fragments.ProfileFragments;
import com.rewardscards.a1rc.Fragments.SidemenuStore;
import com.rewardscards.a1rc.Fragments.StoreCardFragment;
import com.rewardscards.a1rc.Fragments.StoreFragment;
import com.rewardscards.a1rc.Fragments.StoreProductFragment;
import com.rewardscards.a1rc.Fragments.StoreSavedCardFragment;
import com.rewardscards.a1rc.Model.MenuModel;

import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    ExpandableListAdapter expandableListAdapter;
    ImageView img_menu, img_rc, img_home, img_cart, img_close, img_Search;
    LinearLayout layout_store, layout_home, layout_cards, layout_myReward;
    RelativeLayout layout_buttons, layout_search;
    DrawerLayout drawer;

    ViewPager viewPager;
    FrameLayout viewPager_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ProfileFragments());
        fragmentList.add(new StoreFragment());
        fragmentList.add(new MyRewardsFragment());
        fragmentList.add(new StoreSavedCardFragment());

        viewPager_old = (FrameLayout) findViewById(R.id.viewPager_old);

        viewPager = (ViewPager) findViewById(R.id.viewPager);




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
                //Toast.makeText(MainActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
                Utiss.showToast(getApplicationContext(), "Coming Soon", R.color.msg_fail);
            }
        });

        layout_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new StoreSavedCardFragment(), "card");
            }
        });
        layout_myReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new MyRewardsFragment(), "my reward");
            }
        });
        layout_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new StoreFragment(), "store");
            }
        });
        layout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.rc);
                pushFragment(new ProfileFragments(), "profile");
            }
        });

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //pushFragment(new HomeFragments(), "Home");
                Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                intent.putExtra("flag", "home");
                startActivity(intent);

            }
        });
        img_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                intent.putExtra("flag", "rc");
                startActivity(intent);
              /*  ImageView perse = (ImageView) findViewById(R.id.img_rc);
                perse.setImageResource(R.drawable.selected_1rc);
                pushFragment(new StoreCardFragment(), "card");*/


            }

                /*if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }*/

        });

        //NavigationView navigationView = findViewById(R.id.nav_view);
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

        //   pushFragment(new HomeFragments(), "Home");
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

        expandableListAdapter = new com.rewardscards.a1rc.Adapters.ExpandableListAdapter(MainActivity.this, headerList, childList);
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
                        //SharedPrefsUtils.setSharedPreferenceString(MainActivity.this, SharedPrefsUtils.EXPLORE, "1");
                        //Toast.makeText(getApplicationContext(),"Pharmacies - Retail",Toast.LENGTH_LONG).show();
                        //Utiss.showToast(getApplicationContext(),"Pharmacies - Retail",R.color.gold);
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "3");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "no");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                        //pushFragment(new SidemenuStore(), "store");

                    } else if (headerList.get(groupPosition).code.equalsIgnoreCase("Pharmacies - General")) {
                        //Toast.makeText(getApplicationContext(),"Pharmacies - General",Toast.LENGTH_LONG).show();
                        //Utiss.showToast(getApplicationContext(),"Pharmacies - General",R.color.gold);
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "4");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "no");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);

                    } else if (headerList.get(groupPosition).code.equalsIgnoreCase("Hardware & Building")) {
                        //Toast.makeText(getApplicationContext(),"Hardware & Building",Toast.LENGTH_LONG).show();
                        //Utiss.showToast(getApplicationContext(),"Hardware & Building",R.color.gold);
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "7");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "no");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);

                    } else if (headerList.get(groupPosition).code.equalsIgnoreCase("Sign Out")) {
                        //Toast.makeText(getApplicationContext(),"Sign Out",Toast.LENGTH_LONG).show();


                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
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
                                Utiss.showToast(MainActivity.this, "Sign Out", R.color.gold);
                                SharedPrefsUtils.setSharedPreferenceString(MainActivity.this, "login", "0");
                                Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
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
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Massmart")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "2");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Clothing & Brands")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "3");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Cell Phone & Data")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "4");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Food & Beverages")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "5");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Home Tech & Electronics")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "6");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Beds & Linen")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "7");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Liquor")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "1");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "8");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Cinema & Movies")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "9");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Super Car Club")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "10");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Super Car Club")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "11");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("B-up")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "12");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Bounce")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "2");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "13");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Travel Agents")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "5");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "14");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Airlines")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "5");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "15");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("B&B's")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "16");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Chalets")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "17");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Chalets")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "18");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Self Catering")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "6");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "19");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Vaults")) {
                        drawer.closeDrawer(Gravity.START);
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_group", "9");
                        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sidemenu_sub", "20");
                        Intent intent = new Intent(MainActivity.this, NewMainActivity.class);
                        intent.putExtra("flag", "side");
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Contact")) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (model.code.equalsIgnoreCase("Faq")) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
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
}
