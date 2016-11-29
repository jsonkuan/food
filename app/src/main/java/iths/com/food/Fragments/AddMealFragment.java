package iths.com.food.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;
import iths.com.food.Model.HeartRating;
import iths.com.food.Model.Meal;
import iths.com.food.Model.MyCamera;
import iths.com.food.R;

import static android.app.Activity.RESULT_OK;


/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class AddMealFragment extends Fragment{

    private HeartRating heart;
    private ArrayList<Category> categories;
    private Spinner spinner;
    private DatabaseHelper db;
    private static boolean isOpenedFromMenu;
    private ImageView imageView;
    private ImageView mealImage;
    private EditText name;
    private EditText description;
    private MyCamera camera;
    private int healthGrade;
    private int tasteGrade;
    private static final String TAG = "TAG";
    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveMeal();
            Log.d(TAG,"Knappen klickades på!");
        }
    };
    private View.OnClickListener heartButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            heart.fillHearts(v);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_add, container, false);

        if(isOpenedFromMenu) {
            v = inflater.inflate(R.layout.fragment_add, container, false);
            mealImage = (ImageView) v.findViewById(R.id.edit_meal_image);
            camera = new MyCamera(getActivity());
            camera.takePhoto();
            name = (EditText) v.findViewById(R.id.name);
            description = (EditText) v.findViewById(R.id.desc);
            Button b = (Button) v.findViewById(R.id.save_changes_button);
            b.setOnClickListener(saveButtonListener);
            db = new DatabaseHelper(this.getActivity().getApplicationContext());
            categories = db.getCategories();
            spinner = (Spinner) v.findViewById(R.id.spinner);
            setUpSpinner();
            isOpenedFromMenu = false;
        } else {
            v = inflater.inflate(R.layout.fragment_add_meal, container, false);
            mealImage = (ImageView) v.findViewById(R.id.meal_image);
        }

        heart = new HeartRating(getActivity().getApplicationContext(), getActivity());

        for(int i=1; i<=10; i++) {
            //get the resource id number for health hearts
            int resNr = getActivity().getResources().getIdentifier("edit_heart_health_" + i, "id",
                    getActivity().getPackageName());
            imageView = (ImageView) v.findViewById(resNr);
            imageView.setOnClickListener(heartButtonListener);
            //get the resource id number for taste hearts
            resNr = getActivity().getResources().getIdentifier("edit_heart_taste_" + i, "id",
                    getActivity().getPackageName());
            imageView = (ImageView) v.findViewById(resNr);
            imageView.setOnClickListener(heartButtonListener);
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == MyCamera.CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                camera.showImage(mealImage);
            }
        }
    }

    public static void setOpenedFromMenu(boolean b) {
        isOpenedFromMenu = b;
    }

    /**
     * Get category names from database and populate Spinner.
     */
    private void setUpSpinner() {
        String[] categoryNames = new String[categories.size()];
        for(int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categoryNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void saveMeal() {
        Meal meal = new Meal();
        meal.setHealthyScore(HeartRating.getHealthGrade());
        meal.setTasteScore(HeartRating.getTasteGrade());
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
        meal.setCategory(spinner.getSelectedItem().toString());

        Date dateTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
        meal.setDateTime(dateFormat.format(dateTime));
        meal.setLatitude(0);
        meal.setLongitude(0);
        String imagePath = camera.getPhotoFilePath().toString();
        meal.setImagePath(imagePath);

        long id = db.insertMeal(meal);

        db.close();

        //TODO: ändra så att den öppnar typ MealListFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CategoryFragment()).commit();
    }

    public void updateMeal(View view) {

        //VI FÅR IN ID PÅ ANNAT SÄTT
        //String text = ((EditText) findViewById(R.id.idOfMeal)).getText().toString();
        //id = Long.valueOf(text);
        Long id = 3L;
        Meal meal = db.getMeal(id);
        meal.setHealthyScore(healthGrade);
        meal.setTasteScore(tasteGrade);
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
//PUTBACK **** meal.setCategory(spinner.getSelectedItem().toString());
        meal.setImagePath("insert ImagePath");

        // Man kan inte ändra ID eller location??

        int rowsAffected = db.updateMeal(meal);

        if (rowsAffected > 0) {
            Toast.makeText(getActivity(), rowsAffected+" rows updated", Toast.LENGTH_SHORT).show();
        }

    }
}


