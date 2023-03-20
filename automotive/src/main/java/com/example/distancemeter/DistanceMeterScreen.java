package com.example.distancemeter;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;


public class DistanceMeterScreen extends Screen implements LocationListener {

    Pane.Builder paneBuilder = new Pane.Builder();

    LocationInteractor manager;

    Location startingLocation;
    Location currentLocation;

    float distanceInMeter = 0;

    public DistanceMeterScreen(@NonNull CarContext carContext) {
        super(carContext);

        manager = LocationInteractor.getInstance(carContext, this);
        manager.startLocationUpdate();
    }


    @NonNull
    @Override
    public Template onGetTemplate() {

        Row row1 = new Row.Builder()
                .setTitle(" ").build();

        Row row2 = new Row.Builder()
                .setTitle("Hello driver!").build();

        Row row3 = new Row.Builder()
                .setTitle("Please fasten your seatbelt").build();

        Row row4 = new Row.Builder()
                .setTitle("Speed limit: 120 km/h").build();

        paneBuilder.addRow(row1);
        paneBuilder.addRow(row2);
        paneBuilder.addRow(row3);
        paneBuilder.addRow(row4);

        paneBuilder.addAction(
                new Action.Builder()
                        .setTitle("Total distance")
                        .setOnClickListener(this::onClickNavigate)
                        .build());

        return new PaneTemplate.Builder(paneBuilder.build())
                .build();
    }

    private void onClickNavigate() {

        String distanceMessage = "You drove: ";

        if (distanceInMeter < 1000) {
            distanceMessage += (int)(distanceInMeter) + " m";
        } else {
            distanceMessage += (int)(distanceInMeter/1000) + " km";
        }

        CarToast.makeText(getCarContext(), distanceMessage, CarToast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (startingLocation == null) {
            startingLocation = location;
        }

        currentLocation = location;

        distanceInMeter = currentLocation.distanceTo(startingLocation);
    }
}

