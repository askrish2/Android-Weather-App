package com.teamtreehouse.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamtreehouse.stormy.Day;
import com.teamtreehouse.stormy.R;

/**
 * Created by ashkrishnan on 7/2/16.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }
    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; //tag items for easy ref not using
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconDayView);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.DayNameLabel);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
            Day day = mDays[position];
            holder.iconImageView.setImageResource(day.getIconId());
            holder.temperatureLabel.setText(day.getMaxTemp() + "");
            if (position==0) {
                holder.dayLabel.setText("Today");
            }
            else {
                holder.dayLabel.setText(day.getDayofWeek());
            }
        return convertView;
    }
    private static class ViewHolder {
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;

    }
}
