package com.appwalied.quran.quran.qouran_learning;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.appwalied.quran.R;
import com.appwalied.quran.quran.qouran_learning.models.Ayah;
import com.appwalied.quran.quran.qouran_learning.models.LearningData;

import java.util.ArrayList;
import java.util.List;

public class LearningActivity extends AppCompatActivity implements AyasAdapter.Interaction, JcPlayerManagerListener {

    private AyasAdapter ayasAdapter;
    private LearningData learningData;
    private TextView reciterName;
    private RecyclerView ayasRecyclerView;
    private JcPlayerView player;
    private final List<Ayah> selectedAyasList = new ArrayList<>();

    private int currentPlayIndex = 0;
    private int currentRepeatAyaIndex = 1;
    private int currentRepeatSoraIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        getIntentData();
        initViews();

    }

    private void getIntentData() {

        learningData = (LearningData) getIntent().getSerializableExtra("learning_data");
        List<Ayah> selectedAyasList = new ArrayList<>();
        for (int i=0; i < learningData.getSoraDetails().getAyahs().size(); i++){
            if(i >= (learningData.getSelectedFrom()-1) && i <= (learningData.getSelectedTo()-1))
                selectedAyasList.add(learningData.getSoraDetails().getAyahs().get(i));
        }
        learningData.getSoraDetails().setAyahs(selectedAyasList);
    }

    private void initViews() {
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
        reciterName = findViewById(R.id.reciterName);
        ayasRecyclerView = findViewById(R.id.ayasRecyclerView);
        player = findViewById(R.id.jcplayer);
        ayasAdapter = new AyasAdapter(learningData.getSoraDetails().getAyahs(), this);
        ayasRecyclerView.setAdapter(ayasAdapter);
        ayasRecyclerView.setLayoutManager(new LinearLayoutManager((this)));
        reciterName.setText(learningData.getSelectedReciter().getName());



        ArrayList<JcAudio> jcAudios = new ArrayList<>();
        jcAudios.add(getNextAudio(0));
        player.initPlaylist(jcAudios, this);

        player.findViewById(R.id.btnNext).setOnClickListener(view -> {
            if (currentPlayIndex == learningData.getSoraDetails().getAyahs().size() - 1) {
                currentPlayIndex = 0;
                currentRepeatAyaIndex = 1;
                player.playAudio(getNextAudio(0));
                ayasRecyclerView.smoothScrollToPosition(0);

            } else {
                currentPlayIndex += 1;
                currentRepeatAyaIndex = 1;
                player.playAudio(getNextAudio(currentPlayIndex));
                ayasRecyclerView.smoothScrollToPosition(currentPlayIndex);
            }

            ayasAdapter.setSelectedAyaIndex(currentPlayIndex);
        });

        player.findViewById(R.id.btnPrev).setOnClickListener(view -> {

            if (currentPlayIndex == 0) {
                currentRepeatAyaIndex = 1;
                player.playAudio(getNextAudio(0));
                ayasRecyclerView.smoothScrollToPosition(0);
            } else {
                currentPlayIndex -= 1;
                currentRepeatAyaIndex = 1;
                player.playAudio(getNextAudio(currentPlayIndex));
                ayasRecyclerView.smoothScrollToPosition(currentPlayIndex);
            }
            ayasAdapter.setSelectedAyaIndex(currentPlayIndex);

        });


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> runOnUiThread(() -> {
            player.playAudio(getNextAudio(currentPlayIndex));
        }), 500);

    }

    private JcAudio getNextAudio(int index) {
        return JcAudio.createFromURL(learningData.getSoraDetails().getAyahs().get(index).getText(), learningData.getSoraDetails().getAyahs().get(index).getAudio());
    }


    @Override
    public void onAyaClicked(Ayah ayah, int position) {
        currentRepeatAyaIndex = 1;
        currentPlayIndex = position;
        player.playAudio(getNextAudio(position));

    }

    @Override
    public void onCompletedAudio() {

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> runOnUiThread(() -> {
            if (currentRepeatAyaIndex < learningData.getRepeatAya()) {
                currentRepeatAyaIndex += 1;
            } else {
                currentPlayIndex += 1;
                currentRepeatAyaIndex = 1;
            }

            if (currentPlayIndex < learningData.getSoraDetails().getAyahs().size()) {
                player.playAudio(getNextAudio(currentPlayIndex));
                ayasRecyclerView.smoothScrollToPosition(currentPlayIndex);
                ayasAdapter.setSelectedAyaIndex(currentPlayIndex);
            } else {
                currentPlayIndex = 0;
                ayasAdapter.setSelectedAyaIndex(0);

                if (currentRepeatSoraIndex < learningData.getRepeatSora()) {
                    currentRepeatSoraIndex += 1;
                    ayasAdapter.setSelectedAyaIndex(currentPlayIndex);
                    player.playAudio(getNextAudio(currentPlayIndex));
                    ayasRecyclerView.smoothScrollToPosition(currentPlayIndex);
                } else {
                    finish();
                }

            }

        }), 200);
    }

    @Override
    public void onContinueAudio(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onJcpError(@NonNull Throwable throwable) {

    }

    @Override
    public void onPaused(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onPlaying(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onPreparedAudio(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onStopped(@NonNull JcStatus jcStatus) {

    }

    @Override
    public void onTimeChanged(@NonNull JcStatus jcStatus) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.kill();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        player.createNotification();
    }
}