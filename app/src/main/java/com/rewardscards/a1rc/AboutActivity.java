package com.rewardscards.a1rc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.rewardscards.a1rc.Fragments.AboutUsFragment;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.AboutResponse;
import com.rewardscards.a1rc.Utils.Utiss;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity {
    ImageView back;
    TextView txt_data;
    com.wang.avi.AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app_activity);


        ImageView img_back = (ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        txt_data = (TextView)findViewById(R.id.txt_data);
        startAnim();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<AboutResponse> call = webApi.about_app();
        call.enqueue(new Callback<AboutResponse>() {
            @Override
            public void onResponse(Call<AboutResponse> call, Response<AboutResponse> response) {
                txt_data.setText(Html.fromHtml(response.body().getAboutApp()));
                stopAnim();
            }

            @Override
            public void onFailure(Call<AboutResponse> call, Throwable t) {
                Utiss.showToast(getApplicationContext(),"Server error",R.color.msg_fail);
                stopAnim();
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

}
