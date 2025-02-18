package com.appwalied.quran.quran.quran_listening.listening;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.google.firebase.database.annotations.NotNull;

public class ListeningBroadCast extends BroadcastReceiver {
    public static final String PLAY_PAUSE_CLICK = "com.ymg.newappquqapsaada.PLAY_CLICK";

    public static final String FORWARD_CLICK = "com.ymg.newappquqapsaada.FORWARD_CLICK";

    public static final String BACKWARD_CLICK = "com.ymg.newappquqapsaada.BACKWARD_CLICK";

    public static final String OUTSIDE_SHOULD_STOP_EVENT = "com.ymg.newappquqapsaada.SHOULD_STOP";

    public static final String STOP_CLICK = "com.ymg.newappquqapsaada.STOP_CLICK";

    public static final String MEDIA_FINISHED = "com.ymg.newappquqapsaada.MEDIA_STOPPED";

    public static final String TEN_FORWARD_CLICK = "com.ymg.newappquqapsaada.TEN_FORWARD_CLICK";

    public static final String TEN_BACKWARD_CLICK = "com.ymg.newappquqapsaada.TEN_BACKWARD_CLICK";

    public static final String SEEKING_TO = "com.ymg.newappquqapsaada.listening.SEEKING_TO";

    public static final String SEEKING_TO_KEY = "com.ymg.newappquqapsaada.listening.SEEKING_TO_KEY";

    private String eventStatus;

    public void onReceive(Context context, @NotNull Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "com.ymg.newappquqapsaada.PLAY_CLICK":
                    this.eventStatus = "com.ymg.newappquqapsaada.PLAY_CLICK";
                    return;
                case "com.ymg.newappquqapsaada.FORWARD_CLICK":
                    this.eventStatus = "com.ymg.newappquqapsaada.FORWARD_CLICK";
                    return;
                case "com.ymg.newappquqapsaada.BACKWARD_CLICK":
                    this.eventStatus = "com.ymg.newappquqapsaada.BACKWARD_CLICK";
                    return;
                case "com.ymg.newappquqapsaada.STOP_CLICK":
                    this.eventStatus = "com.ymg.newappquqapsaada.STOP_CLICK";
                    return;
                case "com.ymg.newappquqapsaada.MEDIA_STOPPED":
                    this.eventStatus = "com.ymg.newappquqapsaada.MEDIA_STOPPED";
                    return;
                case "android.intent.action.NEW_OUTGOING_CALL":
                    this.eventStatus = "com.ymg.newappquqapsaada.SHOULD_STOP";
                    return;
            }
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
            switch (telephonyManager.getCallState()) {
                case 1:
                case 2:
                    this.eventStatus = "com.ymg.newappquqapsaada.SHOULD_STOP";
                    break;
                case 0:
                    this.eventStatus = "com.ymg.newappquqapsaada.PLAY_CLICK";
                    break;
            }
        }
    }

    public String getEventStatus() {
        return this.eventStatus;
    }
}
