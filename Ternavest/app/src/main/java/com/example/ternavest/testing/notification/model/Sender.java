package com.example.ternavest.testing.notification.model;

// Membawa isi notifikasi dan token si penerima
public class Sender {
    public Notification notification;
    public String receiverToken;

    public Sender(Notification notification, String receiverToken) {
        this.notification = notification;
        this.receiverToken = receiverToken;
    }
}
