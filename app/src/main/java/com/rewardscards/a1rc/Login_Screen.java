package com.rewardscards.a1rc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.CommonModel;
import com.rewardscards.a1rc.Response.LoginModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.Login;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Screen extends AppCompatActivity {
    public TextView tv_reg, tv_forgot_pass,txt_login_skip2;
    public ImageView login, skip_login;

    private EditText edit_email, edit_passworrd;
    com.wang.avi.AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginn_screen);
        requestMultiplePermissions();
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        login = findViewById(R.id.img_login);
        skip_login = findViewById(R.id.img_login_skip);
        tv_forgot_pass = findViewById(R.id.txt_forogot_password);
        tv_reg = findViewById(R.id.tv_reg3);

        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        txt_login_skip2 = (TextView)findViewById(R.id.txt_login_skip2);

        edit_passworrd = (EditText) findViewById(R.id.edit_passworrd);
        edit_email = (EditText) findViewById(R.id.edit_email);


        txt_login_skip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Screen.this, NewMainActivity.class);
                intent.putExtra("flag","Home");
                intent.putExtra("login_step","0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        skip_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Screen.this, NewMainActivity.class);
                intent.putExtra("flag","Home");
                intent.putExtra("login_step","0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_email.getText().toString().isEmpty()) {
                    edit_email.setError("Enter Email");
                } else if (edit_passworrd.getText().toString().isEmpty()) {
                    edit_passworrd.setError("Enter Password");
                } else {
                    startAnim();
                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    Login login = new Login(edit_email.getText().toString(),edit_passworrd.getText().toString(),SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"token"));
                    Call<LoginModel> call = webApi.login(login);
                    call.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                           stopAnim();
                            if (response.body().getMessage().equals("Login Failed")){
                                //Toast.makeText(getApplicationContext(),"Incorrect email and password",Toast.LENGTH_LONG).show();
                                Utiss.showToast(getApplicationContext(),"Incorrect email and password",R.color.msg_fail);

                            }else {
                                Utiss.showToast(getApplicationContext(),"Login Successfully",R.color.green);

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
                                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"password",edit_passworrd.getText().toString());
                                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"profile_pic",response.body().getProfile_pic());
                                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"login","1");
                                SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"login","1");

                                Intent intent = new Intent(Login_Screen.this, NewMainActivity.class);
                                intent.putExtra("flag","Home");
                                intent.putExtra("login_step","1");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            stopAnim();
                            Toast.makeText(getApplicationContext(),"server error",Toast.LENGTH_LONG).show();
                            Utiss.showToast(getApplicationContext(),"server error",R.color.msg_fail);
                        }
                    });

                }
            }
        });

        tv_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Screen.this, RegistrerActivity.class);
                startActivity(intent);
            }
        });
        tv_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Screen.this, ForgotPassword.class);
                startActivity(intent);
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
    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

}
