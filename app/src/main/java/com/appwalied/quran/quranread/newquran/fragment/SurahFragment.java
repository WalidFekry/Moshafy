package com.appwalied.quran.quranread.newquran.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quranread.newquran.adapter.SuraListAdapter;
import com.appwalied.quran.quranread.newquran.db.GetAllSurahList;
import com.appwalied.quran.quranread.newquran.model.Surah;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurahFragment extends Fragment {

    RecyclerView sRecyclerView;
    ArrayList<Surah> surahArrayList;
    SuraListAdapter adapter;
    LinearLayout lastview;
    int pos;
    int sura_id;
    String sura_name;

    public SurahFragment() {
        // Required empty public constructor
    }

    public static SurahFragment newInstance() {
        SurahFragment surahFragment = new SurahFragment();
        return surahFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surahArrayList = new ArrayList<>();
        GetAllSurahList getAllSurahList = new GetAllSurahList(getContext());
        surahArrayList = getAllSurahList.AllSurahList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_surah, container, false);

        sRecyclerView = view.findViewById(R.id.suraRecyclerView);
        lastview = view.findViewById(R.id.bookmark);
        adapter = new SuraListAdapter(surahArrayList, getActivity());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LASTREADAYAT", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("AYAT_ID") && sharedPreferences.contains("SURA_ID"))
        {

            pos = Integer.parseInt(sharedPreferences.getString("AYAT_ID", null));
            sura_id = Integer.parseInt(sharedPreferences.getString("SURA_ID", null));
            sura_name = surahArrayList.get(sura_id - 1).getName();
        }

        lastview.setVisibility(View.VISIBLE);

        lastview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LastReadFragment lastReadFragment = new LastReadFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("sura_id", sura_id);
                bundle.putInt("position", pos);
                bundle.putString("sura_name", sura_name);
                lastReadFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment,lastReadFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sRecyclerView.setHasFixedSize(true);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        sRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        sRecyclerView.setAdapter(adapter);


    }


}
