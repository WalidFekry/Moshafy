package com.appwalied.quran.image_quran.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.BottomSheetSurahIndexBinding;
import com.appwalied.quran.quran.db.QuranRepository;
import com.appwalied.quran.quran.db.models.Surah;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SurahIndexBottomSheet extends BottomSheetDialogFragment {

    public interface OnSurahSelectedListener {
        void onSurahSelected(int startPage);
    }

    private BottomSheetSurahIndexBinding binding;
    private OnSurahSelectedListener listener;
    private final List<Surah> fullSurahList = new ArrayList<>();
    private final List<Surah> filteredSurahList = new ArrayList<>();
    private SurahAdapter adapter;

    public static SurahIndexBottomSheet newInstance() {
        return new SurahIndexBottomSheet();
    }

    public void setOnSurahSelectedListener(OnSurahSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetSurahIndexBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SurahAdapter();
        binding.rvSurahIndex.setAdapter(adapter);

        QuranRepository.getInstance(requireContext()).getAllSurahs(surahs -> {
            if (surahs != null) {
                fullSurahList.clear();
                fullSurahList.addAll(surahs);
                filterList("");
            }
        });

        binding.etSearchSurah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList(String query) {
        filteredSurahList.clear();
        String normalizedQuery = QuranRepository.normalizeArabic(query);
        for (Surah s : fullSurahList) {
            String normalizedName = QuranRepository.normalizeArabic(s.getNameAr());
            if (normalizedQuery.isEmpty() || normalizedName.contains(normalizedQuery)) {
                filteredSurahList.add(s);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_surah_index, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Surah surah = filteredSurahList.get(position);
            holder.tvNum.setText(String.valueOf(surah.getId()));
            holder.tvName.setText("سورة " + surah.getNameAr());
            holder.tvVersesCount.setText("عدد آياتها: " + surah.getVersesCount());
            holder.tvStartPage.setText("صفحة " + surah.getStartPage());

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSurahSelected(surah.getStartPage());
                }
                dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return filteredSurahList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvNum, tvName, tvVersesCount, tvStartPage;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNum = itemView.findViewById(R.id.tv_surah_num);
                tvName = itemView.findViewById(R.id.tv_surah_name);
                tvVersesCount = itemView.findViewById(R.id.tv_surah_verses_count);
                tvStartPage = itemView.findViewById(R.id.tv_surah_start_page);
            }
        }
    }
}
