package com.appwalied.quran.quran.quran_reading.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.quran_reading.adapter.AyatAdapter;
import com.appwalied.quran.quran.quran_reading.db.GetAllSurahList;
import com.appwalied.quran.quran.quran_reading.model.Ayat;

import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class AyatFragment extends Fragment {

    private AyatAdapter ayatAdapter;
    private String sura_name = "";
    private AppCompatImageButton back;
    private AppCompatTextView text;
    private RecyclerView ayatRecyclerView;
    private ArrayList<Ayat> ayatArrayList;

    public AyatFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new AyatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        int sura_id = 1;
        if (bundle != null) {
            sura_id = bundle.getInt("sura_id", 1);
            sura_name = bundle.getString("sura_name", "");
        }
        if (getContext() != null) {
            GetAllSurahList getAllSurahList = new GetAllSurahList(getContext());
            ayatArrayList = getAllSurahList.GetAyatBySurah(sura_id);
        } else {
            ayatArrayList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayat, container, false);
        back = view.findViewById(R.id.back_button);
        text = view.findViewById(R.id.toolbar_title);

        if (text != null) {
            text.setText("سورة " + sura_name);
        }

        if (back != null) {
            back.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        ayatRecyclerView = view.findViewById(R.id.recycler_ayat);
        VerticalRecyclerViewFastScroller fastScroller = view.findViewById(R.id.fast_scroller);

        if (fastScroller != null && ayatRecyclerView != null) {
            fastScroller.setRecyclerView(ayatRecyclerView);
            ayatRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        }

        if (ayatRecyclerView != null && getContext() != null) {
            ayatAdapter = new AyatAdapter(getContext(), ayatArrayList);
            ayatRecyclerView.setHasFixedSize(true);
            ayatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            ayatRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            ayatRecyclerView.setAdapter(ayatAdapter);
        }

        return view;
    }
}
