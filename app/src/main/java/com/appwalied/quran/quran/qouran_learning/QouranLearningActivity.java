package com.appwalied.quran.quran.qouran_learning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.appwalied.quran.base.BaseActivity;
import com.appwalied.quran.utils.shared_helper.SharedPrefsConstants;
import com.appwalied.quran.R;
import com.appwalied.quran.utils.shared_helper.SharedHelper;
import com.appwalied.quran.utils.shared_helper.views.CustomDialogClass;
import com.appwalied.quran.quran.qouran_learning.models.LearningData;
import com.appwalied.quran.quran.qouran_learning.models.Reciter;
import com.appwalied.quran.quran.qouran_learning.models.ReciterResponse;
import com.appwalied.quran.quran.qouran_learning.models.Sora;
import com.appwalied.quran.quran.qouran_learning.models.SoraDetails;
import com.appwalied.quran.quran.qouran_learning.models.SoraDetailsResponse;
import com.appwalied.quran.quran.qouran_learning.networking.QuranUtils;
import com.appwalied.quran.quran.qouran_learning.networking.RetrofitClient;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QouranLearningActivity extends BaseActivity {

    private static final String TAG = "QouranLearningActivity";
    private final List<Sora> soraList = QuranUtils.getSoraList();
    List<Reciter> reciterList;
    SoraDetails soraDetails;
    TextView reciterTextView, soraTextView, startLearn, fromAyaTextView, repeatSoraTextView, repeatAyaTextView, toAyaTextView, startLearnDone;
    int lastReciter = 0;
    ImageView rateApp;
    private Dialog loadingDialog;
    private AppCompatImageButton back;
    Reciter selectedReciter;
    int selectedSoraNumber = 1;
    int selectedFrom = 1;
    int selectedTo = 1;
    int repeatAya = 1;
    int repeatSora = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qouran_learning);

        if (!SharedHelper.getBoolean(this, SharedPrefsConstants.QURAN_LEARNING_FIRST_TIME)) {
            SharedHelper.putBoolean(this, SharedPrefsConstants.QURAN_LEARNING_FIRST_TIME, true);
            CustomDialogClass.Options options = new CustomDialogClass.Options();
            options.title = "ملاحظة هامة";
            options.message = "يحتوي هذا القسم على المصحف المعلم. يمكنك اختيار اسم القارئ ثم اسم السورة والبدء الآن 💙";
            CustomDialogClass customDialogClass = new CustomDialogClass(this, options);
            customDialogClass.show();
        }

        initViews();
        setListeners();

        getListOfReciters();

        promptUserForRating();
    }

    private void setListeners() {
        back.setOnClickListener(view -> finish());
        reciterTextView.setOnClickListener(view -> {
            selectReciter();
        });

        soraTextView.setOnClickListener(view -> {
            selectSora();
        });

        fromAyaTextView.setOnClickListener(view -> {
            selectFromAya();
        });

        toAyaTextView.setOnClickListener(view -> {
            selectToAya();
        });

        repeatAyaTextView.setOnClickListener(view -> {
            selectAyaRepeat();
        });

        repeatSoraTextView.setOnClickListener(view -> {
            selectSoraRepeat();
        });

        startLearn.setOnClickListener(view -> {
            Intent intent = new Intent(this, LearningActivity.class);
            LearningData data = new LearningData(selectedReciter, soraDetails, selectedSoraNumber, selectedFrom, selectedTo, repeatAya, repeatSora);
            intent.putExtra("learning_data", data);
            startActivity(intent);
        });

        rateApp.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.appwalied.quran"))));
    }

    private void initViews() {
        back = findViewById(R.id.back_button);
        reciterTextView = findViewById(R.id.reciter_text_view);
        soraTextView = findViewById(R.id.sora_text_view);
        fromAyaTextView = findViewById(R.id.fromAyaTextView);
        toAyaTextView = findViewById(R.id.toAyaTextView);
        repeatAyaTextView = findViewById(R.id.repeatAyaTextView);
        repeatSoraTextView = findViewById(R.id.repeatSoraTextView);
        startLearn = findViewById(R.id.startLearn);
        rateApp = findViewById(R.id.rating);
        startLearnDone = findViewById(R.id.startLearnDone);
    }

    private void getListOfReciters() {
        showLoading();
        setViewsState(false, 0.5f);
        toggleLearnButtons(true);
        RetrofitClient.getInstance().getApi().getListOfReciter().enqueue(new Callback<ReciterResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReciterResponse> call, @NonNull Response<ReciterResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    logErrorResponse(response);
                    handleError("حدث خطأ أثناء تحميل البيانات. الرجاء المحاولة مرة أخرى.");
                    return;
                }

                List<Reciter> allReciters = response.body().getData();
                reciterList = filterRecitersByFormat(allReciters, "audio");

                Log.d(TAG, "Total reciters: " + allReciters.size());
                Log.d(TAG, "Filtered reciters (audio): " + reciterList.size());

                if (!reciterList.isEmpty()) {
                    updateUIWithReciter(reciterList.get(0));
                } else {
                    Log.d(TAG, "No reciters found with format = audio");
                    handleError("حدث خطأ أثناء تحميل البيانات. الرجاء المحاولة مرة أخرى.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReciterResponse> call, @NonNull Throwable t) {
                handleError("يبدو أن هناك مشكلة في الاتصال بالإنترنت. يرجى التحقق من اتصالك وحاول مرة أخرى.");
                Log.e(TAG, "API call failed: " + t.getLocalizedMessage());
            }
        });
    }



    private void setViewsState(boolean enable, float alpha) {
        soraTextView.setEnabled(enable);
        fromAyaTextView.setEnabled(enable);
        toAyaTextView.setEnabled(enable);
        soraTextView.setAlpha(alpha);
        fromAyaTextView.setAlpha(alpha);
        toAyaTextView.setAlpha(alpha);
    }

    private void toggleLearnButtons(boolean isLoading) {
        if (isLoading) {
            startLearn.setVisibility(View.GONE);
            startLearnDone.setVisibility(View.VISIBLE);
        } else {
            startLearn.setVisibility(View.VISIBLE);
            startLearnDone.setVisibility(View.GONE);
        }
    }

    /**
     * Filters the reciters list based on the given format.
     */
    private List<Reciter> filterRecitersByFormat(List<Reciter> reciters, String format) {
        List<Reciter> filteredList = new ArrayList<>();
        for (Reciter reciter : reciters) {
            if (format.equalsIgnoreCase(reciter.getFormat())) {
                filteredList.add(reciter);
            }
        }
        return filteredList;
    }

    /**
     * Logs API error response details.
     */
    private void logErrorResponse(Response<?> response) {
        Log.e(TAG, "Response Error: " + response.message());
        Log.e(TAG, "Response Code: " + response.code());
    }

    /**
     * Updates the UI with the selected reciter.
     */
    private void updateUIWithReciter(Reciter reciter) {
        reciterTextView.setText(reciter.getName());
        selectedReciter = reciter;
        getSoraDetails();
    }


    private void getSoraDetails() {
        RetrofitClient.getInstance().getApi().getSoraDetails(selectedSoraNumber, selectedReciter.getIdentifier()).enqueue(new Callback<SoraDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SoraDetailsResponse> call, @NonNull Response<SoraDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    SoraDetails data = response.body().getData();
                    soraDetails = data;

                    soraTextView.setText(data.getName());
                    int ayahCount = data.getAyahs().size();
                    toAyaTextView.setText(String.valueOf(ayahCount));
                    selectedTo = ayahCount;
                    fromAyaTextView.setText("1");

                    hideLoading();
                    setViewsState(true, 1f);
                    toggleLearnButtons(false);
                } else {
                    handleError("حدث خطأ أثناء تحميل البيانات. الرجاء المحاولة مرة أخرى.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SoraDetailsResponse> call, @NonNull Throwable t) {
                handleError("يبدو أن هناك مشكلة في الاتصال بالإنترنت. يرجى التحقق من اتصالك وحاول مرة أخرى.");
                Log.e(TAG, "API call failed: " + t.getLocalizedMessage());
            }
        });
    }

    private void handleError(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        hideLoading();
    }


    private void selectReciter() {
        if (reciterList == null) {
            getListOfReciters();
            return;
        }
        CharSequence[] items = new CharSequence[reciterList.size()];
        for (int i = 0; i <= reciterList.size() - 1; i++) {
            items[i] = reciterList.get(i).getName();
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setSingleChoiceItems(items, lastReciter, (d, n) -> {
            selectedReciter = reciterList.get(n);
            reciterTextView.setText(reciterList.get(n).getName());
            lastReciter = n;
            getSoraDetails();
            d.dismiss();
        });
        adb.setNegativeButton("الغاء", null);
        adb.setTitle("اختر القارئ");
        adb.show();
    }


    private void selectSora() {

        CharSequence[] items = new CharSequence[114];
        for (int i = 0; i <= 113; i++) {
            items[i] = soraList.get(i).getName();
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setSingleChoiceItems(items, 0, (d, n) -> {
            String name = soraList.get(n).getName();
            soraTextView.setText(name);
            selectedSoraNumber = n + 1;
            showLoading();
            toggleLearnButtons(true);
            getSoraDetails();
            d.dismiss();
        });
        adb.setNegativeButton("الغاء", null);
        adb.setTitle("اختر سورة");
        adb.show();
    }

    private void selectFromAya() {
        CharSequence[] items = new CharSequence[0];
        NumberFormat nf = NumberFormat.getInstance(new Locale("ar", "EG"));
        try {
            items = new CharSequence[soraDetails.getAyahs().size()];
            for (int i = 0; i <= soraDetails.getAyahs().size() - 1; i++) {

                items[i] = nf.format(i + 1) + " = " + soraDetails.getAyahs().get(i).getText();
            }
        } catch (Exception e) {
            Log.d(TAG, "selectFromAya: " + e.getMessage());
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setSingleChoiceItems(items, selectedFrom - 1, (d, n) -> {
            selectedFrom = n + 1;

            if (selectedTo >= selectedFrom) {
                fromAyaTextView.setText(String.valueOf(n + 1));
            } else {
                Toast.makeText(this, "يجب ان يكون الايه النهائيه بعد الايه الابتدائيه", Toast.LENGTH_LONG).show();
            }

            d.dismiss();
        });
        adb.setNegativeButton("الغاء", null);
        adb.setTitle("من الايه");
        adb.show();

    }

    private void selectToAya() {
        try {
            NumberFormat nf = NumberFormat.getInstance(new Locale("ar", "EG"));
            CharSequence[] items = new CharSequence[soraDetails.getAyahs().size()];
            for (int i = 0; i <= soraDetails.getAyahs().size() - 1; i++) {

                items[i] = nf.format(i + 1) + " = " + soraDetails.getAyahs().get(i).getText();
            }
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setSingleChoiceItems(items, selectedTo - 1, (d, n) -> {
                selectedTo = n + 1;


                if (selectedTo >= selectedFrom) {
                    toAyaTextView.setText(String.valueOf(n + 1));
                } else {
                    Toast.makeText(this, "يجب ان يكون الايه النهائيه بعد الايه الابتدائيه", Toast.LENGTH_LONG).show();
                }
                d.dismiss();
            });
            adb.setNegativeButton("الغاء", null);
            adb.setTitle("الى الايه");
            adb.show();

        } catch (Exception e) {
            Log.d(TAG, "selectToAya: " + e.getMessage());
        }
    }

    @SuppressLint("SetTextI18n")
    private void selectAyaRepeat() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setGravity(Gravity.CENTER);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);

        edittext.setHint("ادخل رقم التكرار");
        alert.setTitle("عدد تكرار الآيه الواحده");

        alert.setView(edittext);

        alert.setPositiveButton("تم", (dialog, whichButton) -> {
            String textValue = edittext.getText().toString();
            if (textValue.isEmpty() || textValue.equals("0") || Integer.parseInt(textValue) > 604) {
                Toast.makeText(this, "برجاء اختيار رقم صفحة صالح", Toast.LENGTH_SHORT).show();
            } else {
                repeatAya = Integer.parseInt(textValue);
                repeatAyaTextView.setText("عدد التكرار لكل آيه" + " = " + textValue);
            }
        });

        alert.setNegativeButton("الغاء", (dialog, whichButton) -> {
            dialog.dismiss();
            // what ever you want to do with No option.
        });

        alert.show();
    }

    @SuppressLint("SetTextI18n")
    private void selectSoraRepeat() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        edittext.setGravity(Gravity.CENTER);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);

        edittext.setHint("ادخل رقم التكرار");
        alert.setTitle("عدد تكرار السورة");

        alert.setView(edittext);

        alert.setPositiveButton("تم", (dialog, whichButton) -> {
            String textValue = edittext.getText().toString();
            if (textValue.isEmpty() || textValue.equals("0") || Integer.parseInt(textValue) > 604) {
                Toast.makeText(this, "برجاء اختيار رقم صفحة صالح", Toast.LENGTH_SHORT).show();
            } else {
                repeatSora = Integer.parseInt(textValue);
                repeatSoraTextView.setText("عدد التكرار " + " = " + textValue);
            }
        });

        alert.setNegativeButton("الغاء", (dialog, whichButton) -> {
            dialog.dismiss();
            // what ever you want to do with No option.
        });

        alert.show();
    }


}