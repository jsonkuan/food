package iths.com.food.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import iths.com.food.Helper.CategoryAdapter;
import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Helper.MealAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Helper.MealAdapter;
import iths.com.food.Model.Category;
import iths.com.food.Model.Meal;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-25.
 */

public class MealFragment extends Fragment {

    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        db = new DatabaseHelper(getActivity());

        Bundle bundle = this.getArguments();
        String category = bundle.getString(CategoryFragment.CHOSEN_CATEGORY);
        Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
        Category listCategory = db.getCategory(category);
        ArrayList<Meal> mealsInCategory = listCategory.getMeals();
        ArrayList<String> idArray = new ArrayList<>();
        for(int i = 0; i < mealsInCategory.size(); i++) {
            idArray.add(Long.toString(mealsInCategory.get(i).getId()));
        }


        MealAdapter adapter = new MealAdapter(getActivity(), idArray);
        ListView listView = (ListView) view.findViewById(R.id.fragmentMealList);
        listView.setAdapter(adapter);

        db.close();
        return view;
    }


}

/*  LÄGG TILL I MEAL LIST
    public void deleteMeal(View view) {
        String text = ((EditText) findViewById(R.id.idOfMeal)).getText().toString();
        id = Long.valueOf(text);

        int rows = db.deleteMeal(id);

        if(rows > 0) {
            Toast.makeText(getApplicationContext(), "Deleted "+rows+" row(s)", Toast.LENGTH_SHORT).show();
        } else if (rows == 0) {
            Toast.makeText(getApplicationContext(), "No rows deleted", Toast.LENGTH_SHORT).show();
        }



    } */
