package com.appwalied.quran.quran.ui.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.audio.Reciter;
import com.appwalied.quran.quran.audio.ReciterRepository;
import com.appwalied.quran.quran.ui.adapters.ReciterAdapter;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class ReciterChooserBottomSheet extends BottomSheetDialogFragment {

    private String activeFolder = "Alafasy_128kbps";
    private OnReciterSelectedListener listener;
    private ReciterAdapter adapter;

    public interface OnReciterSelectedListener {
        void onReciterSelected(Reciter reciter);
    }

    public static ReciterChooserBottomSheet newInstance(String activeFolder, OnReciterSelectedListener listener) {
        ReciterChooserBottomSheet fragment = new ReciterChooserBottomSheet();
        fragment.activeFolder = activeFolder;
        fragment.listener = listener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_reciter_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etSearch = view.findViewById(R.id.et_search_reciter);
        ProgressBar progressBar = view.findViewById(R.id.pb_loading_reciters);
        RecyclerView recyclerView = view.findViewById(R.id.rv_reciters);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReciterAdapter(reciter -> {
            if (listener != null) {
                listener.onReciterSelected(reciter);
            }
            dismiss();
        });
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        ReciterRepository.getInstance(requireContext()).getReciters(false, new ReciterRepository.Callback<List<Reciter>>() {
            @Override
            public void onResult(List<Reciter> reciters) {
                progressBar.setVisibility(View.GONE);
                adapter.setReciters(reciters, activeFolder);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
}
