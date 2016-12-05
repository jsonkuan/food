package iths.com.food;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import iths.com.food.Fragments.MealFragment;
import iths.com.food.Fragments.AddMealFragment;
import iths.com.food.Fragments.CategoryFragment;
import iths.com.food.Fragments.NewCategoryFragment;
import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;

import static iths.com.food.Fragments.NewCategoryFragment.mViewPager;

public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;
    Button addCategory;
    int i = 1;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //open with CategoryFragment
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

                                break;
                        }
                        return false;
                    }
                });


        /*addCategory = (Button) findViewById(R.id.add_category_button);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new CategoryFragment()).commit();
            }
        });*/
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


}









