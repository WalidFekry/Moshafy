package com.appwalied.quran.quran.quran_reading.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;


import com.appwalied.quran.R;
import com.appwalied.quran.quran.quran_reading.adapter.LastReadAdapter;
import com.appwalied.quran.quran.quran_reading.db.GetAllSurahList;
import com.appwalied.quran.quran.quran_reading.model.Ayat;
import com.appwalied.quran.quran.quran_reading.utilities.LinearLayoutManagerWithSmoothScroller;

import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastReadFragment extends Fragment {
    LastReadAdapter ayatAdapter;
    String sura_name;
    private RecyclerView ayatRecyclerView;
    private ArrayList<Ayat> ayatArrayList;
    int position = 0;

    public LastReadFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        LastReadFragment lastReadFragment = new LastReadFragment();
        return lastReadFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        int sura_id = bundle.getInt("sura_id");
       // position = bundle.getInt("pos");
        sura_name = bundle.getString("sura_name");
        ayatArrayList = new ArrayList<>();
        GetAllSurahList getAllSurahList = new GetAllSurahList(getContext());
        ayatArrayList = getAllSurahList.GetAyatBySurah(sura_id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_last_read, container, false);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LASTREADAYAT", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("AYAT_ID"))
        {
            position = Integer.parseInt(sharedPreferences.getString("AYAT_ID",null));
        }




        ayatRecyclerView = view.findViewById(R.id.lastReadrecycler_ayat);
        //for fast scroll
        VerticalRecyclerViewFastScroller fastScroller = view.findViewById(R.id.fast_scroller);

        // let the scroller scroll the list
        fastScroller.setRecyclerView(ayatRecyclerView);

        //  let the recycler scroll the scroller's handle
        ayatRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        ayatAdapter = new LastReadAdapter(getContext(), ayatArrayList);
        ayatRecyclerView.setHasFixedSize(true);
        ayatRecyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getContext()));
        ayatRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        ayatRecyclerView.setAdapter(ayatAdapter);

        if (position == 0) {
            ayatRecyclerView.smoothScrollToPosition(position);
        }else {
            ayatRecyclerView.smoothScrollToPosition(position);
        }
        return view;
    }

}
