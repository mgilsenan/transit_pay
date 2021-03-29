package com.example.transitpay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private static final String TAG = "Transit_Pay";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public float zoom = 15; // 1 for world, 5 for landmass, 10 for City, 15 for Streets, 20 for buildings.
    private GoogleMap mMap;
    private LatLng latLng;
    private boolean first_time=true;
    private Runnable myRunnable;

    private LocationListener locationListener;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment).commit();

        mapFragment.getMapAsync(this);

    }                // Constructor

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }                     // Create the three dots for the map menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        MapStyleOptions style;
        // Change the map type based on the user's selection.
        switch (item.getItemId())
        {
            case R.id.Retro_map:

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.Dark_map:

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.dark_style));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.Cartoon_map:

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.cartoon_style));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.Normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }               // Create the option menu: 4 different types of map

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults)
    {
        // Check if location permissions are granted and if so enable the location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    return;
                }
        }
    }          // Grant permissions*/

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        enableMyLocation();
        paint_map(mMap);
        setPoiClick(mMap);
        setMapLongClick(mMap);
        setMapClick(mMap);
    }                       // Draw the map and execute every function (Main)

    private void paint_map(final GoogleMap map)
    {
        try
        {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        }
        catch (Resources.NotFoundException e)
        {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    private void enableMyLocation()
    {
        if (ContextCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);

        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSION);
        }
    }                                        // obtain the GPS location

    private void setMapClick(final GoogleMap map)
    {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng)
            {
                map.clear();
            }
        });
    }

    private void setMapLongClick(final GoogleMap map)
    {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng) {
                map.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
                String snippet = String.format(Locale.getDefault(),
                        getString(R.string.lat_long_snippet),
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.dropped_pin))
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_ORANGE)).alpha(0.9f));
            }
        });
    }                      // after long click, set a new marker with its own Lat and Long

    private void setPoiClick(final GoogleMap map)
    {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener()
        {
            @Override
            public void onPoiClick(PointOfInterest poi)
            {
                map.clear();
                Marker poiMarker = map.addMarker(new MarkerOptions().position(poi.latLng).title(poi.name).icon(BitmapDescriptorFactory.defaultMarker
                        (BitmapDescriptorFactory.HUE_AZURE)).alpha(0.9f));
                poiMarker.showInfoWindow();
                poiMarker.setTag(getString(R.string.poi));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poi.latLng, mMap.getCameraPosition().zoom));

            }
        });
    }                          // Add marker on Point of interest

}
// Things to do:
// Include Track polylines
