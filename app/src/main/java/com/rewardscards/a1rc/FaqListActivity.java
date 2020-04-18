package com.rewardscards.a1rc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rewardscards.a1rc.Adapters.FaqListAdapter;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.FAQLISTREsponseModel;
import com.rewardscards.a1rc.Utils.Utiss;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqListActivity extends AppCompatActivity {
    RecyclerView recycle_view;
    FaqListAdapter adapter;
    ArrayList<FAQLISTREsponseModel.Datum> list = new ArrayList<>();
    com.wang.avi.AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_list_activity);


        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent intent = new Intent(FaqListActivity.this,NewMainActivity.class);
                intent.putExtra("flag","home");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
                startActivity(intent);
            }
        });
        startAnim();

        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FAQLISTREsponseModel> call =webApi.faq();
        call.enqueue(new Callback<FAQLISTREsponseModel>() {
            @Override
            public void onResponse(Call<FAQLISTREsponseModel> call, Response<FAQLISTREsponseModel> response) {

                stopAnim();
                for (int i =0;i < response.body().getData().size();i++)
                {
                    list.add(response.body().getData().get(i));
                }

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recycle_view.setLayoutManager(mLayoutManager);
                recycle_view.setItemAnimator(new DefaultItemAnimator());
                adapter = new FaqListAdapter(getApplicationContext(), list);
                recycle_view.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<FAQLISTREsponseModel> call, Throwable t) {
                stopAnim();
                Utiss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
            }
        });


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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FaqListActivity.this,NewMainActivity.class);
        intent.putExtra("flag","home");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
        startActivity(intent);
    }
}
