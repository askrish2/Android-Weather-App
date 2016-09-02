package com.teamtreehouse.stormy;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FOR = "DAILY_FORECAST";
    public static final String HOURLY_FOR = "HOURLY_FORECAST";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    //private CurrentWeather mcurr = new CurrentWeather();
    private Forecast mforecast;
    private GoogleApiClient mGoogleApiClient;
    public double mLatitude = 37.826977;
    public double mLongitude = -122.422956;
    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.humVal) TextView mHumidityValue;
    @BindView(R.id.prepVal) TextView mPrepValue;
    @BindView(R.id.sumLabel) TextView mSummaryLabel;
    @BindView(R.id.IconLabel) ImageView mIconImageView;
    @BindView(R.id.refresh_but) ImageView mRefresh;
    @BindView(R.id.progressBar) ProgressBar progress;
    @BindView(R.id.locLabel) TextView mLocationLabel;
    //@BindView(R.id.locationLabel) TextView mCityLabel;
    //@BindView(R.id.typeVal) TextView mType;
    @BindView(R.id.actualVal) TextView mActual;
    private String mCity;
    private String mCountryCode;
    private String mState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        progress.setVisibility(View.INVISIBLE);
        //final double latitude = 37.8267;
        //final double longitude = -122.423;
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(mLatitude, mLongitude);
            }
        });
        getForecast(mLatitude, mLongitude);
        //Log.d(TAG, "Main UI code is running!");
    }

    private void getForecast(double latitude, double longitude) {
       // Log.d(TAG, "lat: " + latitude);
        String apiKey = "06e4df877c4eb103387e2ad1c1ac169c";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });


                    try {
                        String jsonData = response.body().string();
                        //Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            //mcurr = getCurrentDetails(jsonData);
                            mforecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });


                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                       // Log.e(TAG, "Exception caught: ", e);
                    }
                    catch (JSONException e) {
                       // Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });

    }
    else {
            Toast.makeText(this, getString(R.string.network_unavailable), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (progress.getVisibility()==View.INVISIBLE) {
            progress.setVisibility(View.VISIBLE);
            mRefresh.setVisibility(View.INVISIBLE);
        }
        else {
            progress.setVisibility(View.INVISIBLE);
            mRefresh.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        CurrentWeather mcurr = mforecast.getCurr();
        mTemperatureLabel.setText(mcurr.getmTemp() + "");
        mTimeLabel.setText("At " + mcurr.getFormattedTime() + " it will be");
        mHumidityValue.setText(mcurr.getmHum() + "");
        mPrepValue.setText(mcurr.getmPrep() + "%");
        mSummaryLabel.setText(mcurr.getmSum());
        Drawable drawable = getResources().getDrawable(mcurr.getIconId());
        mIconImageView.setImageDrawable(drawable);
        if (mCity != null && mState != null) {
            mLocationLabel.setText(mCity + ", " + mState);
        }
        //mCityLabel.setText(mCity + ", " + mCountryCode);
        //mIconImageView.setImageResource(mcurr.getIconId());
        //mType.setText(mcurr.getmType());
        mActual.setText(mcurr.getmActual() + "");


    }
    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurr(getCurrentDetails(jsonData));
        forecast.setHour(getHourlyForecast(jsonData));
        forecast.setDays(getDailyForecast(jsonData));
        //add alert method setting here
        return forecast;
    }
//MAKE A GET ALERTS METHOD
    //call jsonarray for alerts and array of alert objects
    /**
    private Alert[] getAlerts(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONArray alerts = forecast.getJSONArray("alert");
        Alert[] alert = new Alert[alerts.length];
        for (int i = 0; i < alerts.length(); i++) {
            JSONObject jalert = alerts.getJSONObject(i);
            Alerts a = new Alert();
            a.setTitle(jalert.getString("title"));
            a.setExpire(jalert.getString("expire"));
            a.setDescription(jalert.getString("description"));
            alert[i] = a;
        }
        return alert;
    }
**/



    private Day[] getDailyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        Day[] day = new Day[data.length()];
        for (int i=0; i<data.length(); i++) {
            JSONObject jday = data.getJSONObject(i);
            Day dy = new Day();
            dy.setSum(jday.getString("summary"));

            dy.setMaxTemp(jday.getLong("temperatureMax"));
            dy.setMinTemp(jday.getLong("temperatureMin"));
            dy.setIcon(jday.getString("icon"));
            dy.setTime(jday.getLong("time"));
            dy.setZone(timezone);
            //dy.setSunrise(jday.getLong("sunriseTime"));
            //dy.setSunset(jday.getLong("sunsetTime"));
            //dy.setMoon(jday.getDouble("moonPhase"));
            //get more details for daily object
            day[i] = dy;
        }
        return day;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");
        Hour[] hour = new Hour[data.length()];
        for (int i=0; i<data.length(); i++) {
            JSONObject jhour = data.getJSONObject(i);
            Hour hr =  new Hour();
            hr.setSum(jhour.getString("summary"));
            hr.setTemp(jhour.getLong("temperature"));
            hr.setIcon(jhour.getString("icon"));
            hr.setTime(jhour.getLong("time"));
            hr.setZone(timezone);
            hour[i] = hr;
        }
        return hour;

    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        //Log.i(TAG, "From JSON: " + timeZone);
        JSONObject curr = forecast.getJSONObject("currently");
        CurrentWeather cw = new CurrentWeather();
        cw.setmHum(curr.getDouble("humidity"));
        cw.setmTime(curr.getLong("time"));
        cw.setmIcon(curr.optString("icon"));
        cw.setmPrep(curr.getDouble("precipProbability"));
        cw.setmSum(curr.getString("summary"));
        cw.setmTemp(curr.getDouble("temperature"));
        cw.setmTimeZone(timeZone);
        //cw.setmType(curr.getString("precipType"));
        cw.setmActual(curr.getDouble("apparentTemperature"));
        //cw.setmWind(curr.getDouble("windSpeed"));
        //get more details for currently object
       // Log.d(TAG, cw.getFormattedTime());
        return cw;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo!=null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
            AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.show(getFragmentManager(), "error_dialog");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    private void handleNewLocation(Location location) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        //Log.d(TAG, location.toString());

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(mLatitude, mLongitude, 1);
        } catch (IOException e) {
          //  Log.e(TAG, "No GCD location resolved.");
        }
        if (addresses != null) {
            mCity = addresses.get(0).getLocality();
            mCountryCode = addresses.get(0).getCountryCode();
            mState = addresses.get(0).getAdminArea();
        } else {
            //Log.d(TAG, "No address details available");
            Toast.makeText(this, "No address details", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
        getForecast(mLatitude, mLongitude);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
          //  Log.d(TAG, "Inside the catch block: " + e);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
       // Log.i(TAG, "Location services suspended. Please reconnect.");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
           // Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @OnClick(R.id.dayButton)
    public void startDaily(View view) {
        Intent intent = new Intent(this, DailyForecast.class);
        intent.putExtra(DAILY_FOR, mforecast.getDays());
        startActivity(intent);
    }

    @OnClick(R.id.hourbutton)
    public void startHourly(View view) {
        Intent intent = new Intent(this, HourlyForecast.class);
        intent.putExtra(HOURLY_FOR, mforecast.getHour());
        startActivity(intent);
    }
}
