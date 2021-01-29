package com.example.ternavest.testing.notification.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Kelas ini menangani pengiriman notifikasi (dipanggil tiap kali mengirim notifikasi)
public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
