package com.appwalied.quran.quran.quran_reading.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.quran_reading.QuranRead;
import com.appwalied.quran.quran.quran_reading.adapter.AyatAdapter;
import com.appwalied.quran.quran.quran_reading.db.GetAllSurahList;
import com.appwalied.quran.quran.quran_reading.model.Ayat;

import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A simple {@link Fragment} subclass.
 */
public class AyatFragment extends Fragment {

    AyatAdapter ayatAdapter;
    String sura_name;
    AppCompatImageButton back;
    AppCompatTextView text;
    private RecyclerView ayatRecyclerView;
    private ArrayList<Ayat> ayatArrayList;

    public AyatFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        AyatFragment ayatFragment = new AyatFragment();
        return ayatFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        int sura_id = bundle.getInt("sura_id");
        sura_name = bundle.getString("sura_name");
        ayatArrayList = new ArrayList<>();
        GetAllSurahList getAllSurahList = new GetAllSurahList(getContext());
        ayatArrayList = getAllSurahList.GetAyatBySurah(sura_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ayat, container, false);
        back = view.findViewById(R.id.back_button);
        text = view.findViewById(R.id.toolbar_title);
        text.setText("سورة "+sura_name);

        back.setOnClickListener(view1 -> requireActivity().getSupportFragmentManager().popBackStack());


        ayatRecyclerView = view.findViewById(R.id.recycler_ayat);
        //for fast scroll
        VerticalRecyclerViewFastScroller fastScroller = view.findViewById(R.id.fast_scroller);

        // let the scroller scroll the list
        fastScroller.setRecyclerView(ayatRecyclerView);

        //  let the recycler scroll the scroller's handle
        ayatRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        ayatAdapter = new AyatAdapter(getContext(), ayatArrayList);
        ayatRecyclerView.setHasFixedSize(true);
        ayatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ayatRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        ayatRecyclerView.setAdapter(ayatAdapter);

        return view;
    }



}
