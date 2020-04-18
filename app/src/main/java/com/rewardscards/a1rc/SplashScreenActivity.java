package com.rewardscards.a1rc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rewardscards.a1rc.Utils.SharedPrefsUtils;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Log.w("id",SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"sa_identity_number"));
        Log.w("phone",SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"phone"));
        Log.w("token",SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"token"));
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(),"login").equalsIgnoreCase("1")){
                    Intent intent = new Intent(SplashScreenActivity.this, NewMainActivity.class);
                    intent.putExtra("flag","home");
                    intent.putExtra("login_step","1");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashScreenActivity.this, Login_Screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


                //   edt_pin.setVisibility(View.VISIBLE);
                //   btn.setVisibility(View.VISIBLE);
               /* Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*/

            }
        }, 1000);
    }



}
