package iths.com.food.Fragments;

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
import android.widget.Spinner;

import java.util.ArrayList;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.MainActivity;
import iths.com.food.Model.Category;
import iths.com.food.Model.HeartRating;
import iths.com.food.R;

import static android.content.ContentValues.TAG;

/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class AddFragment extends Fragment implements OnClickListener{

    ArrayList<Category> categories;
    Spinner spinner;
    DatabaseHelper db;
    ImageView imageView;
    HeartRating heart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);

        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        categories = db.getCategories();

        String[] categoryNames = new String[categories.size()];
        for(int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }

        spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categoryNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
            default:
                System.out.println("WTF is going on?");
        }
    }

    private void setUpSpinner() {



    }
}
