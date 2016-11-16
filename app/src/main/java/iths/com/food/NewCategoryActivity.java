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

public class NewCategoryActivity extends AppCompatActivity {


    public static final int REQUEST_CODE = 0;
    public static final String EDIT_TEXT_KEY = "EDIT_TEXT";
    EditText addCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
        addCategoryName = (EditText) findViewById(R.id.add_category_editText);
        Button saveButton = (Button) findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText add_category_editText = (EditText) findViewById(R.id.add_category_editText);
                String text = add_category_editText.getText().toString();

                if (text.length() == 0) {
                    Toast.makeText(NewCategoryActivity.this, "Enter the category name.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EDIT_TEXT_KEY, text);
                    setResult(REQUEST_CODE, resultIntent);
                    finish();
                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        /*if (getSupportActionBar() != null){
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

    /*public void saveButtonPressed(View view) {
        //TODO: Save to database and update MealCategoryActivity

        EditText add_category_editText = (EditText) findViewById(R.id.add_category_editText);
        String text = add_category_editText.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EDIT_TEXT_KEY, text);
        setResult(REQUEST_CODE, resultIntent);
        //finish();
    }*/
}
