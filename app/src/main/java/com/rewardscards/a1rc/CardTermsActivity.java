package com.rewardscards.a1rc;

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
import com.rewardscards.a1rc.Response.PrivacyPolicyResponse;
import com.rewardscards.a1rc.Response.TermsAndConditionResponse;
import com.rewardscards.a1rc.Utils.Utiss;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardTermsActivity extends AppCompatActivity {

    TextView txt_data;
    com.wang.avi.AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition_activity);



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
        Call<TermsAndConditionResponse> call = webApi.terms_condition();
        call.enqueue(new Callback<TermsAndConditionResponse>() {
            @Override
            public void onResponse(Call<TermsAndConditionResponse> call, Response<TermsAndConditionResponse> response) {
                txt_data.setText(Html.fromHtml(response.body().getTerms()));
                stopAnim();
            }

            @Override
            public void onFailure(Call<TermsAndConditionResponse> call, Throwable t) {
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
