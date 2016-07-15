package com.gorobaby.jh.phonebook.Model;


public class Person {
    private int mId;
    private String mPicture;
    private String mName;
    private String mPhoneNumber;
    private boolean mSelected;

    public Person(int id, String picture, String name, String mPhoneNumber, boolean selected) {
        this.mId = id;
        this.mName = name;
        this.mPicture = picture;
        this.mPhoneNumber = mPhoneNumber;
        this.mSelected = selected;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String name) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getPicture() { return mPicture; }

    public void setPicture(String picture) {
        this.mPicture = picture;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }

}

