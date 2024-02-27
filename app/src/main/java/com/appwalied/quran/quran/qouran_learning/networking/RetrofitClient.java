package com.appwalied.quran.quran.qouran_learning.networking;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = "https://api.alquran.cloud/";
    public static Dispatcher dispatcher = new Dispatcher();
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    public static  Context context;

    private RetrofitClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES);
        httpClientBuilder.dispatcher(dispatcher);
        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientBuilder.build())
                .build();
    }
    public static synchronized RetrofitClient getInstance() {

        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static Dispatcher getDispatcher() {
        return dispatcher;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }


}
