package com.example.myapplication;

public final class MainPresenter {

    private static MainPresenter instance = null;

    private static final Object syncObj = new Object();

    private String mail;
    private int num = 0;

    public static MainPresenter getInstance() {
        synchronized (syncObj) {
            if (instance == null) {
                instance = new MainPresenter();
            }
            return instance;
        }
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}