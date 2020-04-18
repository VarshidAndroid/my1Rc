package com.rewardscards.a1rc.Services;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.w("token",refreshedToken);

        SharedPrefsUtils.setSharedPreferenceString(getApplicationContext(),"token",refreshedToken);
       /* // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
       // sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

}
