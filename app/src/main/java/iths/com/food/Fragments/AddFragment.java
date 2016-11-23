package iths.com.food.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.MainActivity;
import iths.com.food.Model.Category;
import iths.com.food.R;

/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class AddFragment extends Fragment {

    ArrayList<Category> categories;
    Spinner spinner;
    DatabaseHelper db;

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

        return v;
    }

    private void setUpSpinner() {



    }

}
