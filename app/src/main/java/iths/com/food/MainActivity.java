package iths.com.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import iths.com.food.Fragments.CategoryFragment;
import iths.com.food.Fragments.MapViewFragment;
import iths.com.food.Fragments.MealFragment;


public class MainActivity extends AppCompatActivity  {

    public static String PACKAGE_NAME;

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
}