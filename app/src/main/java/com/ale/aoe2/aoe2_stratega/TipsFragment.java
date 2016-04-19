package com.ale.aoe2.aoe2_stratega;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ale on 07/03/2016.
 */
public class TipsFragment extends android.support.v4.app.Fragment {
    RecyclerView lv;
    TextView tv;
    TextView tva;
    LinearLayout lLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity superActivity = super.getActivity();
        lLayout = (LinearLayout) inflater.inflate(R.layout.tips_fragment, container, false);

        String[] general_tips = getResources().getStringArray(R.array.noob_tips);
        ArrayList<Tip> _tips = new ArrayList<>();
        for (String string_tip : general_tips) {
            _tips.add(new Tip(0, string_tip));
        }
        ArrayList<String> tips = new ArrayList<>(Arrays.asList(general_tips));
        tv = (TextView)lLayout.findViewById(R.id.tip_header);
        tva = (TextView)lLayout.findViewById(R.id.tip_author);
        tv.setText(R.string.tip_fragment_title);
        tva.setText(R.string.tip_fragment_sub);
        lv = (RecyclerView)lLayout.findViewById(R.id.general_tips_list_view);
        TipAdapter itemsAdapter = new TipAdapter(_tips, superActivity);
        LinearLayoutManager llm = new LinearLayoutManager(superActivity);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(llm);
        lv.setAdapter(itemsAdapter);

        String[] noobs_tips = getResources().getStringArray(R.array.general_tips);
        for (String string_tip : noobs_tips) {
            _tips.add(new Tip(1, string_tip));
        }
        tips.addAll(Arrays.asList(noobs_tips));

        String[] pro_tips = getResources().getStringArray(R.array.pro_tips);
        for (String string_tip : pro_tips) {
            _tips.add(new Tip(2, string_tip));
        }
        tips.addAll(Arrays.asList(pro_tips));

        //overrideFonts(superActivity, container);
        return lLayout;
    }
}
