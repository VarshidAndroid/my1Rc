package com.rewardscards.a1rc.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Utils.Utiss;

public class KYCFragment extends Fragment {
ImageView kyc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kyc_document_layout, null);

        kyc = (ImageView) view.findViewById(R.id.crdview_pair);
        Utiss.showToast(getContext(),"Coming Soon",R.color.msg_fail);
        kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"pairing complete",Toast.LENGTH_LONG).show();
            }
        });
          return view;
        
    }
}
