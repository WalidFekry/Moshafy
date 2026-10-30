package com.appwalied.quran.quran.audio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReciterRepository {

    private static final String PREF_NAME = "everyayah_reciters_pref";
    private static final String KEY_CACHED_JSON = "cached_recitations_json";
    private static final String RECITATIONS_URL = "https://everyayah.com/data/recitations.js";

    private static volatile ReciterRepository instance;
    private final Context context;
    private final ExecutorService executor;
    private final Handler mainHandler;
    private final OkHttpClient okHttpClient;

    public interface Callback<T> {
        void onResult(T result);
        void onError(String message);
    }

    private ReciterRepository(Context context) {
        this.context = context.getApplicationContext();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.okHttpClient = new OkHttpClient();
    }

    public static ReciterRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (ReciterRepository.class) {
                if (instance == null) {
                    instance = new ReciterRepository(context);
                }
            }
        }
        return instance;
    }

    public void getReciters(boolean forceRefresh, Callback<List<Reciter>> callback) {
        executor.execute(() -> {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String cachedJson = prefs.getString(KEY_CACHED_JSON, null);

            if (!forceRefresh && cachedJson != null && !cachedJson.trim().isEmpty()) {
                List<Reciter> cachedList = parseRecitationsJson(cachedJson);
                if (!cachedList.isEmpty()) {
                    mainHandler.post(() -> callback.onResult(cachedList));
                    return;
                }
            }

            // Fetch from network
            try {
                Request request = new Request.Builder()
                        .url(RECITATIONS_URL)
                        .header("User-Agent", "Mozilla/5.0")
                        .build();

                try (Response response = okHttpClient.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonString = response.body().string();
                        List<Reciter> reciters = parseRecitationsJson(jsonString);
                        if (!reciters.isEmpty()) {
                            prefs.edit().putString(KEY_CACHED_JSON, jsonString).apply();
                            mainHandler.post(() -> callback.onResult(reciters));
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                // Ignore and fallback
            }

            // Fallback to cache if available
            if (cachedJson != null && !cachedJson.trim().isEmpty()) {
                List<Reciter> cachedList = parseRecitationsJson(cachedJson);
                if (!cachedList.isEmpty()) {
                    mainHandler.post(() -> callback.onResult(cachedList));
                    return;
                }
            }

            // Final fallback: Built-in default reciters
            List<Reciter> defaultList = getDefaultReciters();
            if (!defaultList.isEmpty()) {
                mainHandler.post(() -> callback.onResult(defaultList));
            } else {
                mainHandler.post(() -> callback.onError("تعذر تحميل قائمة القراء. يرجى الاتصال بالإنترنت والمحاولة مجددًا."));
            }
        });
    }

    private List<Reciter> parseRecitationsJson(String jsonStr) {
        List<Reciter> reciters = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonStr);
            Iterator<String> keys = root.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if ("ayahCount".equals(key)) continue;

                JSONObject obj = root.optJSONObject(key);
                if (obj != null) {
                    String subfolder = obj.optString("subfolder", "");
                    String name = obj.optString("name", "");
                    String bitrate = obj.optString("bitrate", "");

                    if (!subfolder.isEmpty()) {
                        String arName = mapArabicReciterName(name, subfolder);
                        reciters.add(new Reciter(key, name, arName, subfolder, bitrate, false));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reciters;
    }

    private String mapArabicReciterName(String englishName, String subfolder) {
        String lower = (englishName + " " + subfolder).toLowerCase();
        if (lower.contains("alafasy") || lower.contains("afasy")) return "مشاري راشد العفاسي";
        if (lower.contains("abdul_basit") || lower.contains("abdulbasit") || lower.contains("samad")) {
            if (lower.contains("mujawwad")) return "عبد الباسط عبد الصمد (مجود)";
            return "عبد الباسط عبد الصمد (مرتل)";
        }
        if (lower.contains("husary")) {
            if (lower.contains("mujawwad")) return "محمود خليل الحصري (مجود)";
            if (lower.contains("muallim")) return "محمود خليل الحصري (معلم)";
            return "محمود خليل الحصري (مرتل)";
        }
        if (lower.contains("minshawy") || lower.contains("menshawi")) {
            if (lower.contains("mujawwad")) return "محمد صديق المنشاوي (مجود)";
            return "محمد صديق المنشاوي (مرتل)";
        }
        if (lower.contains("maher") || lower.contains("muaiqly")) return "ماهر المعيقلي";
        if (lower.contains("sudais")) return "عبد الرحمن السديس";
        if (lower.contains("shuraym") || lower.contains("shuraim")) return "سعود الشريم";
        if (lower.contains("shaatree") || lower.contains("shatri")) return "أبو بكر الشاطري";
        if (lower.contains("ghamadi")) return "سعد الغامدي";
        if (lower.contains("ajamy") || lower.contains("ajmi")) return "أحمد بن علي العجمي";
        if (lower.contains("rifai")) return "هاني الرفاعي";
        if (lower.contains("hudhaify")) return "علي الحذيفي";
        if (lower.contains("tablaway")) return "محمد الطبلاوي";
        if (lower.contains("ayyoub") || lower.contains("ayoub")) return "محمد أيوب";
        if (lower.contains("jibreel")) return "محمود جبريل";
        if (lower.contains("dussary") || lower.contains("dossari")) return "ياسر الدوسري";
        if (lower.contains("qatami")) return "ناصر القطامي";
        if (lower.contains("bukhatir")) return "صلاح بو خاطر";
        if (lower.contains("juhaynee")) return "عبد الله الجهني";
        if (lower.contains("budair")) return "صلاح البدير";
        if (lower.contains("matroud")) return "عبد الله مطرود";
        if (lower.contains("tunaiji")) return "خليفة التنوجي";
        if (lower.contains("banna")) return "محمود علي البنا";
        if (lower.contains("fares")) return "فارس عباد";
        if (lower.contains("ali_jaber") || lower.contains("ali jaber")) return "علي جابر";
        if (lower.contains("sowaid")) return "أيمن سويد";
        if (lower.contains("basfar")) return "عبد الله بصفر";
        if (lower.contains("akhdar")) return "إبراهيم الأخضر";

        return englishName;
    }

    public List<Reciter> getDefaultReciters() {
        List<Reciter> list = new ArrayList<>();
        list.add(new Reciter("15", "Alafasy", "مشاري راشد العفاسي", "Alafasy_128kbps", "128kbps", false));
        list.add(new Reciter("2", "Abdul Basit Murattal", "عبد الباسط عبد الصمد (مرتل)", "Abdul_Basit_Murattal_192kbps", "192kbps", false));
        list.add(new Reciter("3", "Abdul Basit Mujawwad", "عبد الباسط عبد الصمد (مجود)", "Abdul_Basit_Mujawwad_128kbps", "128kbps", false));
        list.add(new Reciter("20", "Husary", "محمود خليل الحصري (مرتل)", "Husary_128kbps", "128kbps", false));
        list.add(new Reciter("34", "Minshawy Murattal", "محمد صديق المنشاوي (مرتل)", "Minshawy_Murattal_128kbps", "128kbps", false));
        list.add(new Reciter("33", "Minshawy Mujawwad", "محمد صديق المنشاوي (مجود)", "Minshawy_Mujawwad_192kbps", "192kbps", false));
        list.add(new Reciter("29", "Maher Al Muaiqly", "ماهر المعيقلي", "MaherAlMuaiqly128kbps", "128kbps", false));
        list.add(new Reciter("8", "Abdurrahmaan As-Sudais", "عبد الرحمن السديس", "Abdurrahmaan_As-Sudais_192kbps", "192kbps", false));
        list.add(new Reciter("44", "Saood bin Ibraaheem Ash-Shuraym", "سعود الشريم", "Saood_ash-Shuraym_128kbps", "128kbps", false));
        list.add(new Reciter("11", "Abu Bakr Ash-Shaatree", "أبو بكر الشاطري", "Abu_Bakr_Ash-Shaatree_128kbps", "128kbps", false));
        list.add(new Reciter("16", "Ghamadi", "سعد الغامدي", "Ghamadi_40kbps", "40kbps", false));
        list.add(new Reciter("13", "Ahmed ibn Ali al-Ajamy", "أحمد بن علي العجمي", "Ahmed_ibn_Ali_al-Ajamy_128kbps_ketaballah.net", "128kbps", false));
        list.add(new Reciter("18", "Hani Rifai", "هاني الرفاعي", "Hani_Rifai_192kbps", "192kbps", false));
        list.add(new Reciter("25", "Hudhaify", "علي الحذيفي", "Hudhaify_128kbps", "128kbps", false));
        list.add(new Reciter("36", "Mohammad al Tablaway", "محمد الطبلاوي", "Mohammad_al_Tablaway_128kbps", "128kbps", false));
        list.add(new Reciter("37", "Muhammad Ayyoub", "محمد أيوب", "Muhammad_Ayyoub_128kbps", "128kbps", false));
        list.add(new Reciter("41", "Muhammad Jibreel", "محمود جبريل", "Muhammad_Jibreel_128kbps", "128kbps", false));
        list.add(new Reciter("67", "Yasser_Ad-Dussary", "ياسر الدوسري", "Yasser_Ad-Dussary_128kbps", "128kbps", false));
        list.add(new Reciter("68", "Nasser_Alqatami", "ناصر القطامي", "Nasser_Alqatami_128kbps", "128kbps", false));
        list.add(new Reciter("77", "Fares Abbad", "فارس عباد", "Fares_Abbad_64kbps", "64kbps", false));
        return list;
    }
}
