package com.example.yourscheduler;

public class ModelConstructors {
    private String title, note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public ModelConstructors(String title, String note) {
        this.title = title;
        this.note = note;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
