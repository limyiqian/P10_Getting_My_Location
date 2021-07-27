package sg.edu.rp.c346.id19020125.p10_getting_my_location;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileWriter;

public class LocationService extends Service {

    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    private FusedLocationProviderClient client;


    @Override
    public void onCreate() {
        Log.d("LocationService", "Service created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocationService", "Service start");
        client = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    double lat = data.getLatitude();
                    double lng = data.getLongitude();
                    try {
                        String folder = getFilesDir().getAbsolutePath() + "/Locations";
                        File file = new File(folder, "locationData.txt");
                        FileWriter writer = new FileWriter(file, true);
                        writer.write(lat + " " + lng + "\n");
                        Log.d("LocationService",lat + " " + lng );
                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        boolean permissionResult = checkPermission();
        if (permissionResult) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(30);
            mLocationRequest.setSmallestDisplacement(500);
            client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("LocationService", "Service exited");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck_External = ContextCompat.checkSelfPermission(
                LocationService.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if(permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED || permissionCheck_External == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        }
        else {
            return false;
        }
    }


}