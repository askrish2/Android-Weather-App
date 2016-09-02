package com.teamtreehouse.stormy;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ashkrishnan on 6/25/16.
 */
public class Day implements Parcelable{
    private long Time;
    private String Sum;
    private double maxTemp;
    private double minTemp;
    private String Icon;
    private String Zone;

    public int getMinTemp() {
        return (int) Math.round(minTemp);
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
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

    public int getMaxTemp() {
        return (int) Math.round(maxTemp);
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
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

    public int getIconId() {
        return Forecast.getIconId(Icon);
    }
    public String getDayofWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(Zone));
        Date date = new Date(Time * 1000);
        return formatter.format(date);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Day() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(Time);
            dest.writeString(Sum);
            dest.writeDouble(maxTemp);
            dest.writeString(Zone);
            dest.writeString(Icon);
            dest.writeDouble(minTemp);
    }

    private Day(Parcel in) {
        Time = in.readLong();
        Sum = in.readString();
        maxTemp = in.readDouble();
        Zone = in.readString();
        Icon = in.readString();
        minTemp = in.readDouble();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
