package iths.com.food.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import iths.com.food.Model.Category;
import iths.com.food.Model.Meal;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-28.
 *
 */

public class MapViewFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private DatabaseHelper db;
    ArrayList<Meal> allMeals = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        db = new DatabaseHelper(getActivity());

        ArrayList<Category> categories = db.getCategories();

        for (int i = 0; i < categories.size(); i++) {
            ArrayList<Meal> meals = db.getCategories().get(i).getMeals();
            for(int j = 0; j < meals.size(); j++) {
                allMeals.add(meals.get(j));
            }
        }



        /**
         * Creates the frame to display the GoogleMap
         */
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
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
        for (Meal meal : allMeals) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(meal.getLatitude(), meal.getLongitude()))
                    .title(meal.getName()).snippet(meal.getDescription()));
        }
    }


}
