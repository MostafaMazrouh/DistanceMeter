package com.example.distancemeter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;


public class LocationInteractor {

    private final LocationListener locationListener;

    private Context mContext;

    private Location currentLocation;

    private LocationManager mLocationManager;

    private static LocationInteractor INSTANCE;

    public LocationInteractor(Context context, final LocationListener listener) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = listener;
    }

    public synchronized static LocationInteractor getInstance(Context context, final LocationListener listener) {
        if (INSTANCE == null) {
            INSTANCE = new LocationInteractor(context, listener);
        }
        return INSTANCE;
    }

    public void startLocationUpdate() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setCostAllowed(false);

        String provider = mLocationManager.getBestProvider(criteria, true);

        if (provider == null) {
            List<String> providers = mLocationManager.getAllProviders();
            for (String temp : providers) {
                if (mLocationManager.isProviderEnabled(temp)) {
                    provider = temp;
                }
            }
        }
        long distanceChage = 20;
        long updateTime = 1000 * 50;
        if (provider != null && Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(provider, updateTime, distanceChage, locationListener);
            currentLocation = mLocationManager.getLastKnownLocation(provider);
        } else if (provider != null && Build.VERSION.SDK_INT < 23) {
            mLocationManager.requestLocationUpdates(provider, updateTime, distanceChage, locationListener);
            currentLocation = mLocationManager.getLastKnownLocation(provider);
        }
    }


    public void stopLocationUpdate() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.removeUpdates(locationListener);
            }
        }
    }

    public Location getLocation() {
        return currentLocation;
    }

}

