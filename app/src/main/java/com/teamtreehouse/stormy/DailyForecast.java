package com.teamtreehouse.stormy;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;



import com.teamtreehouse.stormy.adapters.DayAdapter;

import java.util.Arrays;


public class DailyForecast extends ListActivity {
    private Day[] mDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        Intent intent = getIntent();
        Parcelable[] parcelable = intent.getParcelableArrayExtra(MainActivity.DAILY_FOR);
        mDays = Arrays.copyOf(parcelable, parcelable.length, Day[].class);
        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String dayOfTheWeek = mDays[position].getDayofWeek();
        String conditions = mDays[position].getSum();
        String highTemp = mDays[position].getMaxTemp() + "";
        String lowTemp = mDays[position].getMinTemp() + "";
        String message = String.format("On %s the high will be %s and the low will be %s and it will be %s", dayOfTheWeek, highTemp, lowTemp, conditions);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("dayofweek", mDays[position].getDayofWeek());
//
//        startActivity(intent);
    }
}
