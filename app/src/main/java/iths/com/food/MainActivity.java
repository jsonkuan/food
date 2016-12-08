package iths.com.food;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import iths.com.food.Fragments.MealFragment;
import iths.com.food.Fragments.AddMealFragment;
import iths.com.food.Fragments.CategoryFragment;
import iths.com.food.Fragments.NewCategoryFragment;
import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;
import static iths.com.food.Fragments.NewCategoryFragment.mViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import iths.com.food.Fragments.MealFragment;
import iths.com.food.Fragments.CategoryFragment;
import iths.com.food.Fragments.MapViewFragment;


public class MainActivity extends AppCompatActivity  {

    public static String PACKAGE_NAME;
    Button addCategory;
    int i = 1;
    DatabaseHelper db;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CategoryFragment()).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_add:
                                MealFragment.setOpenedFromMenu(true);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, new MealFragment()).commit();
                                break;
                            case R.id.action_categories:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, new CategoryFragment()).commit();
                                break;
                            case R.id.action_map:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, new MapViewFragment()).commit();
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    public void addCategoryButtonPressed(View view) {


        // go back to AddCategoryFragment:
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new NewCategoryFragment()).commit();
    }
    /*protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        //TextView mLatitudeText = (TextView) findViewById(R.id.Latitude_text);

        if (mLastLocation != null) {
           // mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            Toast.makeText(this, ""+mLastLocation.getLatitude(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    } */
}









