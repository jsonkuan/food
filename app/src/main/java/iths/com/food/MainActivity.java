package iths.com.food;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import iths.com.food.Fragments.AddFragment;
import iths.com.food.Fragments.CategoriesFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the start fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddFragment()).commit();

        // Set the bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        // Make the bottom navigation working on clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_add:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, new AddFragment()).commit();
                                break;
                            case R.id.action_categories:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, new CategoriesFragment()).commit();
                                break;
                            case R.id.action_map:

                                break;
                        }
                        return false;
                    }
                });
    }
}
