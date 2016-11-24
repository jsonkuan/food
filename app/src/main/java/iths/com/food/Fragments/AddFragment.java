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
import android.widget.ImageButton;
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
import iths.com.food.MainActivity;
import iths.com.food.Model.Category;
import iths.com.food.Model.HeartRating;
import iths.com.food.Model.Meal;
import iths.com.food.Model.MyCamera;
import iths.com.food.R;

import static android.app.Activity.RESULT_OK;


/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class AddFragment extends Fragment implements OnClickListener{

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
            v = inflater.inflate(R.layout.fragment_meal, container, false);
            mealImage = (ImageView) v.findViewById(R.id.meal_image);
        }

        return v;
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

        //TODO: Find proper id to avoid hardcoding
        heart = new HeartRating(getActivity().getApplicationContext(), getActivity());
        imageView = (ImageView) v.findViewById(R.id.edit_heart_health_1);
        imageView.setOnClickListener(this);

        imageView = (ImageView) v.findViewById(R.id.edit_heart_health_2);
        imageView.setOnClickListener(this);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.edit_taste_hearts);
        layout.setOnClickListener(this);

        LinearLayout layout2 = (LinearLayout) v.findViewById(R.id.edit_health_hearts);
        layout2.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_heart_health_1:
                heart.fillHearts(view);
                break;
            case R.id.edit_heart_health_2:
                heart.fillHearts(view);
                break;
            case R.id.edit_health_hearts:
                heart.fillHearts(view);
                break;
            case R.id.edit_taste_hearts:
                heart.fillHearts(view);
                break;
            default:
                System.out.println("WTF is going on?");
        }
    }

    public static void setOpenedFromMenu(boolean b) {
        isOpenedFromMenu = b;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == MyCamera.CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                camera.showImage(mealImage);
            }
        }
    }

    public void saveMeal() {

        Meal meal = new Meal();

        //TODO: Ta bort denna testgrej, ersätt med betyg från hjärtan
        healthGrade = 0;
        tasteGrade = 0;
        meal.setHealthyScore(healthGrade);
        meal.setTasteScore(tasteGrade);
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
        meal.setCategory(spinner.getSelectedItem().toString());

        Date dateTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
        meal.setDateTime(dateFormat.format(dateTime));
        meal.setLatitude(0);
        meal.setLongitude(0);
        meal.setImagePath("insert ImagePath");

        long id = db.insertMeal(meal);

        Toast.makeText(getActivity(), "Saved to "+meal.getCategory(), Toast.LENGTH_SHORT).show();

        //TODO: ändra så att den öppnar typ MealListFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CategoryFragment()).commit();
    }
}
