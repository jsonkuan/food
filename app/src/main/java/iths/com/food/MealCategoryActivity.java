package iths.com.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;

public class MealCategoryActivity extends AppCompatActivity {



    public ArrayList<String> foodtypes;
    ListAdapter listAdapter;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_category);

        db = new DatabaseHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Category> categories = db.getCategories();
        foodtypes = new ArrayList<>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
            foodtypes.add(categories.get(i).getName());
        }

        listAdapter = new CustomAdapter(this, foodtypes);
        ListView listView = (ListView) findViewById(R.id.categoryListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String foodtypes = String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(MealCategoryActivity.this, "" + foodtypes + "is nr " + (i + 1) + "on this!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void addMeal(View view) {
        Intent intent = new Intent(this, MealActivity.class);
        MealActivity.setOpenedFromCameraActivity(true);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meal_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_category_item:
                Intent intent = new Intent(this, NewCategoryActivity.class);
                startActivityForResult(intent, 100);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == NewCategoryActivity.REQUEST_CODE) {

                String text = data.getStringExtra(NewCategoryActivity.EDIT_TEXT_KEY);

                foodtypes.add(text);

                ((BaseAdapter) listAdapter).notifyDataSetChanged();
        //}

    }

   /* public void deleteDatabase(View v) {
        Intent intent = new Intent(this, MealCategoryActivity.class);
        super.deleteDatabase("food.db");
        db = new DatabaseHelper(this);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "Database deleted and restarted", Toast.LENGTH_SHORT).show();
    } */

}
