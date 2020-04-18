package com.rewardscards.a1rc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rewardscards.a1rc.Model.RegistrationModel;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.RegistrationMoel;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrerActivity extends AppCompatActivity {
    ImageView img_register;
    TextView tv_login,txt_term_condition;
    private EditText edit_firstname, edit_lastname, edit_id_number, edit_phonoe_no, edit_email, edit_password, confirm_password;
    com.wang.avi.AVLoadingIndicatorView avi;
    Spinner spinner_country;
    CheckBox ch_bx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        ch_bx = (CheckBox) findViewById(R.id.ch_bx);
        tv_login = (TextView) findViewById(R.id.tv_reg);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        edit_firstname = (EditText) findViewById(R.id.edit_firstname);
        edit_lastname = (EditText) findViewById(R.id.edit_lastname);
        edit_id_number = (EditText) findViewById(R.id.edit_id_number);
        edit_phonoe_no = (EditText) findViewById(R.id.edit_phonoe_no);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_password = (EditText) findViewById(R.id.edit_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        txt_term_condition = (TextView)findViewById(R.id.txt_term_condition);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinner_country.getBackground().setColorFilter(getResources().getColor(R.color.gold), PorterDuff.Mode.SRC_ATOP);
        // Initializing a String Array
        String[] plants = new String[]{
                "+91",
                "+27"
        };

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_country.setAdapter(spinnerArrayAdapter);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrerActivity.this, Login_Screen.class);
                startActivity(intent);
            }
        });


        txt_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(RegistrerActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(RegistrerActivity.this).inflate(R.layout.terms_caonciton, viewGroup, false);
       ///         TextView txt_no = dialogView.findViewById(R.id.buttonOk);
                TextView txt_yes = dialogView.findViewById(R.id.buttonyes);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                txt_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });


        img_register = (ImageView) findViewById(R.id.img_registor);
        img_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_firstname.getText().toString().isEmpty()) {
                    edit_firstname.setError("Enter First Name");
                } else if (edit_lastname.getText().toString().isEmpty()) {
                    edit_lastname.setError("Enter Last Name");
                } else if (edit_id_number.getText().toString().isEmpty()) {
                    edit_id_number.setError("Enter SA Identity Number");
                } else if (edit_phonoe_no.getText().toString().isEmpty()) {
                    edit_phonoe_no.setError("Enter Phone no");
                } else if (edit_email.getText().toString().isEmpty()) {
                    edit_email.setError("Enter Email");
                } else if (edit_password.getText().toString().isEmpty()) {
                    edit_password.setError("Enter Password");
                } else if (confirm_password.getText().toString().isEmpty()) {
                    confirm_password.setError("Enter Confirm Password");
                } else if (!edit_password.getText().toString().equalsIgnoreCase(confirm_password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm password dose not match", Toast.LENGTH_LONG).show();
                } else if (!ch_bx.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please agree with our terms and condition.", Toast.LENGTH_LONG).show();
                } else {
                    startAnim();
                    RegistrationMoel registrationMoel = new RegistrationMoel(edit_firstname.getText().toString(),
                            edit_lastname.getText().toString(),
                            edit_id_number.getText().toString(),
                            edit_phonoe_no.getText().toString(),
                            edit_email.getText().toString(),
                            edit_password.getText().toString(),
                            String.valueOf(spinner_country.getSelectedItem()),
                            SharedPrefsUtils.getSharedPreferenceString(getApplicationContext(), "token")
                    );

                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    Call<RegistrationModel> call = webApi.registration(registrationMoel);
                    call.enqueue(new Callback<RegistrationModel>() {
                        @Override
                        public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                            stopAnim();
                            if (response.body().getMessage().equalsIgnoreCase("Registration Successfully")) {
                                //Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_LONG).show();
                                Utiss.showToast(getApplicationContext(), "Registration Successfully", R.color.green);
                                Intent intent = new Intent(RegistrerActivity.this, OtpActivity.class);
                                intent.putExtra("mobile", edit_phonoe_no.getText().toString());
                                intent.putExtra("code", String.valueOf(spinner_country.getSelectedItem()));
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Registartion Failed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationModel> call, Throwable t) {
                            stopAnim();
                            Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }

}
