package iths.com.food.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import iths.com.food.helper.DatabaseHelper;
import iths.com.food.helper.GPSHelper;
import iths.com.food.helper.sms.SmsSender;
import iths.com.food.model.Category;
import iths.com.food.model.HeartRating;
import iths.com.food.model.Meal;
import iths.com.food.model.MyCamera;
import iths.com.food.R;
import iths.com.food.ShareOnFacebookActivity;

import static android.app.Activity.RESULT_OK;
import static iths.com.food.R.id.container;


/**
 * Created by asakwarnmark on 2016-11-23.
 *
 */

public class MealFragment extends Fragment{

    private static final String MAKE_EDITABLE = "make_editable";
    public static final String MEAL_ID = "meal_id";
    private static final String TAG = "LOGTAG";
    private static boolean isOpenedFromMenu;

    private HeartRating heart;
    private ArrayList<Category> categories;

    private GPSHelper gps;
    private MyCamera camera;

    private View layoutView;
    private ImageView mealImage, heartImage, cameraIcon;
    private EditText nameEdit, descriptionEdit;
    private TextView nameText, descriptionText, categoryText, averageNumber;
    private Spinner spinner;
    private Button saveButton, editButton, shareOnFacebookButton, btnSendSms;

    private long id;
    private long current_id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_meal_editable, container, false);

        setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) layoutView.findViewById(R.id.meal_editable_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        myToolbar.setTitle("FoodFlash!");
        myToolbar.setLogo(R.drawable.empty_heart);

        gps = new GPSHelper(getActivity());

        DatabaseHelper db = new DatabaseHelper(getActivity());
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
            Log.d(TAG, "Log point 1");
            isOpenedFromMenu = false;
        } else if (bundle.getBoolean(MAKE_EDITABLE)) {
            mealImage = (ImageView) layoutView.findViewById(R.id.edit_meal_image);
            saveButton.setOnClickListener(updateButtonListener);
            setUpSpinner();
            setHeartClickListeners();
            displayEditableMeal(bundle.getLong(MEAL_ID));
        }
        else {
            layoutView = inflater.inflate(R.layout.fragment_meal, container, false);
            mealImage = (ImageView) layoutView.findViewById(R.id.meal_image);
            editButton = (Button) layoutView.findViewById(R.id.edit_button);
            editButton.setOnClickListener(editButtonListener);
            id = bundle.getLong(MealListFragment.MEAL_ID);
            shareOnFacebookButton = (Button) layoutView.findViewById(R.id.shareOnFacebookButton);
            shareOnFacebookButton.setOnClickListener(shareOnFBListener);
            btnSendSms = (Button) layoutView.findViewById(R.id.btn_send_sms);
            btnSendSms.setOnClickListener(btnSendSmsListener);
        }
        heart = new HeartRating(layoutView, getActivity().getApplicationContext(), getActivity());
        bundle = this.getArguments();
        Log.d(TAG, "Log point 2");
        if(bundle == null || bundle.getBoolean(MAKE_EDITABLE)) {
        } else {
            long id = bundle.getLong(MealListFragment.MEAL_ID);
            displayMeal(id);
        }

        db.close();
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
        meal.setLatitude(gps.getLatitude());
        meal.setLongitude(gps.getLongitude());
        String imagePath = camera.getPhotoFilePath().getPath();
        meal.setImagePath(imagePath);

        meal.setLatitude(0);
        meal.setLongitude(0);
        Log.d(TAG, "imagePath = " + imagePath);
        meal.setImagePath(imagePath);

        DatabaseHelper db = new DatabaseHelper(getActivity());
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

        Log.d(TAG, "In updateMeal");

        DatabaseHelper db = new DatabaseHelper(getActivity());

        Meal meal = db.getMeal(id);
        meal.setHealthyScore(HeartRating.getHealthGrade());
        meal.setTasteScore(HeartRating.getTasteGrade());
        meal.setName(nameEdit.getText().toString());
        meal.setDescription(descriptionEdit.getText().toString());
        meal.setCategory(spinner.getSelectedItem().toString());

        String imagePath = "";
        if(camera != null) {
            imagePath = camera.getPhotoFilePath().getPath();
            meal.setImagePath(imagePath);
        }

        db.updateMeal(meal);

        Log.d(TAG, "imagePath = " + imagePath);

        db.close();

        getActivity().getSupportFragmentManager().beginTransaction()
               .replace(container, new CategoryFragment()).commit();
    }

    /**
     * Change the screen so that you can edit your meal.
     * @param id The database id of the meal.
     */
    public void makeEditable(long id) {
        Log.d(TAG, "making stuff editable");
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

        this.current_id = id;
        DatabaseHelper db = new DatabaseHelper(getActivity());

        Meal meal = db.getMeal(id);
        nameText = (TextView) layoutView.findViewById(R.id.meal_name_text);
        descriptionText = (TextView) layoutView.findViewById(R.id.meal_description);
        mealImage = (ImageView) layoutView.findViewById(R.id.meal_image);
        categoryText = (TextView) layoutView.findViewById(R.id.category_text);
        averageNumber = (TextView) layoutView.findViewById(R.id.average_number);


        nameText.setText(meal.getName());
        descriptionText.setText(meal.getDescription());
        Log.d(TAG, "Right before setting image");
        Uri filePathUri = Uri.parse(meal.getImagePath());
        Bitmap image = MyCamera.rotatePhoto(filePathUri.getPath());
        mealImage.setImageBitmap(image);
        categoryText.setText(meal.getCategory());
        averageNumber.setText(""+meal.getTotalScore());

        heart.setHearts(false, meal.getHealthyScore(), meal.getTasteScore());
        db.close();
    }

    /**
     * Show the meal with editable fields so that the user can change the details of
     * an existing meal.
     * @param id The database id of the meal.
     */
    private void displayEditableMeal(long id) {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        Meal meal = db.getMeal(id);
        nameEdit.setText(meal.getName());
        descriptionEdit.setText(meal.getDescription());

        Uri filePathUri = Uri.parse(meal.getImagePath());
        Bitmap image = MyCamera.rotatePhoto(filePathUri.getPath());
        mealImage.setImageBitmap(image);

        ArrayList<Category> categories = db.getCategories();
        int position = 0;
        for(int i = 0; i < categories.size(); i++) {
            if (meal.getCategory().equals(categories.get(i).getName())) {
                position = i;
            }
        }

        spinner.setSelection(position);
        HeartRating heart = new HeartRating(layoutView, getContext(), getActivity());
        heart.setHearts(true, meal.getHealthyScore(), meal.getTasteScore());
        db.close();
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


    private View.OnClickListener shareOnFBListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(current_id!=0){
                Intent intent = new Intent(getActivity(), ShareOnFacebookActivity.class);
                intent.putExtra("id",current_id);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener btnSendSmsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(current_id!=0){
                Meal meal = new Meal();

                meal.setName(nameText.getText().toString());
                meal.setId(id);

                SmsSender.sendSms(getContext(), meal);
            }
        }
    };

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
}


