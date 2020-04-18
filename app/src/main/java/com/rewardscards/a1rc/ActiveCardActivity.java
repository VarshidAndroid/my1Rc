package com.rewardscards.a1rc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.CommonModelSmallResponse;
import com.rewardscards.a1rc.Response.ModelResponseCommonSmallCase;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.SAIDParam;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveCardActivity extends AppCompatActivity {
    EditText edit_card_Serial;
    Button btn_request_otp,txt_btn_submit;
    com.chaos.view.PinView pinview;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_card_activity);



        mAuth = FirebaseAuth.getInstance();
        ImageView img_back = (ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_request_otp = (Button)findViewById(R.id.btn_request_otp);
        txt_btn_submit = (Button)findViewById(R.id.txt_btn_submit);
        edit_card_Serial = (EditText)findViewById(R.id.edit_card_Serial);
        pinview = (com.chaos.view.PinView)findViewById(R.id.pinview);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("tag", "onVerificationCompleted:" + phoneAuthCredential);
                String code = phoneAuthCredential.getSmsCode();
                Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("error",e);
                Toast.makeText(getApplicationContext(), "Login falied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //storing the verification id that is sent to the user
                mVerificationId = s;
            }

        };


        btn_request_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (edit_card_Serial.getText().toString().isEmpty()){
                   edit_card_Serial.setError("Enter Card Number");
               }else {
                   WebApi  webApi = ApiUtils.getClient().create(WebApi.class);
                   SAIDParam saidParam = new SAIDParam(edit_card_Serial.getText().toString());
                   Call<CommonModelSmallResponse> call = webApi.fetch_mobile_number(saidParam);
                   call.enqueue(new Callback<CommonModelSmallResponse>() {
                       @Override
                       public void onResponse(Call<CommonModelSmallResponse> call, Response<CommonModelSmallResponse> response) {
                           if (response.body().getMessage().equalsIgnoreCase("Mobile Number Fetched Succesfully")){
                               Utiss.showToast(getApplicationContext(),"Otp Sent, Please wait..!!",R.color.green);
                               String code = response.body().getData().get(0).getCountry_code();
                               String number = response.body().getData().get(0).getMobileNumber();
                               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                       code + number,        // Phone number to verify
                                       60,                 // Timeout duration
                                       TimeUnit.SECONDS,   // Unit of timeout
                                       ActiveCardActivity.this,               // Activity (for callback binding)
                                       mCallbacks);


                           }else {
                               Utiss.showToast(getApplicationContext(),response.body().getMessage(),R.color.msg_fail);
                           }
                       }

                       @Override
                       public void onFailure(Call<CommonModelSmallResponse> call, Throwable t) {
                           Utiss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
                       }
                   });
               }
            }
        });


        txt_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = pinview.getText().toString();
                if(data.toString().isEmpty()){
                    Utiss.showToast(getApplicationContext(),"Enter Otp",R.color.msg_fail);
                }else {
                    verifyVerificationCode(pinview.getText().toString());
                }
            }
        });
    }

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(ActiveCardActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Utiss.showToast(getApplicationContext(),"Verification Completed Successfully",R.color.green);
                    SAIDParam saidParam = new SAIDParam(edit_card_Serial.getText().toString());
                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    Call<ModelResponseCommonSmallCase>  call = webApi.active_onerc_card(saidParam);
                    call.enqueue(new Callback<ModelResponseCommonSmallCase>() {
                        @Override
                        public void onResponse(Call<ModelResponseCommonSmallCase> call, Response<ModelResponseCommonSmallCase> response) {
                            if (response.body().getMessage().equalsIgnoreCase("Activation Date Updated Succesfully")){
                                Utiss.showToast(getApplicationContext(),response.body().getMessage(),R.color.green);
                            }else{
                                Utiss.showToast(getApplicationContext(),response.body().getMessage(),R.color.msg_fail);
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelResponseCommonSmallCase> call, Throwable t) {
                            Utiss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
                        }
                    });
                } else {
                    Utiss.showToast(getApplicationContext(),"Registration failed",R.color.msg_fail);
                    //Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
