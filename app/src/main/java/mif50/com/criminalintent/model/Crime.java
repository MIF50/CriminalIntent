package mif50.com.criminalintent.model;

import java.util.Date;
import java.util.UUID;


public class Crime {

    private UUID mID;
    private String mTitle;
    private Date mData;
    private boolean mSolved;
    private String mSuspect;
    private String number;


    public Crime() {
        this(UUID.randomUUID());

    }

    public Crime(UUID id) {
        this.mID = id;
        this.mData = new Date();
    }

    public Crime(String mTitle, Date mData, boolean mSolved) {
        this();
        this.setmTitle(mTitle);
        this.setmData(mData);
        this.setmSolved(mSolved);
    }

    public UUID getmID() {
        return mID;
    }

    public void setMID() {
        this.mID = UUID.randomUUID();
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmData() {
        return mData;
    }

    public void setmData(Date mData) {
        this.mData = mData;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhotoFileName() {
        return "IMG_" + getmID().toString() + ".jpg";
    }
}
