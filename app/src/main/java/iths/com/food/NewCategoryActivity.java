package iths.com.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iths.com.food.Helper.DatabaseHelper;

public class NewCategoryActivity extends AppCompatActivity {

    DatabaseHelper db = new DatabaseHelper(this);
    public static final int REQUEST_CODE = 0;
    public static final String EDIT_TEXT_KEY = "EDIT_TEXT";
    EditText addCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_category);
        addCategoryName = (EditText) findViewById(R.id.add_category_editText);
        Button saveButton = (Button) findViewById(R.id.save_button);

/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveButtonPressed(View view) {

        EditText add_category_editText = (EditText) findViewById(R.id.add_category_editText);
        String categoryName = add_category_editText.getText().toString();
        db.insertCategory(categoryName, 1);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EDIT_TEXT_KEY, categoryName);
        setResult(REQUEST_CODE, resultIntent);
        finish();
    }
}
