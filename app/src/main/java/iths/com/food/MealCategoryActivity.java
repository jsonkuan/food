package iths.com.food;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MealCategoryActivity extends AppCompatActivity {

    Database database = new Database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_category_list);

    }

    public void deleteDatabase(View v) {
        Intent intent = new Intent(this, MealCategoryActivity.class);
        super.deleteDatabase(Database.DATABASE_NAME);
        Database db = new Database(this);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "Database deleted and restarted", Toast.LENGTH_SHORT).show();
    }
    public void createNewMeal(View v) {
        Intent intent = new Intent(this, MealActivity.class);
        startActivity(intent);
    }
}
