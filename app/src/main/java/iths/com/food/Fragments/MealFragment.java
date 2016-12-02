package iths.com.food.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;
import iths.com.food.Model.HeartRating;
import iths.com.food.Model.Meal;
import iths.com.food.Model.MyCamera;
import iths.com.food.R;
import iths.com.food.ShareOnFacebookActivity;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static iths.com.food.R.id.container;


/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class MealFragment extends Fragment{

    private static final String MAKE_EDITABLE = "make_editable";
    private static final String MEAL_ID = "meal_id";
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
    private Button shareOnFacebookButton;

    private long id;
    private long current_id = 0;


    private View.OnClickListener shareOnFBListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(current_id!=0){

                /*
                Log.d("test", "Trying to publish2: "+current_id);
                shareOnFacebook();
                */

                Intent intent = new Intent(getActivity(), ShareOnFacebookActivity.class);

                intent.putExtra("id",current_id);

                startActivity(intent);

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

    private CallbackManager callbackManager;
    private LoginManager manager;

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
            shareOnFacebookButton = (Button) layoutView.findViewById(R.id.shareOnFacebookButton);
            shareOnFacebookButton.setOnClickListener(shareOnFBListener);
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

        Log.d("test", "Trying to publish3: "+current_id);

        Log.d("test", "Trying to publish4: "+current_id);

        if(requestCode == MyCamera.CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                camera.showImage(mealImage);
            }
        } else {

            Log.d("test", "Trying to publish (else): "+current_id);
            super.onActivityResult(requestCode, resultCode, intent);
            callbackManager.onActivityResult(requestCode, resultCode, intent);

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

        this.current_id = id;

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

    private void publishImage(){

        Meal meal = db.getMeal(current_id);

        Log.d("test", "Is about to be published: "+current_id);

        Bitmap image = BitmapFactory.decodeFile(meal.getImagePath());

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(meal.getName()+" - "+meal.getDescription())
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

        Log.d("test", "Has been published: "+current_id);

    }

    /*
    @Override
    private void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    */

    public void shareOnFacebook() {

        Log.d("test","Share on facebook!");

        FacebookSdk.sdkInitialize(getContext());

        callbackManager = CallbackManager.Factory.create();
        Log.d("test", "Trying to publish5: "+current_id);

        List<String> permissionNeeds = Arrays.asList("publish_actions");
        Log.d("test", "Trying to publish6: "+current_id);

        manager = LoginManager.getInstance();
        Log.d("test", "Trying to publish7: "+current_id);
        manager.logInWithPublishPermissions(this, permissionNeeds);
        Log.d("test", "Trying to publish8: "+current_id);
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("test", "Trying to publish: "+current_id);
                publishImage();
            }

            @Override
            public void onCancel() {
                Log.d("test", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("test", error.getCause().toString());
                Log.d("test", "Trying to publish: "+current_id);
            }
        });

    }


}


