package iths.com.food;

import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MealCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_category);

        String[] foodtypes = {"Popcorn", "Sausages", "Coffee", "Fish", "Pancakes", "Bacon", "Canned Food", "Baby Food"};

        ListAdapter listAdapter = new CustomAdapter(this, foodtypes);

        ListView listView = (ListView) findViewById(R.id.categoryListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String foodtypes = String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(MealCategoryActivity.this, "" + foodtypes + "is nr " + (i+1)+  "on this!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
