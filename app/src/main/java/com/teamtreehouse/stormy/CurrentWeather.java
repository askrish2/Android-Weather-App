package com.teamtreehouse.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ashkrishnan on 6/21/16.
 */
public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemp;
    private double mHum;
    private double mPrep;
    private String mSum;
    private String mTimeZone;
    //private String mType;
    private double mActual;
    //private double mWind;
    /**
    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
    **/
    public int getmActual() {
        return (int) Math.round(mActual);
    }

    public void setmActual(double mActual) {
        this.mActual = mActual;
    }

    /**
    public double getmWind() {
        return mWind;
    }

    public void setmWind(double mWind) {
        this.mWind = mWind;
    }
    **/
    public String getmTimeZone() {
        return mTimeZone;
    }

    public void setmTimeZone(String mTimeZone) {
        this.mTimeZone = mTimeZone;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public int getIconId() {
       return Forecast.getIconId(mIcon);
    }
    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public String getFormattedTime() {
        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        format.setTimeZone(TimeZone.getTimeZone(getmTimeZone()));
        Date date = new Date(getmTime()*1000);
        String timeString = format.format(date);
        return timeString;
    }
    public int getmTemp() {
        return (int) Math.round(mTemp);
    }

    public void setmTemp(double mTemp) {
        this.mTemp = mTemp;
    }

    public double getmHum() {
        return mHum;
    }

    public void setmHum(double mHum) {
        this.mHum = mHum;
    }

    public int getmPrep() {
        return (int) Math.round(mPrep*100);
    }

    public void setmPrep(double mPrep) {
        this.mPrep = mPrep;
    }

    public String getmSum() {
        return mSum;
    }

    public void setmSum(String mSum) {
        this.mSum = mSum;
    }
}
