package com.appwalied.quran.quran_listening.api;

import com.appwalied.quran.quran_listening.model.ReciterResponse;
import com.appwalied.quran.quran_listening.model.SuwarResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Mp3QuranApiService {

    @GET("reciters")
    Call<ReciterResponse> getReciters(@Query("language") String language);

    @GET("suwar")
    Call<SuwarResponse> getSuwar(@Query("language") String language);
}
