package com.rewardscards.a1rc;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.AddCAardToUSerModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.AddCAardToUSerPAram;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCardActivity extends AppCompatActivity {
    String card_id;
    TextView txt_card_name;
    Button btn_save;
    EditText edit_qre;
    com.wang.avi.AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_edit_fragment);



        card_id = getIntent().getStringExtra("card_id");
        txt_card_name = (TextView)findViewById(R.id.txt_card_name);
        edit_qre = (EditText)findViewById(R.id.edit_qre);
        btn_save = (Button)findViewById(R.id.btn_save);
        txt_card_name.setText(getIntent().getStringExtra("name"));
        ImageView img_back = (ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_qre.getText().toString().isEmpty()){
                    edit_qre.setError("Enter Card Number");
                }else {
                    startAnim();
                    AddCAardToUSerPAram addCAardToUSerPAram = new AddCAardToUSerPAram(SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"user_id"),
                            edit_qre.getText().toString(),card_id);
                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    Call<AddCAardToUSerModel> call = webApi.add_store_card(addCAardToUSerPAram);
                    call.enqueue(new Callback<AddCAardToUSerModel>() {
                        @Override
                        public void onResponse(Call<AddCAardToUSerModel> call, Response<AddCAardToUSerModel> response) {
                            stopAnim();
                            if (response.body().getMessage().equalsIgnoreCase("Card Not Added")){
                                Utiss.showToast(getApplicationContext(),"Somthing is wrong, Please try again later",R.color.msg_fail);
                            }else {
                                Utiss.showToast(getApplicationContext(),response.body().getMessage(),R.color.green);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<AddCAardToUSerModel> call, Throwable t) {
                            stopAnim();
                            Utiss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
                        }
                    });
                }
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
