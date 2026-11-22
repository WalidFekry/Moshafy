package com.appwalied.quran.image_quran.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.BottomSheetJuzIndexBinding;
import com.appwalied.quran.quran.db.QuranRepository;
import com.appwalied.quran.quran.db.models.Juz;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class JuzIndexBottomSheet extends BottomSheetDialogFragment {

    public interface OnJuzSelectedListener {
        void onJuzSelected(int startPage);
    }

    private BottomSheetJuzIndexBinding binding;
    private OnJuzSelectedListener listener;
    private final List<Juz> juzList = new ArrayList<>();
    private JuzAdapter adapter;

    public static JuzIndexBottomSheet newInstance() {
        return new JuzIndexBottomSheet();
    }

    public void setOnJuzSelectedListener(OnJuzSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetJuzIndexBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new JuzAdapter();
        binding.rvJuzIndex.setAdapter(adapter);

        QuranRepository.getInstance(requireContext()).getAllJuzs(list -> {
            if (list != null) {
                juzList.clear();
                juzList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_juz_index, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Juz juz = juzList.get(position);
            holder.tvNum.setText(String.valueOf(juz.getJuzNumber()));
            holder.tvTitle.setText("الجزء " + getArabicNumberWord(juz.getJuzNumber()));
            holder.tvStartInfo.setText("يبدأ من سورة " + juz.getStartSuraName() + " (آية " + juz.getStartAyahId() + ")");
            holder.tvStartPage.setText("صفحة " + juz.getStartPage());

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onJuzSelected(juz.getStartPage());
                }
                dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return juzList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvNum, tvTitle, tvStartInfo, tvStartPage;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNum = itemView.findViewById(R.id.tv_juz_num);
                tvTitle = itemView.findViewById(R.id.tv_juz_title);
                tvStartInfo = itemView.findViewById(R.id.tv_juz_start_info);
                tvStartPage = itemView.findViewById(R.id.tv_juz_start_page);
            }
        }
    }

    private String getArabicNumberWord(int number) {
        String[] words = {
            "الأول", "الثاني", "الثالث", "الرابع", "الخامس", "السادس", "السابع", "الثامن", "التاسع", "العاشر",
            "الحادي عشر", "الثاني عشر", "الثالث عشر", "الرابع عشر", "الخامس عشر", "السادس عشر", "السابع عشر", "الثامن عشر", "التاسع عشر", "العشرون",
            "الحادي والعشرون", "الثاني والعشرون", "الثالث والعشرون", "الرابع والعشرون", "الخامس والعشرون", "السادس والعشرون", "السابع والعشرون", "الثامن والعشرون", "التاسع والعشرون", "الثلاثون"
        };
        if (number >= 1 && number <= 30) {
            return words[number - 1];
        }
        return String.valueOf(number);
    }
}
