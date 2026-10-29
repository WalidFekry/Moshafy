package com.appwalied.quran.quran.quran_reading.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
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

public class LastReadFragment extends Fragment {

    private LastReadAdapter ayatAdapter;
    private AppCompatImageButton back;
    private int position = 0;
    private RecyclerView ayatRecyclerView;
    private ArrayList<Ayat> ayatArrayList;

    public LastReadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        int sura_id = 1;
        if (bundle != null) {
            sura_id = bundle.getInt("sura_id", 1);
        }
        if (getContext() != null) {
            GetAllSurahList getAllSurahList = new GetAllSurahList(getContext());
            ayatArrayList = getAllSurahList.GetAyatBySurah(sura_id);
        } else {
            ayatArrayList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_last_read, container, false);

        if (getContext() != null) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("LASTREADAYAT", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("AYAT_ID")) {
                try {
                    position = Integer.parseInt(sharedPreferences.getString("AYAT_ID", "0"));
                } catch (NumberFormatException ignored) {
                    position = 0;
                }
            }
        }

        back = view.findViewById(R.id.back_button);
        if (back != null) {
            back.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        ayatRecyclerView = view.findViewById(R.id.lastReadrecycler_ayat);
        VerticalRecyclerViewFastScroller fastScroller = view.findViewById(R.id.fast_scroller);

        if (fastScroller != null && ayatRecyclerView != null) {
            fastScroller.setRecyclerView(ayatRecyclerView);
            ayatRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        }

        if (ayatRecyclerView != null && getContext() != null) {
            ayatAdapter = new LastReadAdapter(getContext(), ayatArrayList);
            ayatRecyclerView.setHasFixedSize(true);
            ayatRecyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getContext()));
            ayatRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            ayatRecyclerView.setAdapter(ayatAdapter);
            ayatRecyclerView.smoothScrollToPosition(position);
        }

        return view;
    }
}
