package com.example.ternavest.testing.notification.rest;

import com.example.ternavest.testing.notification.response.MyResponse;
import com.example.ternavest.testing.notification.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAhIdklc8:APA91bFMmZNRsua6dBqAQoHRldIpbnQXAD4YtdCy1W9eqZukkdXcEZ5Mak-oipbrtdPpT7WVOaFsdlL-qOBCpv4ZIz5qspdRbtHFxEraR0egdPBNc5A8CmTv_MGv4ECOW5PHYuw1M0ot"
            }
    )

    @POST("fcm/send")
    // Fungsi ini dipanggil ketika akan mengirim notifikasi (isi notifikasi dan token dibungkus dalam kelas Sender)
    Call<MyResponse> sendNotification(@Body Sender body);
}