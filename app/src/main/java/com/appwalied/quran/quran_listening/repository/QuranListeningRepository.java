package com.appwalied.quran.quran_listening.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.appwalied.quran.quran_listening.api.Mp3QuranApiClient;
import com.appwalied.quran.quran_listening.api.Mp3QuranApiService;
import com.appwalied.quran.quran_listening.helper.ListeningPreferences;
import com.appwalied.quran.quran_listening.model.ReciterModel;
import com.appwalied.quran.quran_listening.model.ReciterResponse;
import com.appwalied.quran.quran_listening.model.SurahModel;
import com.appwalied.quran.quran_listening.model.SuwarResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class QuranListeningRepository {

    private final Mp3QuranApiService apiService;
    private final ListeningPreferences preferences;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private final Gson gson;

    public interface OnRecitersFetchedListener {
        void onSuccess(List<ReciterModel> reciters, boolean isFromCache);
        void onFailure(String errorMessage);
    }

    public interface OnSuwarFetchedListener {
        void onSuccess(List<SurahModel> suwar, boolean isFromCache);
        void onFailure(String errorMessage);
    }

    public QuranListeningRepository(Context context) {
        apiService = Mp3QuranApiClient.getApiService();
        preferences = new ListeningPreferences(context);
        executor = Executors.newFixedThreadPool(2);
        mainHandler = new Handler(Looper.getMainLooper());
        gson = new Gson();
    }

    public void fetchReciters(OnRecitersFetchedListener listener) {
        executor.execute(() -> {
            try {
                Call<ReciterResponse> call = apiService.getReciters("ar");
                Response<ReciterResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<ReciterModel> reciters = response.body().getReciters();
                    String json = gson.toJson(response.body());
                    preferences.cacheRecitersJson(json);

                    mainHandler.post(() -> listener.onSuccess(reciters, false));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fallback to cache
            String cachedJson = preferences.getCachedRecitersJson();
            if (cachedJson != null && !cachedJson.isEmpty()) {
                try {
                    ReciterResponse response = gson.fromJson(cachedJson, ReciterResponse.class);
                    if (response != null && response.getReciters() != null) {
                        List<ReciterModel> cachedList = response.getReciters();
                        mainHandler.post(() -> listener.onSuccess(cachedList, true));
                        return;
                    }
                } catch (Exception ignored) {}
            }

            mainHandler.post(() -> listener.onFailure("تعذر جلب قائمة القراء، يرجى التأكد من الاتصال بالإنترنت"));
        });
    }

    public void fetchSuwar(OnSuwarFetchedListener listener) {
        executor.execute(() -> {
            try {
                Call<SuwarResponse> call = apiService.getSuwar("ar");
                Response<SuwarResponse> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<SurahModel> suwar = response.body().getSuwar();
                    String json = gson.toJson(response.body());
                    preferences.cacheSuwarJson(json);

                    mainHandler.post(() -> listener.onSuccess(suwar, false));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fallback to cache
            String cachedJson = preferences.getCachedSuwarJson();
            if (cachedJson != null && !cachedJson.isEmpty()) {
                try {
                    SuwarResponse response = gson.fromJson(cachedJson, SuwarResponse.class);
                    if (response != null && response.getSuwar() != null) {
                        List<SurahModel> cachedList = response.getSuwar();
                        mainHandler.post(() -> listener.onSuccess(cachedList, true));
                        return;
                    }
                } catch (Exception ignored) {}
            }

            mainHandler.post(() -> listener.onFailure("تعذر جلب قائمة السور، يرجى التأكد من الاتصال بالإنترنت"));
        });
    }
}
