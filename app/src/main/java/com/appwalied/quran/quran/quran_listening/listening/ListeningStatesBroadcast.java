package com.appwalied.quran.quran.quran_listening.listening;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.annotations.NotNull;


public class ListeningStatesBroadcast extends BroadcastReceiver {
    public static final String TAG = "ListeningStates";

    public static final String STATE_PAUSED = "com.ymg.qoran.listening.StatePaused";

    public static final String STATE_PLAYING = "com.ymg.qoran.listening.Playing";

    public static final String STATE_STOPPED = "com.ymg.qoran.listening.StateStopped";

    public static final String STATE_READY = "com.ymg.qoran.listening.StateReading";

    public static final String CURRENT_TIME = "com.ymg.qoran.listening.CURRENT_TIME";

    public static final String CURRENT_TIME_VALUE_KEY = "com.ymg.qoran.listening.CURRENT_TIME_KEY";

    public static final String MEDIA_DURATION_TIME = "com.ymg.qoran.listening.DURATION_TIME";

    public static final String MEDIA_DURATION_TIME_KEY = "com.ymg.qoran.listening.DURATION_TIME_KEY";

    public static final String CURRENT_SOURAH_NAME_KEY = "com.ymg.qoran.listening.CURRENT_SOURAH_NAME";

    public static final String NEW_SOURAH_NAME = "com.ymg.qoran.listening.NEW_SOURAH_NAME";

    public static final String NEW_SOURAH_NAME_KEY = "com.ymg.qoran.listening.NEW_SOURAH_NAME_KEY";

    public void onReceive(Context context, @NotNull Intent intent) {
        if (intent.getAction() != null)
            Log.d("ListeningStates", "onReceive: => ".concat(intent.getAction()));
    }
}
