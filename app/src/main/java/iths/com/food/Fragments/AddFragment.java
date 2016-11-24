package iths.com.food.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.MainActivity;
import iths.com.food.Model.Category;
import iths.com.food.Model.MyCamera;
import iths.com.food.R;

import static android.app.Activity.RESULT_OK;


/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class AddFragment extends Fragment {

    private ArrayList<Category> categories;
    private Spinner spinner;
    private DatabaseHelper db;
    private static boolean isOpenedFromMenu;
    private ImageView mealImage;
    private EditText name;
    private EditText description;
    private MyCamera camera;
    private static final String TAG = "TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_add, container, false);

        if(isOpenedFromMenu) {
            v = inflater.inflate(R.layout.fragment_add, container, false);
            mealImage = (ImageView) v.findViewById(R.id.edit_meal_image);
            camera = new MyCamera(getActivity());
            camera.takePhoto();
            //TODO: takePhoto() funkar inte, kraschar och visar ingen bild
            name = (EditText) v.findViewById(R.id.name);
            description = (EditText) v.findViewById(R.id.desc);
            isOpenedFromMenu = false;
        } else {

            v = inflater.inflate(R.layout.fragment_meal, container, false);
            mealImage = (ImageView) v.findViewById(R.id.meal_image);
        }

        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        categories = db.getCategories();

        spinner = (Spinner) v.findViewById(R.id.spinner);
        setUpSpinner();

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


}
