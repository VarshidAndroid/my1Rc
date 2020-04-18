package com.rewardscards.a1rc;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.FAQANSModel;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.FAQQUEPARAM;
import com.google.android.gms.common.api.Api;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqActivity extends AppCompatActivity {
    TextView txt_data;
    com.wang.avi.AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);
        txt_data = (TextView)findViewById(R.id.txt_data);

        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);

        startAnim();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        FAQQUEPARAM faqqueparam = new FAQQUEPARAM(getIntent().getStringExtra("q_id"));
        Call<FAQANSModel> call = webApi.faq_answer(faqqueparam);
        call.enqueue(new Callback<FAQANSModel>() {
            @Override
            public void onResponse(Call<FAQANSModel> call, Response<FAQANSModel> response) {
                stopAnim();
                if (response.body().getMessage().equalsIgnoreCase("FAQ Answer Fetched Succesfully")){
                    txt_data.setText(Html.fromHtml(response.body().getAnswer()));
                }else{
                    Utiss.showToast(getApplicationContext(),response.body().getMessage(),R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<FAQANSModel> call, Throwable t) {
                Utiss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
            }
        });

        ImageView img_back = (ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, FaqListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FaqActivity.this, FaqListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
