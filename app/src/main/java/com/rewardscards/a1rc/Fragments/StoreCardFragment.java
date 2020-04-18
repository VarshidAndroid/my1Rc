package com.rewardscards.a1rc.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.rewardscards.a1rc.ActiveCardActivity;
import com.rewardscards.a1rc.ApplyNowActivity;
import com.rewardscards.a1rc.R;
import com.rewardscards.a1rc.Utils.SharedPrefsUtils;

public class StoreCardFragment extends Fragment {
    CardView cardview_apply,cardview_activae,crdview_pair;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_crards_fragment, null);
        SharedPrefsUtils.setSharedPreferenceString(getContext(),"back","5");
        cardview_apply = (CardView)view.findViewById(R.id.cardview_apply);
        cardview_activae = (CardView)view.findViewById(R.id.cardview_activae);
        crdview_pair = (CardView)view.findViewById(R.id.crdview_pair);

        cardview_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ApplyNowActivity.class);
                startActivity(intent);
            }
        });

        cardview_activae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActiveCardActivity.class);
                startActivity(intent);
            }
        });

        crdview_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"pairing complete",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
