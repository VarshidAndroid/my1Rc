package com.rewardscards.a1rc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Response.CommonModel;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.params.Login;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout layout_login,skip_login,layout_register;
    TextView regi,txt_forogot_password,tv_reg;
    ImageView login,skip;

    com.wang.avi.AVLoadingIndicatorView avi;
    private EditText edit_email, edit_passworrd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginn_screen);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);
        edit_passworrd = (EditText) findViewById(R.id.edit_passworrd);
        edit_email = (EditText) findViewById(R.id.edit_email);

        txt_forogot_password = (TextView)findViewById(R.id.txt_forogot_password);

        skip = (ImageView) findViewById(R.id.img_login_skip);
        tv_reg = (TextView) findViewById(R.id.tv_reg);
        login = (ImageView) findViewById(R.id.img_login);



        tv_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrerActivity.class);
                startActivity(intent);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        txt_forogot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrerActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
