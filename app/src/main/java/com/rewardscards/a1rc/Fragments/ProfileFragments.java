package com.rewardscards.a1rc.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.rewardscards.a1rc.AboutActivity;
import com.rewardscards.a1rc.CardTermsActivity;
import com.rewardscards.a1rc.FaqActivity;
import com.rewardscards.a1rc.FaqListActivity;
import com.rewardscards.a1rc.MainActivity;
import com.rewardscards.a1rc.Networks.ApiUtils;
import com.rewardscards.a1rc.Networks.WebApi;
import com.rewardscards.a1rc.PrivacyPolicyActivity;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Response.DeleteAccountResponse;
import com.rewardscards.a1rc.Response.PRofileUpdateSuccesResponse;
import com.rewardscards.a1rc.Response.ProfileModel;
import com.rewardscards.a1rc.SplashScreenActivity;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;
import com.rewardscards.a1rc.params.DeleteAccountParam;
import com.rewardscards.a1rc.params.ProfilePicParam;
import com.rewardscards.a1rc.params.ProfileupdatePRama;
import com.google.android.gms.common.api.Api;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragments extends Fragment {
    LinearLayout txt_profile,txt_about,txt_kyc;
    RelativeLayout layout_profile,layout_aboutus,layout_kyc;
    ConstraintLayout card_about,card_faq,card_terms,card_privacy;
    View view_1,view_2,view_3;
    Button btn_save;
    TextView edit_firstname,edit_last_name,edit_sa_id,edit_phonr,
             edit_email,edit_password,id_conf_pass,txt_delete;
            EditText edit_address_1,edit_address_2,edit_address_3,edit_address_4;

    com.wang.avi.AVLoadingIndicatorView avi;
    CircleImageView img_upload_image;
    private Button btn;
    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","1");
        avi = (AVLoadingIndicatorView)view.findViewById(R.id.avi);
        btn_save = (Button)view.findViewById(R.id.btn_save);
        edit_firstname = (TextView)view.findViewById(R.id.edit_firstname);
        edit_last_name = (TextView)view.findViewById(R.id.edit_last_name);
        edit_sa_id = (TextView)view.findViewById(R.id.edit_sa_id);
        edit_phonr = (TextView)view.findViewById(R.id.edit_phonr);
        edit_email = (TextView)view.findViewById(R.id.edit_email);
        edit_password = (TextView)view.findViewById(R.id.edit_password);
        id_conf_pass = (TextView)view.findViewById(R.id.id_conf_pass);
        edit_address_1 = (EditText)view.findViewById(R.id.edit_address_1);
        edit_address_2 = (EditText)view.findViewById(R.id.edit_address_2);
        edit_address_3 = (EditText)view.findViewById(R.id.edit_address_3);
        edit_address_4 = (EditText)view.findViewById(R.id.edit_address_4);
        txt_delete = (TextView)view.findViewById(R.id.txt_delete);
        img_upload_image = (CircleImageView)view.findViewById(R.id.img_upload_image);
        Glide
                .with(getActivity())
                .load("https://1rewardscards.com/API/" + SharedPrefsUtils.getSharedPreferenceString(getActivity(),"profile_pic"))
                .centerCrop()
                .placeholder(R.drawable.add_btn)
                .into(img_upload_image);


        imageview = (ImageView)view.findViewById(R.id.img_upload_image);
        img_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });


        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CustomAlertDialog);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.delete_account, viewGroup, false);
                Button buttonOk=dialogView.findViewById(R.id.buttonOk);
                Button buttonyes = dialogView.findViewById(R.id.buttonyes);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                buttonyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                        DeleteAccountParam deleteAccountParam = new DeleteAccountParam(SharedPrefsUtils.getSharedPreferenceString(getActivity(),"user_id"));
                        Call<DeleteAccountResponse> call= webApi.delete_account(deleteAccountParam);
                        call.enqueue(new Callback<DeleteAccountResponse>() {
                            @Override
                            public void onResponse(Call<DeleteAccountResponse> call, Response<DeleteAccountResponse> response) {
                                if (response.body().getMessage().equalsIgnoreCase("Profile Deleted Succesfully")){
                                    Utiss.showToast(getContext(),"Profile Deleted Succesfully",R.color.green);
                                }else {
                                    Utiss.showToast(getContext(),"Unable to Deleted Account",R.color.green);
                                }
                                SharedPrefsUtils.setSharedPreferenceString(getActivity(),"login","0");
                                Intent intent= new Intent(getActivity(),SplashScreenActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<DeleteAccountResponse> call, Throwable t) {
                                    Utiss.showToast(getContext(),"Server Error",R.color.msg_fail);
                            }
                        });

                    }
                });
                alertDialog.show();
            }
        });
        edit_firstname.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"first_name"));
        edit_last_name.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"last_name"));
        edit_sa_id.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"sa_identity_number"));
        edit_phonr.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"phone"));
        edit_email.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"email"));
        edit_password.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"password"));

        edit_address_1.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"address_1"));
        edit_address_2.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"address_2"));
        edit_address_3.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"address_3"));
        edit_address_4.setText(SharedPrefsUtils.getSharedPreferenceString(getContext(),"address_4"));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (edit_address_1.getText().toString().isEmpty()){
                    edit_address_1.setError("Enter Address 1");
                }else  if (edit_address_2.getText().toString().isEmpty()){
                    edit_address_2.setError("Enter Address 2");
                }else  if (edit_address_3.getText().toString().isEmpty()){
                    edit_address_3.setError("Enter Address 3");
                }else  if (edit_address_4.getText().toString().isEmpty()){
                    edit_address_4.setError("Enter Address 4");
                }else {
                   startAnim();

                   WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                   ProfileupdatePRama profileupdatePRama =  new ProfileupdatePRama(SharedPrefsUtils.getSharedPreferenceString(getActivity(),"sa_identity_number"),
                           edit_address_1.getText().toString(),
                           edit_address_2.getText().toString(),
                           edit_address_3.getText().toString(),
                           edit_address_4.getText().toString());
                   Call<PRofileUpdateSuccesResponse> call = webApi.update_profile(profileupdatePRama);
                   call.enqueue(new Callback<PRofileUpdateSuccesResponse>() {
                       @Override
                       public void onResponse(Call<PRofileUpdateSuccesResponse> call, Response<PRofileUpdateSuccesResponse> response) {
                           stopAnim();
                            if (response.body().getMessage().equalsIgnoreCase("Date Updated Succesfully")){
                                Utiss.showToast(getContext(),"Data Saved Successfully",R.color.green);

                                SharedPrefsUtils.setSharedPreferenceString(getContext(), "address_1",edit_address_1.getText().toString());
                                SharedPrefsUtils.setSharedPreferenceString(getContext(), "address_2",edit_address_2.getText().toString());
                                SharedPrefsUtils.setSharedPreferenceString(getContext(), "address_3",edit_address_3.getText().toString());
                                SharedPrefsUtils.setSharedPreferenceString(getContext(), "address_4",edit_address_4.getText().toString());

                            }else {
                                Utiss.showToast(getContext(),response.body().getMessage(),R.color.msg_fail);
                            }
                       }

                       @Override
                       public void onFailure(Call<PRofileUpdateSuccesResponse> call, Throwable t) {
                           stopAnim();
                           Utiss.showToast(getContext(),"Server Error",R.color.msg_fail);
                       }
                   });
                }
            }
        });


        txt_profile = (LinearLayout)view.findViewById(R.id.txt_profile);
        txt_about = (LinearLayout)view.findViewById(R.id.txt_about);
        txt_kyc = (LinearLayout)view.findViewById(R.id.txt_kyc);

        layout_profile = (RelativeLayout)view.findViewById(R.id.layout_profile);
        layout_aboutus = (RelativeLayout)view.findViewById(R.id.layout_aboutus);
        layout_kyc = (RelativeLayout)view.findViewById(R.id.layout_kyc);

        view_1 =(View)view.findViewById(R.id.view_1);
        view_2 =(View)view.findViewById(R.id.view_2);
        view_3 =(View)view.findViewById(R.id.view_3);


        txt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_profile.setVisibility(View.VISIBLE);
                layout_aboutus.setVisibility(View.GONE);
                layout_kyc.setVisibility(View.GONE);

                view_1.setVisibility(View.VISIBLE);
                view_2.setVisibility(View.INVISIBLE);
                view_3.setVisibility(View.INVISIBLE);
            }
        });

        txt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_profile.setVisibility(View.GONE);
                layout_aboutus.setVisibility(View.VISIBLE);
                layout_kyc.setVisibility(View.GONE);


                view_1.setVisibility(View.INVISIBLE);
                view_2.setVisibility(View.VISIBLE);
                view_3.setVisibility(View.INVISIBLE);
            }
        });

        txt_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_profile.setVisibility(View.GONE);
                layout_aboutus.setVisibility(View.GONE);
                layout_kyc.setVisibility(View.VISIBLE);
                Utiss.showToast(getContext(),"Coming Soon",R.color.msg_fail);
                view_1.setVisibility(View.INVISIBLE);
                view_2.setVisibility(View.INVISIBLE);
                view_3.setVisibility(View.VISIBLE);

            }
        });




        card_about = (ConstraintLayout)view.findViewById(R.id.card_about);
        card_faq = (ConstraintLayout)view.findViewById(R.id.card_faq);
        card_terms = (ConstraintLayout)view.findViewById(R.id.card_terms);
        card_privacy = (ConstraintLayout)view.findViewById(R.id.card_privacy);

        card_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FaqListActivity.class);
                startActivity(intent);
            }
        });
        card_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        card_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardTermsActivity.class);
                startActivity(intent);
            }
        });

        card_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                    String img =  Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    ProfilePicParam profilePicParam = new ProfilePicParam(SharedPrefsUtils.getSharedPreferenceString(getActivity(),"user_id"),
                            img);
                    Call<ProfileModel> call = webApi.update_profile_pic(profilePicParam);
                    call.enqueue(new Callback<ProfileModel>() {
                        @Override
                        public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                            if (response.body().getMessage().equalsIgnoreCase("Profile Pic Updated Succesfully")){
                                Utiss.showToast(getActivity(),"Profile Pic Updated Succesfully",R.color.green);
                                SharedPrefsUtils.setSharedPreferenceString(getActivity(),"profile_pic",response.body().getProfilePic());
                            }else {
                                Utiss.showToast(getActivity(),response.body().getMessage(),R.color.msg_fail);
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileModel> call, Throwable t) {
                        Utiss.showToast(getActivity(),"Server Error",R.color.msg_fail);
                        }
                    });



                   // Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
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
