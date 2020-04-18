package com.rewardscards.a1rc;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.ApplyRcResponse;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.ApplyRcParam;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyNowActivity extends AppCompatActivity {
    Spinner spinner;
    EditText edit_firstname,edit_last_name,edit_address_1,edit_address_2,edit_address_3,edit_address_4,edit_code;
    Button btn_submit;
    String mobile_no,sa_id;
    com.wang.avi.AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_for_card);



        spinner = findViewById(R.id.spinner);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        mobile_no = SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"phone");
        sa_id = SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"sa_identity_number");

        edit_firstname = (EditText)findViewById(R.id.edit_firstname);
        edit_last_name = (EditText)findViewById(R.id.edit_last_name);
        edit_address_1 = (EditText)findViewById(R.id.edit_address_1);
        edit_address_2 = (EditText)findViewById(R.id.edit_address_2);
        edit_address_3 = (EditText)findViewById(R.id.edit_address_3);
        edit_address_4 = (EditText)findViewById(R.id.edit_address_4);
        edit_code = (EditText)findViewById(R.id.edit_code);
        btn_submit = (Button)findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_firstname.getText().toString().isEmpty()){
                    edit_firstname.setError("Enter First Name");
                }else if (edit_last_name.getText().toString().isEmpty()){
                    edit_last_name.setError("Enter Last Name");
                }else if (edit_address_1.getText().toString().isEmpty()){
                    edit_address_1.setError("Enter Address 1");
                }else if (edit_address_2.getText().toString().isEmpty()){
                    edit_address_2.setError("Enter Address 2");
                }else if (edit_address_3.getText().toString().isEmpty()){
                    edit_address_3.setError("Enter Address 3");
                }else if (edit_address_4.getText().toString().isEmpty()){
                    edit_address_4.setError("Enter Address 4");
                }else if (edit_code.getText().toString().isEmpty()){
                    edit_code.setError("Enter Code");
                }else {
                    startAnim();
                    ApplyRcParam param= new ApplyRcParam(edit_firstname.getText().toString(),
                            edit_last_name.getText().toString(),edit_address_1.getText().toString(),
                            edit_address_2.getText().toString(),
                            edit_address_3.getText().toString(),
                            edit_address_4.getText().toString(),
                            "province",
                            edit_code.getText().toString(),
                            "2",
                            "1",
                            sa_id,
                            mobile_no);
                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    Call<ApplyRcResponse> call = webApi.apply_onerc_card(param);
                    call.enqueue(new Callback<ApplyRcResponse>() {
                        @Override
                        public void onResponse(Call<ApplyRcResponse> call, Response<ApplyRcResponse> response) {
                            stopAnim();
                            if (response.body().getMessage().equalsIgnoreCase("Registaration failed")){
                                Utiss.showToast(getApplicationContext(),response.body().getMessage(),R.color.msg_fail);
                            }else {
                                Utiss.showToast(getApplicationContext(),response.body().getMessage(),R.color.green);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApplyRcResponse> call, Throwable t) {
                            stopAnim();
                            Utiss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
                        }
                    });
                }
            }
        });
        ImageView img_back = (ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] plants = new String[]{
                "Province",
                "Kwazulu Natal",
                "Gauteng","Limpopo",
                "Mpumalanga","Free State",
                "North West",
                "Northern Cape",
                "Western Cape",
                "Eastern West",

        };

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_rr,plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_rr);
        spinner.setAdapter(spinnerArrayAdapter);


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
