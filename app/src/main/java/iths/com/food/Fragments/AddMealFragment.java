package iths.com.food.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import static iths.com.food.R.id.container;


/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class AddMealFragment extends Fragment{

    private static final String MAKE_EDITABLE = "make_editable";
    private static final String MEAL_ID = "meal_id";
    View v;
    private HeartRating heart;
    private ArrayList<Category> categories;
    private Spinner spinner;
    private DatabaseHelper db;
    private static boolean isOpenedFromMenu;
    private ImageView imageView;
    private ImageView mealImage;
    private EditText name;
    private TextView nameText;
    private EditText description;
    private TextView descriptionText;
    private TextView categoryText;
    private TextView averageNumber;
    private MyCamera camera;
    private static final String TAG = "TAG";
    private long id;
    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveMeal();
        }
    };
    private View.OnClickListener heartButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            heart.fillHearts(v);
        }
    };
    private View.OnClickListener editButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            makeEditable(id);
        }
    };
    private View.OnClickListener updateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = getArguments();
            long id = bundle.getLong(MEAL_ID);
            updateMeal(id);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_meal_editable, container, false);

        Bundle bundle = getArguments();

        if(isOpenedFromMenu) {
            v = inflater.inflate(R.layout.fragment_meal, container, false);
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
            ImageView heartImage;
            for(int i=1; i<=10; i++) {
                //get the resource id number for health hearts
                int resNr = getActivity().getResources().getIdentifier("edit_heart_health_" + i, "id",
                        getActivity().getPackageName());
                heartImage = (ImageView) v.findViewById(resNr);
                heartImage.setOnClickListener(heartButtonListener);
                //get the resource id number for taste hearts
                resNr = getActivity().getResources().getIdentifier("edit_heart_taste_" + i, "id",
                        getActivity().getPackageName());
                heartImage = (ImageView) v.findViewById(resNr);
                heartImage.setOnClickListener(heartButtonListener);
            }
        } else if (bundle.getBoolean(MAKE_EDITABLE)) {
            v = inflater.inflate(R.layout.fragment_meal_editable, container, false);
            mealImage = (ImageView) v.findViewById(R.id.edit_meal_image);
            name = (EditText) v.findViewById(R.id.name);
            description = (EditText) v.findViewById(R.id.desc);
            Button b = (Button) v.findViewById(R.id.save_changes_button);
            b.setOnClickListener(updateButtonListener);
            db = new DatabaseHelper(this.getActivity().getApplicationContext());
            categories = db.getCategories();
            spinner = (Spinner) v.findViewById(R.id.spinner);
            setUpSpinner();
            isOpenedFromMenu = false;
            ImageView heartImage;
            for (int i = 1; i <= 10; i++) {
                //get the resource id number for health hearts
                int resNr = getActivity().getResources().getIdentifier("edit_heart_health_" + i, "id",
                        getActivity().getPackageName());
                heartImage = (ImageView) v.findViewById(resNr);
                heartImage.setOnClickListener(heartButtonListener);
                //get the resource id number for taste hearts
                resNr = getActivity().getResources().getIdentifier("edit_heart_taste_" + i, "id",
                        getActivity().getPackageName());
                heartImage = (ImageView) v.findViewById(resNr);
                heartImage.setOnClickListener(heartButtonListener);


            }
            displayEditableMeal(bundle.getLong(MEAL_ID));
        }
        else {
            db = new DatabaseHelper(getActivity());
            v = inflater.inflate(R.layout.fragment_meal_editable, container, false);
            mealImage = (ImageView) v.findViewById(R.id.meal_image);
            Button b = (Button) v.findViewById(R.id.edit_button);
            b.setOnClickListener(editButtonListener);
            id = bundle.getLong(MealFragment.MEAL_ID);
        }

        heart = new HeartRating(getActivity().getApplicationContext(), getActivity());

        bundle = this.getArguments();
        if(bundle == null || bundle.getBoolean(MAKE_EDITABLE)) {
        } else {
            Long id = bundle.getLong(MealFragment.MEAL_ID);
            displayMeal(id);
        }




        return v;
    }

    private void displayEditableMeal(long id) {
        Meal meal = db.getMeal(id);
        name.setText(meal.getName());
        description.setText(meal.getDescription());

        Uri filePathUri = Uri.parse(meal.getImagePath());
        Bitmap image = BitmapFactory.decodeFile(filePathUri.getPath());
        mealImage.setImageBitmap(image);

        ArrayList<Category> categories = db.getCategories();
        int position = 0;
        for(int i = 0; i < categories.size(); i++) {
            if (meal.getCategory().equals(categories.get(i).getName())) {
                position = i;
            }
        }

        spinner.setSelection(position);

    }

    private void displayMeal(Long id) {
        Meal meal = db.getMeal(id);
        nameText = (TextView)v.findViewById(R.id.meal_name_text);
        descriptionText = (TextView)v.findViewById(R.id.meal_description);
        imageView = (ImageView) v.findViewById(R.id.meal_image);
        categoryText = (TextView)v.findViewById(R.id.category_text);
        averageNumber = (TextView) v.findViewById(R.id.average_number);


        nameText.setText(meal.getName());
        descriptionText.setText(meal.getDescription());
        Uri filePathUri = Uri.parse(meal.getImagePath());
        Bitmap image = BitmapFactory.decodeFile(filePathUri.getPath());
        imageView.setImageBitmap(image);
        categoryText.setText(meal.getCategory());
        averageNumber.setText(""+meal.getTotalScore());

        //heart.setHearts(getContext(), false, meal.getHealthyScore(), meal.getTasteScore());


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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        meal.setDateTime(dateFormat.format(dateTime));
        meal.setLatitude(0);
        meal.setLongitude(0);
        String imagePath = camera.getPhotoFilePath().toString();
        meal.setImagePath(imagePath);

        long id = db.insertMeal(meal);

        db.close();

        //TODO: ändra så att den öppnar typ MealListFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(container, new CategoryFragment()).commit();
    }

    public void updateMeal(long id) {

        Meal meal = db.getMeal(id);
        meal.setHealthyScore(HeartRating.getHealthGrade());
        meal.setTasteScore(HeartRating.getTasteGrade());
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
        meal.setCategory(spinner.getSelectedItem().toString());
        //String imagePath = camera.getPhotoFilePath().toString();
        //meal.setImagePath(imagePath);


        int rowsAffected = db.updateMeal(meal);

        if (rowsAffected > 0) {
            Toast.makeText(getActivity(), rowsAffected+" rows updated", Toast.LENGTH_SHORT).show();
        }

    }
    public void makeEditable(long id) {
        AddMealFragment newFragment = new AddMealFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MAKE_EDITABLE, true);
        bundle.putLong(MEAL_ID, id);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }
}


