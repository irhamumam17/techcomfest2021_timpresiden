package com.example.ternavest.testing.notification.model;

// Representasi dari isi notifikasi
public class Notification {
    private String title;
    private String message;

    public Notification() {}

    public Notification(String title, String message) {
        this.message = message;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
