package iths.com.food.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Helper.GPSHelper;
import iths.com.food.MainActivity;
import iths.com.food.Model.Locations;
import iths.com.food.Place;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-28.
 */

public class MapViewFragment extends Fragment {

    ArrayList<Locations> locationsArrayList = new ArrayList<>();
    private MapView mMapView;
    private GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    public static final String TAG = "GPS_TEST";
    GPSHelper gps;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        gps = new GPSHelper(getActivity());
        db = new DatabaseHelper(getActivity());

        /**
         * //TODO: Replace with actual locations and store in DB
         *  CLASS: Locations - Takes Place as arg
         *  ENUM: Place - Stores a hardcoded Location, Latitude and Longitude ex. GOTHENBURG (57.7, 11.97)
         */
        Locations location = new Locations(Place.GOTHENBURG);
        Locations location2 = new Locations(Place.MALMO);
        Locations location3 = new Locations(Place.STOCKHOLM);
        Locations location4 = new Locations(Place.SKOOL);
        //Locations foodLocation = new Locations("foodLocation");

        //Place food = new Place(db.getMeal(0).getLatitude(),db.getMeal(0).getLongitude());

        locationsArrayList.add(location);
        locationsArrayList.add(location2);
        locationsArrayList.add(location3);
        locationsArrayList.add(location4);
        //locationsArrayList.add(foodLocation);

        /**
         * Creates the frame to display the GoogleMap
         */
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         *  Configures the map with the start position & camera zoom.
         */
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                markLocations();

                LatLng london = new LatLng(55.5, 15);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(london).zoom(5).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                /**
                 * Obligatory code block for handling permissions
                 */
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
            }
        });

        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    /**
     * Places map markers on each Locations in ArrayList<Locations> locationsArrayList = new ArrayList<>();
     */
    public void markLocations() {
        for (Locations l : locationsArrayList) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(l.getLat(), l.getLng()))
                    .title(l.getTitle()).snippet("Description here..."));
        }
    }


}
