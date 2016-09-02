package com.teamtreehouse.stormy;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.teamtreehouse.stormy.adapters.DayAdapter;
import com.teamtreehouse.stormy.adapters.HourAdapter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HourlyForecast extends ActionBarActivity {
    private Hour[] mHours;
    @BindView(R.id.recyclerView)
    RecyclerView mRecycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Parcelable[] parcelable = intent.getParcelableArrayExtra(MainActivity.HOURLY_FOR);
        mHours = Arrays.copyOf(parcelable, parcelable.length, Hour[].class);
        HourAdapter adapter = new HourAdapter(this, mHours);
        mRecycle.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.setHasFixedSize(true);
   }


}
