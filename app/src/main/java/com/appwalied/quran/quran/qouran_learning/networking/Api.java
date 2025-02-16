package com.appwalied.quran.quran.qouran_learning.networking;





import com.appwalied.quran.quran.qouran_learning.models.ReciterResponse;
import com.appwalied.quran.quran.qouran_learning.models.SoraDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("v1/edition?language=ar")
    Call<ReciterResponse> getListOfReciter();

    @GET("v1/surah/{soraId}}/{identifier}")
    Call<SoraDetailsResponse> getSoraDetails(@Path(value = "soraId", encoded = true) Integer soraId,
                                             @Path(value ="identifier", encoded = true) String identifier);

}

