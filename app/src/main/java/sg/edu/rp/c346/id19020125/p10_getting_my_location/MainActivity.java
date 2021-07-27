package sg.edu.rp.c346.id19020125.p10_getting_my_location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    Button btnStartDe, btnStopDe, btnCheck;
    ToggleButton toggleMusic;
    TextView tvLast;
    private GoogleMap map;
    private FusedLocationProviderClient client;
    String folderLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartDe = findViewById(R.id.btnStartDe);
        btnStopDe = findViewById(R.id.btnStopDe);
        btnCheck = findViewById(R.id.btnCheck);
        toggleMusic = findViewById(R.id.toggleMusic);
        tvLast = findViewById(R.id.tvLast);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.map);

        client = LocationServices.getFusedLocationProviderClient(this);

        folderLocation = getFilesDir().getAbsolutePath() + "/Locations";
        File folder = new File(folderLocation);
        if(folder.exists() == false) {
            boolean result = folder.mkdir();
            if(result) {
                Log.i("Folder", "Folder created");
            }
            else {
                Log.i("Folder", "Folder not created");
            }
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                UiSettings ui = map.getUiSettings();
                ui.setZoomControlsEnabled(true);
                ui.setCompassEnabled(true);

                boolean check = checkPermission();
                if(check) {
                    map.setMyLocationEnabled(true);
                    client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                tvLast.setText("Last known location: \nLatititude: " + location.getLatitude() + "\nLongtitude: " + location.getLongitude());
                                LatLng lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 11));
                            }
                        }
                    });
                }
                else {
                    Log.d("Permission","Denied");
                }

            }
        });

        btnStartDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                Intent startDetectorService = new Intent(MainActivity.this, LocationService.class);
                startService(startDetectorService);
            }
        });

        btnStopDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopDetectorService = new Intent(MainActivity.this, LocationService.class);
                stopService(stopDetectorService);
            }
        });

        toggleMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent startMusicService = new Intent(MainActivity.this, MusicService.class);
                if(isChecked) {
                    Log.d("MusicService","checked");
                    startService(startMusicService);
                }
                else {
                    Log.d("MusicService","not checked");
                    stopService(startMusicService);
                }
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CheckRecords.class);
                startActivity(i);
            }
        });

    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck_External = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if(permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED || permissionCheck_External == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return false;
        }
    }
}




