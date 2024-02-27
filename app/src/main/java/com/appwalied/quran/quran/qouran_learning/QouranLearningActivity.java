package com.appwalied.quran.quran.qouran_learning;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.appwalied.quran.listing.Constants;
import com.appwalied.quran.R;
import com.appwalied.quran.features.SharedHelper;
import com.appwalied.quran.features.views.CustomDialogClass;
import com.appwalied.quran.quran.qouran_learning.models.LearningData;
import com.appwalied.quran.quran.qouran_learning.models.Reciter;
import com.appwalied.quran.quran.qouran_learning.models.ReciterResponse;
import com.appwalied.quran.quran.qouran_learning.models.Sora;
import com.appwalied.quran.quran.qouran_learning.models.SoraDetails;
import com.appwalied.quran.quran.qouran_learning.models.SoraDetailsResponse;
import com.appwalied.quran.quran.qouran_learning.networking.QuranUtils;
import com.appwalied.quran.quran.qouran_learning.networking.RetrofitClient;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QouranLearningActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final List<Sora> soraList = QuranUtils.getSoraList();
    List<Reciter> reciterList;
    SoraDetails soraDetails;
    TextView reciterTextView, soraTextView, startLearn, fromAyaTextView, repeatSoraTextView, repeatAyaTextView, toAyaTextView;
    int lastReciter = 0;

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

        if (!SharedHelper.getBoolean(this, Constants.QOURAN_FIRST_TIME)) {
            SharedHelper.putBoolean(this, Constants.QOURAN_FIRST_TIME, true);
            CustomDialogClass.Options options = new CustomDialogClass.Options();
            options.title = "ملاحظة هامة ";
            options.message = "يحتوي هذا القسم على المصحف المعلم يمكنك اختيار اسم القارئ ثم اسم السورة وابدأ الآن 💙";
            CustomDialogClass customDialogClass = new CustomDialogClass(this, options);
            customDialogClass.show();
        }

        initViews();
        setListeners();
        getListOfReciters();
    }

    private void setListeners() {
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
            LearningData data = new LearningData(
                    selectedReciter,
                    soraDetails,
                    selectedSoraNumber ,
                    selectedFrom ,
                    selectedTo ,
                    repeatAya ,
                    repeatSora
            );
            intent.putExtra("learning_data",data );
            startActivity(intent);
        });
    }


    private void initViews() {
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
        reciterTextView = findViewById(R.id.reciter_text_view);
        soraTextView = findViewById(R.id.sora_text_view);
        fromAyaTextView = findViewById(R.id.fromAyaTextView);
        toAyaTextView = findViewById(R.id.toAyaTextView);
        repeatAyaTextView = findViewById(R.id.repeatAyaTextView);
        repeatSoraTextView = findViewById(R.id.repeatSoraTextView);
        startLearn = findViewById(R.id.startLearn);
    }

    private void getListOfReciters() {

        RetrofitClient.getInstance().getApi().getListOfReciter().enqueue(new Callback<ReciterResponse>() {
            @Override
            public void onResponse(Call<ReciterResponse> call, Response<ReciterResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, ": size = " + response.body().getData().size());
                    reciterList = response.body().getData();
                    reciterTextView.setText(reciterList.get(0).getName());
                    selectedReciter = reciterList.get(0);
                    getSoraDetails();
                } else {
                    Log.d(TAG, ": " + response.message());
                    Log.d(TAG, ": " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ReciterResponse> call, Throwable t) {
                Log.d(TAG, ": " + t.getLocalizedMessage());
            }
        });

    }

    private void getSoraDetails() {
        startLearn.setVisibility(View.GONE);
        RetrofitClient.getInstance().getApi().getSoraDetails(selectedSoraNumber, selectedReciter.getIdentifier())
                .enqueue(new Callback<SoraDetailsResponse>() {
                    @Override
                    public void onResponse(Call<SoraDetailsResponse> call, Response<SoraDetailsResponse> response) {
                        if (response.isSuccessful()) {
                            soraDetails = response.body().getData();
                            soraTextView.setText(soraDetails.getName());
                            toAyaTextView.setText(String.valueOf(soraDetails.getAyahs().size()));
                            selectedTo = soraDetails.getAyahs().size();
                            fromAyaTextView.setText("1");
                            startLearn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<SoraDetailsResponse> call, Throwable t) {
                    }
                });
    }

    private void selectReciter() {
        if (reciterList == null) return;
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
            Log.d(TAG, "onClick: " + 2);
            String name = soraList.get(n).getName();
            soraTextView.setText(name);
            selectedSoraNumber = n+1;
            getSoraDetails();
            d.dismiss();
        });
        adb.setNegativeButton("الغاء", null);
        adb.setTitle("اختر سورة");
        adb.show();

    }

    private void selectFromAya() {
        NumberFormat nf = NumberFormat.getInstance(new Locale("ar", "EG"));
        CharSequence[] items = new CharSequence[soraDetails.getAyahs().size()];
        for (int i = 0; i <= soraDetails.getAyahs().size() - 1; i++) {

            items[i] = nf.format(i + 1) + " = " + soraDetails.getAyahs().get(i).getText();
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

    }

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
            if (textValue.equals("") || textValue.equals("0") || Integer.parseInt(textValue) > 604) {
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
            if (textValue.equals("") || textValue.equals("0") || Integer.parseInt(textValue) > 604) {
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