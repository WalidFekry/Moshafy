package com.appwalied.quran.masbaha;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.appwalied.quran.R;
import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.databinding.ActivityMasbahaBinding;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.google.android.material.button.MaterialButton;

public class MasbahaActivity extends BaseActivity {

    private ActivityMasbahaBinding binding;
    private SharedPreferences sharedPreferences;
    private Vibrator vibrator;

    private int selection = 1;
    private int currentCount = 0;
    private boolean isVibrationEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasbahaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkAndShowDialog(
                SharedPrefsConstants.MASBAHA_FIRST_TIME,
                "يحتوي هذا القسم على المسبحة الإلكترونية تعمل بدون اتصال انترنت 💙"
        );

        sharedPreferences = getSharedPreferences("masbaha", MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        isVibrationEnabled = sharedPreferences.getBoolean("vibration_enabled", true);

        updateVibrationButtonUI();

        binding.backButton.setOnClickListener(v -> finish());

        binding.btnPreset1.setOnClickListener(v -> selectPreset(1));
        binding.btnPreset2.setOnClickListener(v -> selectPreset(2));
        binding.btnPreset3.setOnClickListener(v -> selectPreset(3));
        binding.btnPreset4.setOnClickListener(v -> selectPreset(4));
        binding.btnPreset5.setOnClickListener(v -> selectPreset(5));

        binding.counterTapArea.setOnClickListener(v -> incrementCounter());

        binding.btnReset.setOnClickListener(v -> resetCounter());

        binding.btnVibrate.setOnClickListener(v -> toggleVibration());

        selectPreset(1);

        showBanner(binding.adsContainer);
    }

    private void incrementCounter() {
        int maxLimit = (selection == 5) ? 1000 : 100;
        currentCount++;
        if (currentCount > maxLimit) {
            currentCount = 1;
        }

        binding.tvCounter.setText(String.valueOf(currentCount));

        sharedPreferences.edit()
                .putInt("index" + selection, currentCount)
                .apply();

        if (isVibrationEnabled && vibrator != null && vibrator.hasVibrator()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(40);
                }
            } catch (Exception ignored) {}
        }
    }

    private void resetCounter() {
        currentCount = 0;
        binding.tvCounter.setText("0");
        sharedPreferences.edit()
                .putInt("index" + selection, 0)
                .apply();
        Toast.makeText(this, "تم إعادة ضبط العداد 🔄", Toast.LENGTH_SHORT).show();
    }

    private void toggleVibration() {
        isVibrationEnabled = !isVibrationEnabled;
        sharedPreferences.edit()
                .putBoolean("vibration_enabled", isVibrationEnabled)
                .apply();
        updateVibrationButtonUI();
        Toast.makeText(this, isVibrationEnabled ? "تم تفعيل الاهتزاز 📳" : "تم إيقاف الاهتزاز 🔕", Toast.LENGTH_SHORT).show();
    }

    private void updateVibrationButtonUI() {
        if (isVibrationEnabled) {
            binding.btnVibrate.setText("الاهتزاز: تفعيل 📳");
            binding.btnVibrate.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            binding.btnVibrate.setStrokeColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
        } else {
            binding.btnVibrate.setText("الاهتزاز: إيقاف 🔕");
            binding.btnVibrate.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            binding.btnVibrate.setStrokeColor(ContextCompat.getColorStateList(this, R.color.card_stroke));
        }
    }

    private void selectPreset(int type) {
        selection = type;
        currentCount = sharedPreferences.getInt("index" + type, 0);
        binding.tvCounter.setText(String.valueOf(currentCount));

        updatePresetButton(binding.btnPreset1, type == 1);
        updatePresetButton(binding.btnPreset2, type == 2);
        updatePresetButton(binding.btnPreset3, type == 3);
        updatePresetButton(binding.btnPreset4, type == 4);
        updatePresetButton(binding.btnPreset5, type == 5);

        switch (type) {
            case 1:
                binding.tvTitle.setText("لا إله إلا الله");
                binding.tvDesc.setText("فضلها: عن النبي ﷺ أنه قال: من قال لا إله إلا الله صدقاً من قلبه دخل الجنة");
                break;
            case 2:
                binding.tvTitle.setText("أستغفر الله العظيم وأتوب إليه");
                binding.tvDesc.setText("فضلها: قال رسول الله ﷺ: يا أيها الناس توبوا إلى الله، فإني أتوب في اليوم إليه مائة مرة.");
                break;
            case 3:
                binding.tvTitle.setText("سُبْحَانَ اللهِ، وَالْحَمْدُ لِلَّهِ، وَلَا إِلٰهَ إِلَّا اللهُ، وَاللهُ أَكْبَرُ");
                binding.tvDesc.setText("فضلها: تُغْرَسُ لَكَ بِكُلِّ وَاحِدَةٍ شَجَرَةٌ فِي الْجَنَّةِ");
                break;
            case 4:
                binding.tvTitle.setText("اللهم صلِّ على سيدنا محمد");
                binding.tvDesc.setText("فضلها: من صلى عليَّ حين يصبح عشراً وحين يمسي عشراً أدركته شفاعتي يوم القيامة");
                break;
            case 5:
                binding.tvTitle.setText("سبحان الله وبحمده");
                binding.tvDesc.setText("فضلها: من قالها مائة مرة حُطَّتْ خطاياه وإن كانت مثل زبد البحر");
                break;
        }
    }

    private void updatePresetButton(MaterialButton button, boolean isSelected) {
        if (isSelected) {
            button.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            button.setStrokeColor(ContextCompat.getColorStateList(this, R.color.colorPrimary));
            button.setStrokeWidth(3);
        } else {
            button.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            button.setStrokeColor(ContextCompat.getColorStateList(this, R.color.card_stroke));
            button.setStrokeWidth(1);
        }
    }
}
