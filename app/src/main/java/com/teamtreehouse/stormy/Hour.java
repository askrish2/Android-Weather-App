package com.teamtreehouse.stormy;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ashkrishnan on 6/25/16.
 */
public class Hour implements Parcelable{
    private long Time;
    private String Sum;
    private double Temp;
    private String Icon;
    private String Zone;

    public Hour() {

    }
    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getSum() {
        return Sum;
    }

    public void setSum(String sum) {
        Sum = sum;
    }

    public int getTemp() {
        return (int) Math.round(Temp);
    }

    public void setTemp(double temp) {
        Temp = temp;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getHour() {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        Date date = new Date(Time *1000);
        return formatter.format(date);
    }
    public int getIconId() {
        return Forecast.getIconId(Icon);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(Time);
        dest.writeString(Sum);
        dest.writeDouble(Temp);
        dest.writeString(Zone);
        dest.writeString(Icon);
    }

    private Hour(Parcel in) {
        Time = in.readLong();
        Sum = in.readString();
        Temp = in.readDouble();
        Zone = in.readString();
        Icon = in.readString();
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
