package com.rewardscards.a1rc.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.rewardscards.a1rc.AboutActivity;
import com.rewardscards.a1rc.CardTermsActivity;
import com.rewardscards.a1rc.PrivacyPolicyActivity;
import com.rewardscards.a1rc.R;

public class AboutUsFragment extends Fragment {
    ConstraintLayout card_about,card_faq,card_terms,card_privacy;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_us_fragment, null);
        card_about = (ConstraintLayout)view.findViewById(R.id.card_about);
        card_faq = (ConstraintLayout)view.findViewById(R.id.card_faq);
        card_terms = (ConstraintLayout)view.findViewById(R.id.card_terms);
        card_privacy = (ConstraintLayout)view.findViewById(R.id.card_privacy);

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
}
