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

public class MealCategoryActivity extends AppCompatActivity {



    public ArrayList<String> foodtypes;
    ListAdapter listAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //TODO: Replace with sqlite
        foodtypes = new ArrayList<>();
        foodtypes.add("Popcorn");
        foodtypes.add("Sausages");
        foodtypes.add("Coffee");

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
}
