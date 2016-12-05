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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import iths.com.food.Helper.CategoryAdapter;
import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Helper.MealAdapter;
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

public class MealFragment extends Fragment{

    private static final String MAKE_EDITABLE = "make_editable";
    public static final String MEAL_ID = "meal_id";
    private static boolean isOpenedFromMenu;

    private HeartRating heart;
    private ArrayList<Category> categories;
    private DatabaseHelper db;
    private MyCamera camera;

    private View layoutView;
    private ImageView mealImage, heartImage, cameraIcon;
    private EditText nameEdit, descriptionEdit;
    private TextView nameText, descriptionText, categoryText, averageNumber;
    private Spinner spinner;
    private Button saveButton, editButton;

    private long id;
    private View.OnClickListener cameraButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            camera = new MyCamera(getActivity());
            camera.takePhoto();
        }
    };
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

        layoutView = inflater.inflate(R.layout.fragment_meal_editable, container, false);

        db = new DatabaseHelper(getActivity());
        categories = db.getCategories();

        spinner = (Spinner) layoutView.findViewById(R.id.spinner);
        nameEdit = (EditText) layoutView.findViewById(R.id.name);
        descriptionEdit = (EditText) layoutView.findViewById(R.id.desc);
        saveButton = (Button) layoutView.findViewById(R.id.save_changes_button);
        cameraIcon = (ImageView) layoutView.findViewById(R.id.camera_icon);

        cameraIcon.setOnClickListener(cameraButtonListener);

        Bundle bundle = getArguments();

        if(isOpenedFromMenu) {
            mealImage = (ImageView) layoutView.findViewById(R.id.edit_meal_image);

            camera = new MyCamera(getActivity());
            camera.takePhoto();

            saveButton.setOnClickListener(saveButtonListener);
            setUpSpinner();
            setHeartClickListeners();
            isOpenedFromMenu = false;
        } else if (bundle.getBoolean(MAKE_EDITABLE)) {
            mealImage = (ImageView) layoutView.findViewById(R.id.edit_meal_image);
            saveButton.setOnClickListener(updateButtonListener);
            setUpSpinner();
            setHeartClickListeners();
            displayEditableMeal(bundle.getLong(MEAL_ID));
            //isOpenedFromMenu = false;
        }
        else {
            layoutView = inflater.inflate(R.layout.fragment_meal, container, false);
            mealImage = (ImageView) layoutView.findViewById(R.id.meal_image);
            editButton = (Button) layoutView.findViewById(R.id.edit_button);
            editButton.setOnClickListener(editButtonListener);
            id = bundle.getLong(MealListFragment.MEAL_ID);
        }
        heart = new HeartRating(getActivity().getApplicationContext(), getActivity());
        bundle = this.getArguments();
        if(bundle == null || bundle.getBoolean(MAKE_EDITABLE)) {
        } else {
            long id = bundle.getLong(MealListFragment.MEAL_ID);
            displayMeal(id);
        }

        return layoutView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == MyCamera.CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                camera.showImage(mealImage);
            }
        }
    }

    /**
     * Save a new meal to the database.
     */
    public void saveMeal() {
        Meal meal = new Meal();
        meal.setHealthyScore(HeartRating.getHealthGrade());
        meal.setTasteScore(HeartRating.getTasteGrade());
        meal.setName(nameEdit.getText().toString());
        meal.setDescription(descriptionEdit.getText().toString());
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

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(container, new CategoryFragment()).commit();
    }

    /**
     * Save changes to a meal to the database.
     * @param id The database id of the meal that is updated.
     */
    public void updateMeal(long id) {

        Meal meal = db.getMeal(id);
        meal.setHealthyScore(HeartRating.getHealthGrade());
        meal.setTasteScore(HeartRating.getTasteGrade());
        meal.setName(nameEdit.getText().toString());
        meal.setDescription(descriptionEdit.getText().toString());
        meal.setCategory(spinner.getSelectedItem().toString());
        //String imagePath = camera.getPhotoFilePath().toString();
        //meal.setImagePath(imagePath);


        int rowsAffected = db.updateMeal(meal);

        if (rowsAffected > 0) {
            Toast.makeText(getActivity(), rowsAffected+" rows updated", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Change the screen so that you can edit your meal.
     * @param id The database id of the meal.
     */
    public void makeEditable(long id) {
        MealFragment newFragment = new MealFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(MAKE_EDITABLE, true);
        bundle.putLong(MEAL_ID, id);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
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

    /**
     * Show meal details on the screen: nameEdit, descriptionEdit, category, grade.
     * @param id The database id of the meal.
     */
    private void displayMeal(Long id) {
        Meal meal = db.getMeal(id);
        nameText = (TextView) layoutView.findViewById(R.id.meal_name_text);
        descriptionText = (TextView) layoutView.findViewById(R.id.meal_description);
        mealImage = (ImageView) layoutView.findViewById(R.id.meal_image);
        categoryText = (TextView) layoutView.findViewById(R.id.category_text);
        averageNumber = (TextView) layoutView.findViewById(R.id.average_number);


        nameText.setText(meal.getName());
        descriptionText.setText(meal.getDescription());
        Uri filePathUri = Uri.parse(meal.getImagePath());
        Bitmap image = BitmapFactory.decodeFile(filePathUri.getPath());
        mealImage.setImageBitmap(image);
        categoryText.setText(meal.getCategory());
        averageNumber.setText(""+meal.getTotalScore());

        //heart.setHearts(getContext(), false, meal.getHealthyScore(), meal.getTasteScore());
    }

    /**
     * Show the meal with editable fields so that the user can change the details of
     * an existing meal.
     * @param id The database id of the meal.
     */
    private void displayEditableMeal(long id) {
        Meal meal = db.getMeal(id);
        nameEdit.setText(meal.getName());
        descriptionEdit.setText(meal.getDescription());

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

    /**
     * Set up click listeners for the rating hearts.
     */
    private void setHeartClickListeners() {
        for (int i = 1; i <= 10; i++) {
            int resNr = getActivity().getResources().getIdentifier("edit_heart_health_" + i, "id",
                    getActivity().getPackageName());
            heartImage = (ImageView) layoutView.findViewById(resNr);
            heartImage.setOnClickListener(heartButtonListener);
            resNr = getActivity().getResources().getIdentifier("edit_heart_taste_" + i, "id",
                    getActivity().getPackageName());
            heartImage = (ImageView) layoutView.findViewById(resNr);
            heartImage.setOnClickListener(heartButtonListener);
        }
    }
}


