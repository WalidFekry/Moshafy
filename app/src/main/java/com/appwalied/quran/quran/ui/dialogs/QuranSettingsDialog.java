package com.appwalied.quran.quran.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appwalied.quran.R;
import com.appwalied.quran.quran.room.QuranSettingsEntity;
import com.appwalied.quran.quran.room.UserLocalRepository;
import com.appwalied.quran.quran.utils.QuranFontHelper;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class QuranSettingsDialog extends Dialog {

    private QuranSettingsEntity currentSettings;
    private OnSettingsChangedListener listener;

    public interface OnSettingsChangedListener {
        void onSettingsChanged(QuranSettingsEntity settings);
    }

    public QuranSettingsDialog(@NonNull Context context, OnSettingsChangedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_quran_settings);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        RadioGroup rgFont = findViewById(R.id.rg_font_selection);
        RadioButton rbUthmanii = findViewById(R.id.rb_font_uthmanii);
        RadioButton rbQuranMsg = findViewById(R.id.rb_font_quran_msg);

        SeekBar sbFontSize = findViewById(R.id.sb_font_size);
        ImageButton btnFontDecrease = findViewById(R.id.btn_font_decrease);
        ImageButton btnFontIncrease = findViewById(R.id.btn_font_increase);
        TextView tvFontPreview = findViewById(R.id.tv_font_preview);

        SwitchMaterial switchKeepScreenOn = findViewById(R.id.switch_keep_screen_on);
        SwitchMaterial switchAutoPlayNext = findViewById(R.id.switch_auto_play_next);

        SeekBar sbRepeatCount = findViewById(R.id.sb_repeat_count);
        TextView tvRepeatCount = findViewById(R.id.tv_repeat_count);

        Button btnSave = findViewById(R.id.btn_save_settings);

        UserLocalRepository.getInstance(getContext()).getSettings(settings -> {
            this.currentSettings = settings;

            String fontKey = settings.getSelectedFont();
            if (QuranFontHelper.FONT_QURAN_MSG.equals(fontKey)) {
                rbQuranMsg.setChecked(true);
            } else {
                rbUthmanii.setChecked(true);
            }
            tvFontPreview.setTypeface(QuranFontHelper.getFont(getContext(), fontKey));

            int progressFontSize = Math.max(0, Math.min(24, settings.getFontSize() - 16));
            sbFontSize.setProgress(progressFontSize);
            tvFontPreview.setTextSize(settings.getFontSize());

            switchKeepScreenOn.setChecked(settings.isKeepScreenOn());
            switchAutoPlayNext.setChecked(settings.isAutoPlayNextAyah());

            int repeatIndex = Math.max(0, Math.min(4, settings.getRepeatCount() - 1));
            sbRepeatCount.setProgress(repeatIndex);
            updateRepeatLabel(tvRepeatCount, settings.getRepeatCount());
        });

        rgFont.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedFontKey = (checkedId == R.id.rb_font_quran_msg)
                    ? QuranFontHelper.FONT_QURAN_MSG
                    : QuranFontHelper.FONT_UTHMANII;
            if (currentSettings != null) {
                currentSettings.setSelectedFont(selectedFontKey);
            }
            tvFontPreview.setTypeface(QuranFontHelper.getFont(getContext(), selectedFontKey));
        });

        sbFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int size = 16 + progress;
                tvFontPreview.setTextSize(size);
                if (currentSettings != null) {
                    currentSettings.setFontSize(size);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnFontDecrease.setOnClickListener(v -> {
            int current = sbFontSize.getProgress();
            if (current > 0) {
                sbFontSize.setProgress(current - 2);
            }
        });

        btnFontIncrease.setOnClickListener(v -> {
            int current = sbFontSize.getProgress();
            if (current < sbFontSize.getMax()) {
                sbFontSize.setProgress(current + 2);
            }
        });

        sbRepeatCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int count = progress + 1;
                updateRepeatLabel(tvRepeatCount, count);
                if (currentSettings != null) {
                    currentSettings.setRepeatCount(count);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> {
            if (currentSettings != null) {
                currentSettings.setKeepScreenOn(switchKeepScreenOn.isChecked());
                currentSettings.setAutoPlayNextAyah(switchAutoPlayNext.isChecked());

                UserLocalRepository.getInstance(getContext()).updateSettings(currentSettings);

                if (listener != null) {
                    listener.onSettingsChanged(currentSettings);
                }
                Toast.makeText(getContext(), "تم حفظ الإعدادات بنجاح", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
    }

    private void updateRepeatLabel(TextView tv, int count) {
        if (count == 1) {
            tv.setText("مرة واحدة");
        } else if (count == 2) {
            tv.setText("مرتان");
        } else {
            tv.setText(count + " مرات");
        }
    }
}
