package com.gulcusoftware.fratdevtakip;


import retrofit2.Call;

import retrofit2.http.GET;

import java.util.List;

public interface DuyuruService {
    @GET("crowroser/FiratOdevTakip/main/Duyuru/duyuru")
    Call<List<Duyuru>> getDuyurular();
}

