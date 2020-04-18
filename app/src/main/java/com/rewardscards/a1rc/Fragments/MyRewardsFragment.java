package com.rewardscards.a1rc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;
import com.rewardscards.a1rc.Utils.Utiss;

public class MyRewardsFragment  extends Fragment {
    LinearLayout layout_line_one;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_rewards, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","3");
        Utiss.showToast(getContext(),"Coming Soon",R.color.msg_fail);

        return view;
    }
}
