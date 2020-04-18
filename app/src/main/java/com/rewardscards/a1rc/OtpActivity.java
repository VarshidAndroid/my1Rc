package com.rewardscards.a1rc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.chaos.view.PinView;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.OtpVerificationResponse;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.OtpParam;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {
    CardView cardview;
    ImageView back_arrow;
    Button bn_login;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PinView pinView;
    String mVerificationId;
    String mobileno,country_code;
    private FirebaseAuth mAuth;
    TextView txt_data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verificaition_activity);



        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        pinView = (PinView)findViewById(R.id.pinView);
        mobileno = getIntent().getStringExtra("mobile");
        country_code = getIntent().getStringExtra("code");
        txt_data = (TextView)findViewById(R.id.txt_data);
        txt_data.setText("Please type the verification code \n send to "+mobileno);
        /*cardview = (CardView)findViewById(R.id.cardview);
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });*/

        ImageView img_back = (ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bn_login = (Button)findViewById(R.id.bn_login);
        bn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyVerificationCode(pinView.getText().toString());
            }
        });

        /*back_arrow = findViewById(R.id.back_verification);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });*/

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("tag", "onVerificationCompleted:" + phoneAuthCredential);
                String code = phoneAuthCredential.getSmsCode();
                Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Login falied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //storing the verification id that is sent to the user
                mVerificationId = s;
            }

        };


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                country_code+mobileno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OtpActivity.this,               // Activity (for callback binding)
                mCallbacks);
    }

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    OtpParam otpParam = new OtpParam(mobileno);
                    Call<OtpVerificationResponse> call = webApi.otp_verify(otpParam);
                    call.enqueue(new Callback<OtpVerificationResponse>() {
                        @Override
                        public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                            Utiss.showToast(getApplicationContext(),"Verification Completed Successfully",R.color.green);
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"login","1");
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "user_id",response.body().getId());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "first_name",response.body().getFirstName());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "last_name",response.body().getLastName());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "email",response.body().getEmail());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "phone",response.body().getMobileNumber());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "address_1",response.body().getAddress1());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "address_2",response.body().getAddress2());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "address_3",response.body().getAddress3());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "address_4",response.body().getAddress4());
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(), "sa_identity_number",response.body().getSaIdentityNumber());
                            //SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"password",edit_passworrd.getText().toString());

                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"login","1");
                            SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"login","1");

                            Intent intent = new Intent(OtpActivity.this, NewMainActivity.class);
                            intent.putExtra("flag","Home");
                            intent.putExtra("login_step","1");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {
                            Utiss.showToast(getApplicationContext(),"Registration failed",R.color.msg_fail);
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
