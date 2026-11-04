package com.appwalied.quran.azkar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appwalied.quran.R;
import com.appwalied.quran.databinding.ItemAzkarCardBinding;

import java.util.List;
import java.util.Locale;

public class AzkarAdapter extends RecyclerView.Adapter<AzkarAdapter.AzkarViewHolder> {

    private final Context context;
    private final List<AzkarModel> azkarList;
    private int currentTextSizeSp;

    public interface OnZikrStatusChangeListener {
        void onZikrStatusChanged(int position, AzkarModel model);
    }

    private OnZikrStatusChangeListener listener;

    public AzkarAdapter(Context context, List<AzkarModel> azkarList) {
        this.context = context;
        this.azkarList = azkarList;
        this.currentTextSizeSp = AzkarManager.getTextSize(context, 20);
    }

    public void setOnZikrStatusChangeListener(OnZikrStatusChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AzkarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAzkarCardBinding binding = ItemAzkarCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new AzkarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AzkarViewHolder holder, int position) {
        AzkarModel item = azkarList.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return azkarList != null ? azkarList.size() : 0;
    }

    public class AzkarViewHolder extends RecyclerView.ViewHolder {
        private final ItemAzkarCardBinding binding;

        public AzkarViewHolder(@NonNull ItemAzkarCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AzkarModel item, int position) {
            // Text & Font Size
            binding.tvZikrText.setText(item.getZikrText());
            binding.tvZikrText.setTextSize(currentTextSizeSp);

            // Badge Count
            String targetCountText = String.format(Locale.getDefault(), "التكرار: %d %s",
                    item.getTargetCount(), item.getTargetCount() == 1 ? "مرة" : "مرات");
            binding.tvBadgeCount.setText(targetCountText);

            // Virtue / Description Box
            if (item.getVirtueText() != null && !item.getVirtueText().trim().isEmpty()) {
                binding.layoutVirtue.setVisibility(View.VISIBLE);
                binding.tvVirtueText.setText(item.getVirtueText());
            } else {
                binding.layoutVirtue.setVisibility(View.GONE);
            }

            // Reset Counter
            binding.btnReset.setOnClickListener(v -> {
                item.resetCount();
                AzkarManager.resetCount(context, item.getId());
                updateCounterUI(item);
                Toast.makeText(context, "تمت إعادة ضبط العداد", Toast.LENGTH_SHORT).show();
                if (listener != null) listener.onZikrStatusChanged(position, item);
            });

            // Counter Logic
            updateCounterUI(item);
            binding.btnCounter.setOnClickListener(v -> {
                if (!item.isCompleted()) {
                    item.incrementCount();
                    AzkarManager.saveCount(context, item.getId(), item.getCurrentCount());
                    updateCounterUI(item);
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    if (item.isCompleted()) {
                        triggerVibration();
                        Toast.makeText(context, "أحسنت! اكتمل الذكر", Toast.LENGTH_SHORT).show();
                    }
                    if (listener != null) listener.onZikrStatusChanged(position, item);
                } else {
                    Toast.makeText(context, "اكتمل الذكر بالفعل! اضغط زر الإعادة لإعادة العد", Toast.LENGTH_SHORT).show();
                }
            });

            // Font Size Adjustment
            binding.btnFontMinus.setOnClickListener(v -> {
                if (currentTextSizeSp > 15) {
                    currentTextSizeSp -= 2;
                    AzkarManager.saveTextSize(context, currentTextSizeSp);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "هذا أصغر حجم للخط", Toast.LENGTH_SHORT).show();
                }
            });

            binding.btnFontPlus.setOnClickListener(v -> {
                if (currentTextSizeSp < 36) {
                    currentTextSizeSp += 2;
                    AzkarManager.saveTextSize(context, currentTextSizeSp);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "هذا أكبر حجم للخط", Toast.LENGTH_SHORT).show();
                }
            });

            // Copy Action
            binding.btnCopy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("Zikr", item.getZikrText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "تم نسخ الذكر بنجاح 📋", Toast.LENGTH_SHORT).show();
                }
            });

            // Share Action
            binding.btnShare.setOnClickListener(v -> {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                String shareBody = item.getZikrText() + "\n\n" +
                        (item.getVirtueText().isEmpty() ? "" : (item.getVirtueText() + "\n\n")) +
                        "مشاركة من تطبيق مصحفي:\nhttps://play.google.com/store/apps/details?id=com.appwalied.quran";
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sendIntent, "مشاركة الذكر:"));
            });
        }

        private void updateCounterUI(AzkarModel item) {
            binding.progressCounter.setMax(item.getTargetCount());
            binding.progressCounter.setProgress(item.getCurrentCount());

            if (item.isCompleted()) {
                binding.btnCounter.setBackgroundResource(R.drawable.bg_counter_completed);
                binding.ivCounterCheck.setVisibility(View.VISIBLE);
                binding.tvCounterStatus.setText("اكتمل الذكر");
            } else {
                binding.btnCounter.setBackgroundResource(R.drawable.bg_counter_btn);
                binding.ivCounterCheck.setVisibility(View.GONE);
                String status = String.format(Locale.getDefault(), "اضغط للعد  ( %d / %d )",
                        item.getCurrentCount(), item.getTargetCount());
                binding.tvCounterStatus.setText(status);
            }
        }

        private void triggerVibration() {
            try {
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null && v.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(150);
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
